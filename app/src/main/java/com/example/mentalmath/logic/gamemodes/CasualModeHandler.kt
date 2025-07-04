package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.managers.QuizFactory
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.TimerType
import kotlin.time.Duration

private var quiz: List<MathProblem> = emptyList()
private var index = 0

class CasualModeHandler: GameModeHandler {
    val quizFactory: QuizFactory = QuizFactory()
    override fun startGame(modeConfiguration: ModeConfiguration): List<MathProblem> {
        return quizFactory.generateQuizByGameMode(modeConfiguration)
    }

    // For Semantics - Not to be used
    override fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem {
        return quiz[index++]
    }


    override fun timerType(): TimerType = TimerType.STOPWATCH

    override fun timeLimit(): Duration? = null

    override fun onAnswerSubmitted() {
        TODO("Not yet implemented")
    }

    override fun shouldEndGame(): Boolean {
        TODO("Not yet implemented")
    }


}