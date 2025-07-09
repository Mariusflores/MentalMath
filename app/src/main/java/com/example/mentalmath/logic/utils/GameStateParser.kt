package com.example.mentalmath.logic.utils

import com.example.mentalmath.logic.models.quiz.GameState
import kotlin.time.Duration

object GameStateParser {

    fun getIndexProperty(gameState: GameState): Int {
        return when (val state = gameState) {
            is GameState.Casual -> state.index
            is GameState.TimeAttack -> state.index
            is GameState.Survival -> state.total
            is GameState.Practice -> state.total
        }
    }

    fun getTotalProperty(gameState: GameState): Int {
        return when (val state = gameState) {
            is GameState.Casual -> state.total
            is GameState.TimeAttack -> state.total
            is GameState.Survival -> state.total
            is GameState.Practice -> state.total
        }
    }
    fun getCorrectProperty(gameState: GameState): Int{
        return when (val state = gameState) {
            is GameState.Casual -> state.correct
            is GameState.TimeAttack -> state.correct
            is GameState.Practice -> state.correct
            is GameState.Survival -> state.total - state.mistakes
        }
    }
    fun getIsFinishedProperty(gameState: GameState): Boolean{
        return when (val state = gameState) {
            is GameState.Casual -> state.isFinished
            is GameState.TimeAttack -> state.isFinished
            is GameState.Survival -> state.isFinished
            is GameState.Practice -> state.isFinished
        }
    }

    fun getTimeLeftProperty(gameState: GameState): Duration{
        return when (val state = gameState){
            is GameState.TimeAttack -> state.timeLeft!!
            else ->  Duration.ZERO
        }
    }

    fun getMistakesProperty(gameState: GameState): Int{
        return when (val state = gameState){
            is GameState.Survival -> state.mistakes
            else ->  0
        }
    }

    fun getLivesProperty(gameState: GameState): Int{
        return when (val state = gameState){
            is GameState.Survival -> state.lives
            else ->  0
        }
    }
    fun getStreakProperty(gameState: GameState): Int{
        return when (val state = gameState){
            is GameState.Practice -> state.streak
            else ->  0
        }
    }
    fun getMaxStreakProperty(gameState: GameState): Int{
        return when (val state = gameState){
            is GameState.Practice -> state.maxStreak
            else ->  0
        }
    }



}