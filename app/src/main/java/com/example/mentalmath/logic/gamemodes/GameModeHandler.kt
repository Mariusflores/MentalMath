package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.quiz.TimerType
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import kotlin.time.Duration

interface GameModeHandler {
    fun startGame(modeConfiguration: ModeConfiguration): List<MathProblem>
    fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem
    fun timerType(): TimerType
    fun timeLimit(): Duration?
    fun onAnswerSubmitted()
    fun shouldEndGame(): Boolean

}