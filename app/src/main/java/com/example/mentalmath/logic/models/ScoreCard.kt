package com.example.mentalmath.logic.models

import kotlin.time.Duration

data class ScoreCard (
    val score: Int,
    val quizLength : Int,
    val percentage: Double,
    val elapsedTime: Duration
)