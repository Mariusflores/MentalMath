package com.example.mentalmath.logic.models

import kotlin.time.Duration

data class ScoreCard (
    val score: Int,
    val problemCount : Int,
    val percentage: Double,
    val elapsedTime: Duration
)