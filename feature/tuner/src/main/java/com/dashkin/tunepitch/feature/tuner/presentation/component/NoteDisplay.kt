package com.dashkin.tunepitch.feature.tuner.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dashkin.tunepitch.core.audio.model.PitchResult
import com.dashkin.tunepitch.core.ui.theme.NeonCyan
import com.dashkin.tunepitch.core.ui.theme.NeonGlowCyan
import com.dashkin.tunepitch.core.ui.theme.NeonTextSecondary
import com.dashkin.tunepitch.core.ui.theme.NeonTextTertiary

// Displays the currently detected note and its frequency.
// Shows "---" and no frequency when no pitch is detected.
@Composable
fun NoteDisplay(
    pitchResult: PitchResult?, // pitchResult The latest pitch detection result, or null when silent
    modifier: Modifier = Modifier // Modifier applied to the root column
) {
    val noteLabel = pitchResult?.noteLabel ?: "---"
    val frequencyText = pitchResult?.let { "%.1f Hz".format(it.frequency) } ?: ""
    val isDetected = pitchResult != null

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = noteLabel,
            fontSize = 96.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDetected) NeonCyan else NeonTextTertiary,
            modifier = Modifier.drawBehind {
                if (isDetected) drawNoteGlow()
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = frequencyText,
            style = MaterialTheme.typography.titleLarge,
            color = NeonTextSecondary
        )
    }
}

private fun DrawScope.drawNoteGlow() {
    drawCircle(
        color = NeonGlowCyan,
        radius = size.maxDimension * 0.65f
    )
}
