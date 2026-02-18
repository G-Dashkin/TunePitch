package com.dashkin.tunepitch.feature.home.presentation.state

sealed interface HomeSideEffect {
    data class NavigateToExercise(val exerciseId: String) : HomeSideEffect
    data object NavigateToTuner : HomeSideEffect
}
