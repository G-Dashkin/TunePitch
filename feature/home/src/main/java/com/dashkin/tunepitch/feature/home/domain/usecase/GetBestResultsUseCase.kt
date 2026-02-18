package com.dashkin.tunepitch.feature.home.domain.usecase

import com.dashkin.tunepitch.feature.home.domain.model.ExerciseResult
import com.dashkin.tunepitch.feature.home.domain.repository.ExerciseRepository

class GetBestResultsUseCase(
    private val repository: ExerciseRepository
) {

    suspend operator fun invoke(): Map<String, ExerciseResult> {
        return repository.getBestResults()
    }
}
