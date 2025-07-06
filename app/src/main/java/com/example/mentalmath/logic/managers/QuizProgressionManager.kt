package com.example.mentalmath.logic.managers

import com.example.mentalmath.logic.models.quiz.GameState
import com.example.mentalmath.logic.models.quiz.ScoreCard
import kotlin.time.Duration

private const val INVALID_INPUT = "Please enter a valid number."

class QuizProgressionManager {
    fun getScoreCardFromGameState(gameState: GameState, time: Duration? = null): ScoreCard{
        return when (gameState){

            is GameState.Casual -> {
                val safeElapsed = time ?: Duration.ZERO
                generateCasualScoreCard(gameState, safeElapsed)
            }
            is GameState.Practice -> {
                generatePracticeScoreCard(gameState)
            }
            is GameState.Survival -> {
                generateSurvivalScoreCard(gameState)
            }
            is GameState.TimeAttack -> {
                val safeTimeLeft = time ?: Duration.ZERO
                generateTimeAttackScoreCard(gameState, safeTimeLeft)

            }
        }
    }
    fun resetAnswer(): String{
        return ""
    }
    fun returnInvalidInputString(): String{
        return INVALID_INPUT
    }
    fun checkAnswer(inputAnswer: Int, answer: Int ): Boolean{

        return inputAnswer == answer
    }

    private fun generateSurvivalScoreCard(gameState: GameState.Survival): ScoreCard {
        val correct = gameState.total - gameState.mistakes
        val accuracy = calculateAccuracy(correct, gameState.total)
        return ScoreCard.Survival(gameState.mistakes, gameState.lives, gameState.total, accuracy)
    }

    private fun generateTimeAttackScoreCard(gameState: GameState.TimeAttack, timeLeft: Duration): ScoreCard {
        val accuracy = calculateAccuracy(gameState.correct, gameState.total)
        return ScoreCard.TimeAttack(gameState.correct, gameState.total,accuracy, timeLeft )
    }

    private fun generatePracticeScoreCard(gameState: GameState.Practice): ScoreCard {
        val accuracy = calculateAccuracy(gameState.correct, gameState.total)
        return ScoreCard.Practice(gameState.correct, gameState.total, gameState.maxStreak, accuracy)
    }

    private fun generateCasualScoreCard(
        gameState: GameState.Casual,
        elapsedTime: Duration
    ): ScoreCard {
        val accuracy = calculateAccuracy(gameState.correct, gameState.total)
        return ScoreCard.Casual(gameState.correct, gameState.total, accuracy, elapsedTime)
    }

    private fun calculateAccuracy(correct: Int, total: Int): Double {
        if (total == 0) return 0.0
        return  (correct.toDouble() / total) * 100
    }
}