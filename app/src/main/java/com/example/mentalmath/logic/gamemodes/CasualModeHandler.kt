package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.managers.QuizFactory
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.ProblemMode
import com.example.mentalmath.logic.models.quiz.TimerType
import kotlin.time.Duration

class CasualModeHandler: GameModeHandler {
    val quizFactory: QuizFactory = QuizFactory()
    private var quiz: List<MathProblem> = emptyList()
    private var index = 0

    override fun startGame(modeConfiguration: ModeConfiguration): List<MathProblem> {
        var index = 0
        return quizFactory.generateQuizByGameMode(modeConfiguration)
    }

    // For Semantics - Not to be used
    override fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem {
        return quiz[index++]
    }


    override fun timerType(): TimerType = TimerType.STOPWATCH
    override fun problemMode(): ProblemMode = ProblemMode.FINITE

    override fun timeLimit(): Duration? = null

    override fun onAnswerSubmitted(wasCorrect: Boolean) {
        index++
    }


    override fun shouldEndGame(): Boolean {
        return index > quiz.size - 1
    }


}