package com.dashkin.tunepitch.feature.tuner.presentation.state

sealed class TunerEvent {
    data object OnPermissionGranted : TunerEvent()
    data object OnPermissionDenied : TunerEvent()
}
