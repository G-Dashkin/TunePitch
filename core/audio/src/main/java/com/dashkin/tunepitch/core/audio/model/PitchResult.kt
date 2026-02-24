package com.dashkin.tunepitch.core.audio.model

import com.dashkin.tunepitch.core.utils.Note

// Represents the result of a single pitch-detection frame from the microphone.
data class PitchResult(
    val frequency: Float, // Detected fundamental frequency in Hz
    val note: Note, // The nearest musical note to frequency
    val octave: Int, // Scientific pitch octave of note (e.g. 4 for the middle-C octave)
    val centsOffset: Float, // Deviation from the exact note frequency in cents. Negative = flat, positive = sharp. Range: -50f to +50f
    val confidence: Float, // Detection confidence produced by the YIN algorithm. Range: 0f to 1f
    val timestamp: Long // System time when this result was produced, in milliseconds
) {
    // Human-readable note label, e.g. "A4" or "C#3".
    val noteLabel: String get() = "${note.displayName}$octave"
}
