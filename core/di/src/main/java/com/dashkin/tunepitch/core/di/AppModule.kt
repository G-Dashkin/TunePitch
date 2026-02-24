package com.dashkin.tunepitch.core.di

import com.dashkin.tunepitch.core.audio.di.audioModule
import com.dashkin.tunepitch.feature.home.di.homeModule
import com.dashkin.tunepitch.feature.tuner.di.tunerModule
import org.koin.dsl.module

val appModule = module {
    includes(homeModule, audioModule, tunerModule)
}
