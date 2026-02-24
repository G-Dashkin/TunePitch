package com.dashkin.tunepitch.core.audio.config

import android.media.AudioFormat

// Central configuration constants for audio capture and pitch detection.
// All magic numbers related to audio processing live here to keep the rest of
// the codebase free of unexplained literals.
internal object AudioConfig {

    // Samples per second used for recording and pitch analysis.
    const val SAMPLE_RATE = 44100

    // Mono input — sufficient for pitch detection and reduces CPU load.
    const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO

    // 16-bit PCM — standard quality, wide hardware support.
    const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    // Number of samples per processing buffer.
    // 4096 samples at 44100 Hz ≈ 93 ms per frame — a good trade-off between
    // detection latency and algorithm accuracy.
    const val BUFFER_SIZE_SAMPLES = 4096

    // YIN algorithm confidence threshold.
    // Lower value → fewer false positives but may miss faint pitches.
    const val YIN_THRESHOLD = 0.15

    // Ignore frequencies below this value (below E1 ≈ 41 Hz — not a singing pitch).
    const val MIN_FREQUENCY = 50f

    // Ignore frequencies above this value (comfortably above soprano high C ≈ 1047 Hz).
    const val MAX_FREQUENCY = 1600f

    // Divisor for normalising PCM-16 short samples to the [-1.0, 1.0] float range.
    const val PCM_NORMALIZATION_FACTOR = 32768f

    // Minimum YIN probability to emit a PitchResult.
    // Results below this threshold are discarded as unreliable.
    const val MIN_CONFIDENCE = 0.8f
}
