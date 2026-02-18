package com.dashkin.tunepitch.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dashkin.tunepitch.core.ui.theme.NeonCyan
import com.dashkin.tunepitch.core.ui.theme.NeonGlowCyan
import com.dashkin.tunepitch.core.ui.theme.NeonMagenta
import com.dashkin.tunepitch.core.ui.theme.NeonPurple
import com.dashkin.tunepitch.core.ui.theme.NeonSurface
import com.dashkin.tunepitch.core.ui.theme.NeonSurfaceVariant
import com.dashkin.tunepitch.core.ui.theme.NeonTextSecondary
import com.dashkin.tunepitch.feature.home.domain.model.Exercise
import com.dashkin.tunepitch.feature.home.domain.model.ExerciseResult

private val CardShape = RoundedCornerShape(16.dp)

@Composable
fun ExerciseCard(
    exercise: Exercise,
    bestResult: ExerciseResult?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderGradient = Brush.linearGradient(
        colors = listOf(
            NeonCyan.copy(alpha = 0.5f),
            NeonPurple.copy(alpha = 0.3f),
            NeonMagenta.copy(alpha = 0.2f)
        )
    )

    val cardBackground = Brush.verticalGradient(
        colors = listOf(
            NeonSurfaceVariant.copy(alpha = 0.7f),
            NeonSurface
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .border(width = 1.dp, brush = borderGradient, shape = CardShape)
            .background(brush = cardBackground)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exercise number indicator
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NeonGlowCyan)
                    .border(1.dp, NeonCyan.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = exercise.noteCount.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = exercise.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    DifficultyBadge(difficulty = exercise.difficulty)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = exercise.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonTextSecondary
                )

                if (bestResult != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Best: ${bestResult.bestScore}%",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = NeonTextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
