package com.dashkin.tunepitch.feature.home.domain.model

data class Exercise(
    val id: String,
    val title: String,
    val description: String,
    val difficulty: Difficulty,
    val noteCount: Int
)
