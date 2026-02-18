package com.dashkin.tunepitch.feature.home.domain.repository

import com.dashkin.tunepitch.feature.home.domain.model.Exercise
import com.dashkin.tunepitch.feature.home.domain.model.ExerciseResult

interface ExerciseRepository {

    suspend fun getExercises(): List<Exercise>

    suspend fun getBestResults(): Map<String, ExerciseResult>
}
