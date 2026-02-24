package com.dashkin.tunepitch.core.audio.detector

import com.dashkin.tunepitch.core.audio.config.AudioConfig
import com.dashkin.tunepitch.core.audio.model.PitchResult
import com.dashkin.tunepitch.core.utils.NoteConverter

// Must be called from a single thread or coroutine (not thread-safe).
internal class PitchDetectionEngine {

    private val yin = YinPitchDetector(
        sampleRate = AudioConfig.SAMPLE_RATE,
        threshold = AudioConfig.YIN_THRESHOLD
    )

    // Analyses a PCM ShortArray and returns a PitchResult if a confident pitch
    // is detected within the vocal range, or `null` otherwise.
    fun detect(buffer: ShortArray): PitchResult? {
        val floatBuffer = toFloatBuffer(buffer)
        val result = yin.getPitch(floatBuffer)

        if (!result.isPitched) return null
        if (result.probability < AudioConfig.MIN_CONFIDENCE) return null

        val frequency = result.pitch
        if (frequency < AudioConfig.MIN_FREQUENCY || frequency > AudioConfig.MAX_FREQUENCY) return null

        val noteInfo = NoteConverter.fromFrequency(frequency) ?: return null

        return PitchResult(
            frequency = frequency,
            note = noteInfo.note,
            octave = noteInfo.octave,
            centsOffset = noteInfo.centsOffset,
            confidence = result.probability,
            timestamp = System.currentTimeMillis()
        )
    }

    // Converts PCM-16 short samples to normalised floats in the range [-1.0, 1.0].
    private fun toFloatBuffer(buffer: ShortArray): FloatArray =
        FloatArray(buffer.size) { i -> buffer[i] / AudioConfig.PCM_NORMALIZATION_FACTOR }
}
