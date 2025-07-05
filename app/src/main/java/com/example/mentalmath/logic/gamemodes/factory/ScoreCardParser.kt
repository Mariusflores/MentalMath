package com.example.mentalmath.logic.gamemodes.factory

import com.example.mentalmath.logic.managers.QuizProgressionManager
import com.example.mentalmath.logic.models.quiz.ScoreCard
import kotlin.time.Duration

object ScoreCardParser {

    fun getCorrectProperty(scoreCard: ScoreCard): Int {
        return when(val card = scoreCard){
            is ScoreCard.Casual -> card.correct
            is ScoreCard.TimeAttack -> card.correct
            is ScoreCard.Survival -> card.total - card.lives
            is ScoreCard.Practice -> card.correct
        }
    }
    fun getTotalProperty(scoreCard: ScoreCard): Int {
        return when(val card = scoreCard){
            is ScoreCard.Casual -> card.total
            is ScoreCard.TimeAttack -> card.total
            is ScoreCard.Survival -> card.total
            is ScoreCard.Practice -> card.total
        }
    }
    fun getAccuracyProperty (scoreCard: ScoreCard): Double{
        return when(val card = scoreCard){
            is ScoreCard.Casual -> card.accuracy
            is ScoreCard.TimeAttack -> card.accuracy
            is ScoreCard.Survival -> card.accuracy
            is ScoreCard.Practice -> card.accuracy
        }
    }

    fun getTimeElapsedOrRemaining(scoreCard: ScoreCard): Duration{
        return when(val card = scoreCard){
            is ScoreCard.Casual -> card.time
            is ScoreCard.TimeAttack -> card.timeLeft
            else -> error("Game mode does not use timer")
        }
    }
    fun getMistakes(scoreCard: ScoreCard): Int{
        return when(val card = scoreCard){
            is ScoreCard.Survival -> card.mistakes
            else -> error("Game mode does not track mistakes")
        }
    }

    fun getLives(scoreCard: ScoreCard): Int{
        return when(val card = scoreCard){
            is ScoreCard.Survival -> card.lives
            else -> error("Game mode does not support lives")
        }
    }

    fun getStreak(scoreCard: ScoreCard): Int{
        return when(val card = scoreCard){
            is ScoreCard.Practice -> card.streak
            else -> error("Game mode does not support lives")
        }
    }
}