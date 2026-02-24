package com.dashkin.tunepitch.feature.tuner.di

import com.dashkin.tunepitch.feature.tuner.presentation.viewmodel.TunerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val tunerModule = module {
    viewModelOf(::TunerViewModel)
}
