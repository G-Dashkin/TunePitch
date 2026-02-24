package com.dashkin.tunepitch.feature.tuner.presentation.state

import com.dashkin.tunepitch.core.audio.model.PitchResult

data class TunerState(
    val pitchResult: PitchResult? = null,
    val hasPermission: Boolean = false,
    val isRecording: Boolean = false,
    val error: TunerError? = null
)

sealed class TunerError {
    data object NoPermission : TunerError()
    data object MicrophoneUnavailable : TunerError()
}
