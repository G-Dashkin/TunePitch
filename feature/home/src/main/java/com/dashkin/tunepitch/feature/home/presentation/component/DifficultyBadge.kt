package com.dashkin.tunepitch.feature.home.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dashkin.tunepitch.core.ui.theme.DifficultyAdvanced
import com.dashkin.tunepitch.core.ui.theme.DifficultyBeginner
import com.dashkin.tunepitch.core.ui.theme.DifficultyIntermediate
import com.dashkin.tunepitch.feature.home.domain.model.Difficulty

@Composable
fun DifficultyBadge(
    difficulty: Difficulty,
    modifier: Modifier = Modifier
) {
    val color = when (difficulty) {
        Difficulty.BEGINNER -> DifficultyBeginner
        Difficulty.INTERMEDIATE -> DifficultyIntermediate
        Difficulty.ADVANCED -> DifficultyAdvanced
    }

    val label = when (difficulty) {
        Difficulty.BEGINNER -> "Beginner"
        Difficulty.INTERMEDIATE -> "Intermediate"
        Difficulty.ADVANCED -> "Advanced"
    }

    val shape = RoundedCornerShape(20.dp)

    Surface(
        modifier = modifier
            .clip(shape)
            .border(width = 1.dp, color = color.copy(alpha = 0.6f), shape = shape),
        shape = shape,
        color = color.copy(alpha = 0.10f),
        contentColor = color
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
