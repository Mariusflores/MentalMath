package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.quiz.TimerType
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.GameState
import com.example.mentalmath.logic.models.quiz.ProblemMode
import kotlin.time.Duration

interface GameModeHandler {
    fun startGame(modeConfiguration: ModeConfiguration): List<MathProblem>
    fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem
    fun timerType(): TimerType

    fun problemMode(): ProblemMode
    fun timeLimit(): Duration?
    fun onAnswerSubmitted(wasCorrect: Boolean)

    fun getGameState(timeLeft: Duration? = null): GameState

    fun endGameEarly()

}