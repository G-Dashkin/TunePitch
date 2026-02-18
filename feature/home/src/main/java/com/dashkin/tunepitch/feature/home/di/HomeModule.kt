package com.dashkin.tunepitch.feature.home.di

import com.dashkin.tunepitch.feature.home.data.repository.ExerciseRepositoryImpl
import com.dashkin.tunepitch.feature.home.domain.repository.ExerciseRepository
import com.dashkin.tunepitch.feature.home.domain.usecase.GetBestResultsUseCase
import com.dashkin.tunepitch.feature.home.domain.usecase.GetExercisesUseCase
import com.dashkin.tunepitch.feature.home.presentation.viewmodel.HomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    singleOf(::ExerciseRepositoryImpl) bind ExerciseRepository::class
    singleOf(::GetExercisesUseCase)
    singleOf(::GetBestResultsUseCase)
    viewModelOf(::HomeViewModel)
}
