package com.dashkin.tunepitch.feature.home.data.datasource

import com.dashkin.tunepitch.feature.home.domain.model.Difficulty
import com.dashkin.tunepitch.feature.home.domain.model.Exercise

object ExerciseDataSource {

    fun getAll(): List<Exercise> = listOf(
        Exercise(
            id = "single_notes",
            title = "Single Notes",
            description = "Hold a single note steady. Perfect for beginners learning pitch control.",
            difficulty = Difficulty.BEGINNER,
            noteCount = 4
        ),
        Exercise(
            id = "scale_up",
            title = "Scale Up",
            description = "Sing an ascending C major scale from C4 to C5.",
            difficulty = Difficulty.INTERMEDIATE,
            noteCount = 8
        ),
        Exercise(
            id = "scale_down",
            title = "Scale Down",
            description = "Sing a descending C major scale from C5 to C4.",
            difficulty = Difficulty.INTERMEDIATE,
            noteCount = 8
        ),
        Exercise(
            id = "intervals",
            title = "Intervals",
            description = "Practice singing intervals: thirds, fifths, and octaves.",
            difficulty = Difficulty.ADVANCED,
            noteCount = 6
        ),
        Exercise(
            id = "melody",
            title = "Melody",
            description = "Sing a simple 8-note melody. Test your overall pitch accuracy.",
            difficulty = Difficulty.ADVANCED,
            noteCount = 8
        )
    )
}
