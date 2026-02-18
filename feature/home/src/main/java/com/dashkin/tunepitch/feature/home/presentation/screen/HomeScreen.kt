package com.dashkin.tunepitch.feature.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dashkin.tunepitch.core.ui.theme.NeonBackground
import com.dashkin.tunepitch.core.ui.theme.NeonCyan
import com.dashkin.tunepitch.core.ui.theme.NeonGlowCyan
import com.dashkin.tunepitch.core.ui.theme.NeonGlowMagenta
import com.dashkin.tunepitch.core.ui.theme.NeonMagenta
import com.dashkin.tunepitch.core.ui.theme.NeonPurple
import com.dashkin.tunepitch.core.ui.theme.NeonSurface
import com.dashkin.tunepitch.core.ui.theme.NeonSurfaceVariant
import com.dashkin.tunepitch.core.ui.theme.NeonTextSecondary
import com.dashkin.tunepitch.feature.home.presentation.component.ExerciseCard
import com.dashkin.tunepitch.feature.home.presentation.state.HomeEvent
import com.dashkin.tunepitch.feature.home.presentation.state.HomeSideEffect
import com.dashkin.tunepitch.feature.home.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToExercise: (String) -> Unit,
    onNavigateToTuner: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HomeSideEffect.NavigateToExercise -> {
                    onNavigateToExercise(effect.exerciseId)
                }
                is HomeSideEffect.NavigateToTuner -> {
                    onNavigateToTuner()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeonBackground)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = NeonCyan
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 60.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                item {
                    HomeHeader()
                }

                // Tuner button
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    TunerButton(
                        onClick = { viewModel.onEvent(HomeEvent.OnTunerClick) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Section title
                item {
                    Text(
                        text = "EXERCISES",
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 3.sp,
                        color = NeonTextSecondary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                // Exercise cards
                items(
                    items = state.exercises,
                    key = { it.id }
                ) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        bestResult = state.bestResults[exercise.id],
                        onClick = {
                            viewModel.onEvent(HomeEvent.OnExerciseClick(exercise.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "TunePitch",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = NeonCyan
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Train your voice. Hit every note.",
            style = MaterialTheme.typography.bodyLarge,
            color = NeonTextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun TunerButton(onClick: () -> Unit) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(NeonCyan, NeonPurple, NeonMagenta)
    )

    val bgBrush = Brush.horizontalGradient(
        colors = listOf(NeonGlowCyan, NeonGlowMagenta)
    )

    val shape = RoundedCornerShape(16.dp)

    Surface(
        onClick = onClick,
        shape = shape,
        color = NeonSurface,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(width = 1.dp, brush = gradientBrush, shape = shape)
    ) {
        Box(
            modifier = Modifier
                .background(brush = bgBrush)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Free Tuner",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonCyan
                )
            }
        }
    }
}
