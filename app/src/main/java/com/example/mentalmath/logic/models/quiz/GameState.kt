package com.example.mentalmath.logic.models.quiz

import kotlin.time.Duration

sealed class GameState(

) {
    data class Casual (
        val index: Int,
        val total: Int,
        val correct: Int,
        val isFinished: Boolean
    ): GameState()

    data class TimeAttack (
        val index: Int,
        val total: Int,
        val correct: Int,
        val isFinished: Boolean,
        val timeLeft: Duration? = null
    ): GameState()

    data class Survival (
        val mistakes: Int,
        val lives: Int,
        val total: Int,
        val isFinished: Boolean
    ): GameState()

    data class Practice (
        val correct: Int,
        val total: Int,
        val streak: Int,
        val maxStreak: Int,
        val isFinished: Boolean
    ): GameState()

}