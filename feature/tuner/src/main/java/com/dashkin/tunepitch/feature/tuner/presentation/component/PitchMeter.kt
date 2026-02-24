package com.dashkin.tunepitch.feature.tuner.presentation.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.dashkin.tunepitch.core.ui.theme.NeonSurfaceVariant
import com.dashkin.tunepitch.core.ui.theme.NeonTextTertiary
import com.dashkin.tunepitch.core.ui.theme.PitchGreen
import com.dashkin.tunepitch.core.ui.theme.PitchRed
import com.dashkin.tunepitch.core.ui.theme.PitchYellow
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

private const val ARC_START_ANGLE = 180f
private const val ARC_SWEEP_TOTAL = 180f

// Cent thresholds mapped to arc degrees
// -50 cents = 180°, 0 cents = 270°, +50 cents = 360°
private const val DEGREE_PER_CENT = ARC_SWEEP_TOTAL / 100f

// Segment boundaries in arc degrees
private const val SEG_RED_LEFT_END = ARC_START_ANGLE + 45f      // -25 cents
private const val SEG_YELLOW_LEFT_END = ARC_START_ANGLE + 72f   // -10 cents
private const val SEG_GREEN_END = ARC_START_ANGLE + 108f         // +10 cents
private const val SEG_YELLOW_RIGHT_END = ARC_START_ANGLE + 135f // +25 cents

// Semicircular pitch accuracy meter.
// Displays a color-coded arc (red → yellow → green → yellow → red) with an
// animated needle showing the current cents offset from the target pitch.
@Composable
fun PitchMeter(
    centsOffset: Float, // Current deviation in cents. Negative = flat, positive = sharp. Range: -50..+50.
    modifier: Modifier = Modifier // Modifier for sizing. Recommended aspect ratio 2:1 (width:height).
) {
    val animatedCents by animateFloatAsState(
        targetValue = centsOffset.coerceIn(-50f, 50f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pitch_meter_cents"
    )

    val needleColor = when {
        abs(animatedCents) <= 10f -> PitchGreen
        abs(animatedCents) <= 25f -> PitchYellow
        else -> PitchRed
    }

    Canvas(modifier = modifier.fillMaxWidth()) {
        val radius = size.width * 0.42f
        val centerX = size.width / 2f
        val centerY = size.height * 0.92f
        val trackWidth = 14.dp.toPx()
        val arcTopLeft = Offset(centerX - radius, centerY - radius)
        val arcSize = Size(radius * 2f, radius * 2f)
        val strokeStyle = Stroke(width = trackWidth, cap = StrokeCap.Butt)

        // Background track
        drawArc(
            color = NeonSurfaceVariant,
            startAngle = ARC_START_ANGLE,
            sweepAngle = ARC_SWEEP_TOTAL,
            useCenter = false,
            style = Stroke(width = trackWidth, cap = StrokeCap.Round),
            topLeft = arcTopLeft,
            size = arcSize
        )

        // Red segment: -50 to -25 cents
        drawArc(
            color = PitchRed.copy(alpha = 0.75f),
            startAngle = ARC_START_ANGLE,
            sweepAngle = 45f,
            useCenter = false,
            style = strokeStyle,
            topLeft = arcTopLeft,
            size = arcSize
        )
        // Yellow segment: -25 to -10 cents
        drawArc(
            color = PitchYellow.copy(alpha = 0.75f),
            startAngle = SEG_RED_LEFT_END,
            sweepAngle = 27f,
            useCenter = false,
            style = strokeStyle,
            topLeft = arcTopLeft,
            size = arcSize
        )
        // Green segment: -10 to +10 cents
        drawArc(
            color = PitchGreen.copy(alpha = 0.75f),
            startAngle = SEG_YELLOW_LEFT_END,
            sweepAngle = 36f,
            useCenter = false,
            style = strokeStyle,
            topLeft = arcTopLeft,
            size = arcSize
        )
        // Yellow segment: +10 to +25 cents
        drawArc(
            color = PitchYellow.copy(alpha = 0.75f),
            startAngle = SEG_GREEN_END,
            sweepAngle = 27f,
            useCenter = false,
            style = strokeStyle,
            topLeft = arcTopLeft,
            size = arcSize
        )
        // Red segment: +25 to +50 cents
        drawArc(
            color = PitchRed.copy(alpha = 0.75f),
            startAngle = SEG_YELLOW_RIGHT_END,
            sweepAngle = 45f,
            useCenter = false,
            style = strokeStyle,
            topLeft = arcTopLeft,
            size = arcSize
        )

        // Tick marks at -50, -25, 0, +25, +50 cents
        val tickAngles = listOf(180f, 225f, 270f, 315f, 360f)
        val innerTickRadius = radius - trackWidth * 0.9f
        val outerTickRadius = radius + trackWidth * 0.9f
        tickAngles.forEach { angleDeg ->
            val angleRad = Math.toRadians(angleDeg.toDouble())
            val cosA = cos(angleRad).toFloat()
            val sinA = sin(angleRad).toFloat()
            drawLine(
                color = NeonTextTertiary,
                start = Offset(centerX + innerTickRadius * cosA, centerY + innerTickRadius * sinA),
                end = Offset(centerX + outerTickRadius * cosA, centerY + outerTickRadius * sinA),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Needle
        val needleAngleDeg = ARC_START_ANGLE + ((animatedCents + 50f) / 100f) * ARC_SWEEP_TOTAL
        val needleAngleRad = Math.toRadians(needleAngleDeg.toDouble())
        val needleLength = radius * 0.82f
        val needleEndX = centerX + needleLength * cos(needleAngleRad).toFloat()
        val needleEndY = centerY + needleLength * sin(needleAngleRad).toFloat()

        drawLine(
            color = needleColor,
            start = Offset(centerX, centerY),
            end = Offset(needleEndX, needleEndY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Pivot glow
        drawCircle(
            color = needleColor.copy(alpha = 0.35f),
            radius = 18.dp.toPx(),
            center = Offset(centerX, centerY)
        )
        // Pivot dot
        drawCircle(
            color = needleColor,
            radius = 7.dp.toPx(),
            center = Offset(centerX, centerY)
        )
    }
}
