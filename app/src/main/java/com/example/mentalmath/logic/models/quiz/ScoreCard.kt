package com.example.mentalmath.logic.models.quiz

import kotlin.time.Duration

sealed class ScoreCard {
    data class Casual(
        val correct: Int,
        val total: Int,
        val accuracy: Double,
        val time : Duration
    ): ScoreCard()
    data class TimeAttack(
        val correct: Int,
        val total: Int,
        val accuracy: Double,
        val timeLeft : Duration
    ): ScoreCard()
    data class Survival(
        val mistakes: Int,
        val lives: Int,
        val total: Int
    ): ScoreCard()
    data class Practice(
        val correct: Int,
        val total: Int,
        val streak: Int,
        val accuracy: Double
    ): ScoreCard()
}