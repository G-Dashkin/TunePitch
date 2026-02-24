package com.dashkin.tunepitch.core.audio.detector

import com.dashkin.tunepitch.core.audio.model.PitchResult
import com.dashkin.tunepitch.core.audio.recorder.AudioRecordWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

// High-level API for real-time pitch detection from the device microphone.
// Callers collect [pitchResultsFlow] within an appropriate coroutine scope
// (e.g. `viewModelScope`). The microphone is opened on collection and released
// automatically on cancellation — no additional lifecycle management is required.
interface PitchDetector {

    // Cold Flow that emits a PitchResult for every audio frame in which a
    // confident pitch is detected.
    // Starts recording when the flow is collected.
    // Stops recording and releases the microphone when the collector is cancelled.
    // Frames without a detectable pitch are silently dropped.
    fun pitchResultsFlow(): Flow<PitchResult>
}

internal class PitchDetectorImpl(
    private val audioRecordWrapper: AudioRecordWrapper,
    private val engine: PitchDetectionEngine
) : PitchDetector {

    override fun pitchResultsFlow(): Flow<PitchResult> =
        audioRecordWrapper.audioFlow().mapNotNull { buffer -> engine.detect(buffer) }
}
