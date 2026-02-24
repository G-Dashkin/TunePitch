package com.dashkin.tunepitch.core.audio.recorder

import android.media.AudioRecord
import android.media.MediaRecorder
import com.dashkin.tunepitch.core.audio.config.AudioConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive

// Wraps Android's AudioRecord and exposes raw PCM audio data as a coroutine (Flow).
// The microphone is opened when the flow is collected and released automatically
// when collection is cancelled or completes — no explicit lifecycle management needed.
internal class AudioRecordWrapper {

    // Returns a cold Flow that emits ShortArray PCM buffers captured from the microphone.
    // Recording starts on the first collection and stops when the collector is cancelled.
    // The flow runs on Dispatchers.IO because AudioRecord.read is a blocking call.
    fun audioFlow(): Flow<ShortArray> = flow {
        val audioRecord = createAudioRecord() ?: return@flow
        audioRecord.startRecording()
        try {
            val buffer = ShortArray(AudioConfig.BUFFER_SIZE_SAMPLES)
            while (currentCoroutineContext().isActive) {
                val samplesRead = audioRecord.read(buffer, 0, buffer.size)
                if (samplesRead > 0) emit(buffer.copyOf())
            }
        } finally {
            audioRecord.stop()
            audioRecord.release()
        }
    }.flowOn(Dispatchers.IO)

    private fun createAudioRecord(): AudioRecord? {
        val minBufferBytes = AudioRecord.getMinBufferSize(
            AudioConfig.SAMPLE_RATE,
            AudioConfig.CHANNEL_CONFIG,
            AudioConfig.AUDIO_FORMAT
        )
        if (minBufferBytes <= 0) return null

        // Ensure the hardware buffer is at least as large as our processing buffer.
        val bufferBytes = maxOf(minBufferBytes, AudioConfig.BUFFER_SIZE_SAMPLES * Short.SIZE_BYTES)

        val record = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            AudioConfig.SAMPLE_RATE,
            AudioConfig.CHANNEL_CONFIG,
            AudioConfig.AUDIO_FORMAT,
            bufferBytes
        )
        return record.takeIf { it.state == AudioRecord.STATE_INITIALIZED }
    }
}
