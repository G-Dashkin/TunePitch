package com.dashkin.tunepitch.feature.tuner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashkin.tunepitch.core.audio.detector.PitchDetector
import com.dashkin.tunepitch.feature.tuner.presentation.state.TunerError
import com.dashkin.tunepitch.feature.tuner.presentation.state.TunerEvent
import com.dashkin.tunepitch.feature.tuner.presentation.state.TunerState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val PITCH_TIMEOUT_MS = 500L

class TunerViewModel(private val pitchDetector: PitchDetector) : ViewModel() {

    private val _state = MutableStateFlow(TunerState())
    val state: StateFlow<TunerState> = _state.asStateFlow()

    private var pitchCollectionJob: Job? = null

    fun onEvent(event: TunerEvent) {
        when (event) {
            TunerEvent.OnPermissionGranted -> startRecording()
            TunerEvent.OnPermissionDenied -> handlePermissionDenied()
        }
    }

    private fun startRecording() {
        _state.update { it.copy(hasPermission = true, error = null, isRecording = true) }
        pitchCollectionJob?.cancel()
        pitchCollectionJob = viewModelScope.launch {
            try {
                pitchDetector.pitchResultsFlow()
                    .transformLatest { result ->
                        emit(result)
                        delay(PITCH_TIMEOUT_MS)
                        emit(null)
                    }
                    .collect { result ->
                        _state.update { it.copy(pitchResult = result) }
                    }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(isRecording = false, error = TunerError.MicrophoneUnavailable) }
            }
        }
    }

    private fun handlePermissionDenied() {
        _state.update { it.copy(hasPermission = false, error = TunerError.NoPermission) }
    }

    override fun onCleared() {
        super.onCleared()
        pitchCollectionJob?.cancel()
    }
}
