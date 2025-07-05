package com.example.mentalmath.logic.gamemodes.factory

import com.example.mentalmath.logic.managers.QuizProgressionManager
import com.example.mentalmath.logic.models.quiz.GameState
import com.example.mentalmath.logic.models.quiz.ScoreCard
import kotlin.time.Duration

object ScoreCardFactory {
    val quizProgressionManager = QuizProgressionManager()
    fun create(gameState: GameState, elapsedOrRemainingTime: Duration): ScoreCard {
        val scoreCard = quizProgressionManager.getScoreCardFromGameState(gameState, elapsedOrRemainingTime)



    }
}