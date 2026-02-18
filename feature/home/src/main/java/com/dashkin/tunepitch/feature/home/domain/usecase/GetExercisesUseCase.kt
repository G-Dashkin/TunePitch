package com.dashkin.tunepitch.feature.home.domain.usecase

import com.dashkin.tunepitch.feature.home.domain.model.Exercise
import com.dashkin.tunepitch.feature.home.domain.repository.ExerciseRepository

class GetExercisesUseCase(
    private val repository: ExerciseRepository
) {

    suspend operator fun invoke(): List<Exercise> {
        return repository.getExercises()
    }
}
