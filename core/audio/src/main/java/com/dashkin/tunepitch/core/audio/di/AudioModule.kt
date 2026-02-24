package com.dashkin.tunepitch.core.audio.di

import com.dashkin.tunepitch.core.audio.detector.PitchDetectionEngine
import com.dashkin.tunepitch.core.audio.detector.PitchDetector
import com.dashkin.tunepitch.core.audio.detector.PitchDetectorImpl
import com.dashkin.tunepitch.core.audio.recorder.AudioRecordWrapper
import org.koin.dsl.module

val audioModule = module {
    factory { AudioRecordWrapper() }
    factory { PitchDetectionEngine() }
    single<PitchDetector> { PitchDetectorImpl(get(), get()) }
}
