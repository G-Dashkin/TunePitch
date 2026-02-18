package com.dashkin.tunepitch.feature.home.presentation.state

sealed interface HomeEvent {
    data class OnExerciseClick(val exerciseId: String) : HomeEvent
    data object OnTunerClick : HomeEvent
}
