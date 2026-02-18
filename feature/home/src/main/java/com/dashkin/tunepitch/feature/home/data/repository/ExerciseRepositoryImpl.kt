package com.dashkin.tunepitch.feature.home.data.repository

import com.dashkin.tunepitch.feature.home.data.datasource.ExerciseDataSource
import com.dashkin.tunepitch.feature.home.domain.model.Exercise
import com.dashkin.tunepitch.feature.home.domain.model.ExerciseResult
import com.dashkin.tunepitch.feature.home.domain.repository.ExerciseRepository

class ExerciseRepositoryImpl : ExerciseRepository {

    override suspend fun getExercises(): List<Exercise> {
        return ExerciseDataSource.getAll()
    }

    override suspend fun getBestResults(): Map<String, ExerciseResult> {
        // Will be backed by DataStore in the future
        return emptyMap()
    }
}
