package com.dashkin.tunepitch.feature.home.presentation.state

import com.dashkin.tunepitch.feature.home.domain.model.Exercise
import com.dashkin.tunepitch.feature.home.domain.model.ExerciseResult

data class HomeState(
    val exercises: List<Exercise> = emptyList(),
    val bestResults: Map<String, ExerciseResult> = emptyMap(),
    val isLoading: Boolean = true
)
