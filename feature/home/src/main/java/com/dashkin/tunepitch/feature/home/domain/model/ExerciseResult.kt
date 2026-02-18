package com.dashkin.tunepitch.feature.home.domain.model

data class ExerciseResult(
    val exerciseId: String,
    val bestScore: Int,
    val lastAttemptTimestamp: Long
)
