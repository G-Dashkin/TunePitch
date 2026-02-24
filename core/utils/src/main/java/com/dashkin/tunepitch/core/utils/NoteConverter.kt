package com.dashkin.tunepitch.core.utils

import kotlin.math.log2
import kotlin.math.roundToInt

// Converts audio frequencies (Hz) to musical note information.
// Uses the standard formula: MIDI note = 69 + 12 * log2(frequency / 440) where MIDI note 69 = A4 = 440 Hz.
object NoteConverter {

    private const val A4_MIDI_NUMBER = 69
    private const val A4_FREQUENCY_HZ = 440.0
    private const val SEMITONES_PER_OCTAVE = 12
    private const val CENTS_PER_SEMITONE = 100f
    private const val MIDI_TO_OCTAVE_OFFSET = 1

    // Converts a frequency in Hz to a [NoteInfo].
    // Frequency in Hz. Must be positive.
    // return NoteInfo with note name, octave, and cents deviation, or `null` if frequency is invalid.
    fun fromFrequency(frequency: Float): NoteInfo? {
        if (frequency <= 0f) return null

        val midiNote = A4_MIDI_NUMBER + SEMITONES_PER_OCTAVE * log2(frequency / A4_FREQUENCY_HZ)
        val roundedMidi = midiNote.roundToInt()

        val noteIndex = ((roundedMidi % SEMITONES_PER_OCTAVE) + SEMITONES_PER_OCTAVE) % SEMITONES_PER_OCTAVE
        val octave = roundedMidi / SEMITONES_PER_OCTAVE - MIDI_TO_OCTAVE_OFFSET
        val centsOffset = ((midiNote - roundedMidi) * CENTS_PER_SEMITONE).toFloat()

        return NoteInfo(
            note = Note.entries[noteIndex],
            octave = octave,
            centsOffset = centsOffset
        )
    }
}

// Combines a musical [Note], its octave number, and a cents deviation from the exact pitch.
// note The nearest note.
// octave Scientific pitch octave (e.g. 4 for middle-C octave).
// centsOffset Deviation in cents. Negative = flat, positive = sharp. Range: -50f to +50f.
data class NoteInfo(
    val note: Note,
    val octave: Int,
    val centsOffset: Float
) {
    // Human-readable representation, e.g. "A4" or "C#3".
    override fun toString(): String = "${note.displayName}$octave"
}
