package com.dashkin.tunepitch.core.audio.detector

// Pure Kotlin implementation of the YIN pitch detection algorithm.
// Reference: de Cheveigné, A., & Kawahara, H. (2002). YIN, a fundamental frequency estimator
// for speech and music. Journal of the Acoustical Society of America, 111(4), 1917–1930.
// Algorithm steps (as numbered in the paper):
// 1. Difference function
// 2. Cumulative mean normalised difference function (CMNDF)
// 3. Absolute threshold — find first tau below [threshold]
// 4. Parabolic interpolation — refine the estimate
internal class YinPitchDetector(
    private val sampleRate: Int, // Audio sample rate in Hz (e.g. 44100)
    private val threshold: Double = 0.15 // CMNDF threshold. Lower → fewer false positives, may miss faint pitches
) {

    data class Result(
        val pitch: Float, // Detected fundamental frequency in Hz, or -1f if no pitch found
        val probability: Float, // Confidence of the detection. Range: 0f to 1f.
        val isPitched: Boolean
    )

    // Analyses [buffer] and returns the detected pitch.
    // buffer Normalised audio samples in the range [-1.0, 1.0].
    // Buffer size must be even; the algorithm uses the first half as the lag window.
    fun getPitch(buffer: FloatArray): Result {
        val halfSize = buffer.size / 2
        val yinBuffer = DoubleArray(halfSize)

        step1DifferenceFunction(buffer, yinBuffer)
        step2CumulativeMeanNormalizedDifference(yinBuffer)

        val tauEstimate = step3AbsoluteThreshold(yinBuffer)
        if (tauEstimate == -1) return Result(-1f, 0f, false)

        val refinedTau = step4ParabolicInterpolation(yinBuffer, tauEstimate)
        val frequency = sampleRate / refinedTau
        val probability = (1.0 - yinBuffer[tauEstimate]).toFloat().coerceIn(0f, 1f)

        return Result(frequency, probability, true)
    }

    //Step 1 — d(τ): squared difference of the signal with a delayed copy of itself.
    private fun step1DifferenceFunction(buffer: FloatArray, yinBuffer: DoubleArray) {
        val halfSize = yinBuffer.size
        for (tau in yinBuffer.indices) {
            yinBuffer[tau] = 0.0
            for (j in 0 until halfSize) {
                val delta = buffer[j].toDouble() - buffer[j + tau].toDouble()
                yinBuffer[tau] += delta * delta
            }
        }
    }

    // Step 2 — d'(τ): normalise d(τ) by its cumulative mean to suppress low-frequency bias.
    private fun step2CumulativeMeanNormalizedDifference(yinBuffer: DoubleArray) {
        yinBuffer[0] = 1.0
        var runningSum = 0.0
        for (tau in 1 until yinBuffer.size) {
            runningSum += yinBuffer[tau]
            yinBuffer[tau] = yinBuffer[tau] * tau / runningSum
        }
    }

    // Step 3 — find the first τ where d'(τ) dips below [threshold].
    // Walks forward to find the local minimum within that dip.
    // return Estimated period τ, or -1 if none found.
    private fun step3AbsoluteThreshold(yinBuffer: DoubleArray): Int {
        var tau = 2
        while (tau < yinBuffer.size) {
            if (yinBuffer[tau] < threshold) {
                while (tau + 1 < yinBuffer.size && yinBuffer[tau + 1] < yinBuffer[tau]) {
                    tau++
                }
                return tau
            }
            tau++
        }
        return -1
    }

    // Step 4 — fit a parabola through the three points around tauEstimate and
    // return the interpolated minimum, giving subsample precision.
    private fun step4ParabolicInterpolation(yinBuffer: DoubleArray, tauEstimate: Int): Float {
        val x0 = maxOf(tauEstimate - 1, 0)
        val x2 = minOf(tauEstimate + 1, yinBuffer.size - 1)
        return when {
            x0 == tauEstimate ->
                if (yinBuffer[tauEstimate] <= yinBuffer[x2]) tauEstimate.toFloat() else x2.toFloat()
            x2 == tauEstimate ->
                if (yinBuffer[tauEstimate] <= yinBuffer[x0]) tauEstimate.toFloat() else x0.toFloat()
            else -> {
                val s0 = yinBuffer[x0]
                val s1 = yinBuffer[tauEstimate]
                val s2 = yinBuffer[x2]
                (tauEstimate + (s2 - s0) / (2.0 * (2.0 * s1 - s2 - s0))).toFloat()
            }
        }
    }
}
