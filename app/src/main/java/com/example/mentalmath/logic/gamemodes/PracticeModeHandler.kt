package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.managers.QuizFactory
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.quiz.TimerType
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import kotlin.time.Duration

class PracticeModeHandler: GameModeHandler {
    val quizFactory: QuizFactory = QuizFactory()
    override fun startGame(modeConfiguration: ModeConfiguration): List<MathProblem> {
        return quizFactory.generateQuizByGameMode(modeConfiguration)
    }

    override fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem {
        return quizFactory.generateProblemByGameMode(modeConfiguration )!!
    }

    override fun timerType(): TimerType = TimerType.NONE

    override fun timeLimit(): Duration? = null

    override fun onAnswerSubmitted() {
        TODO("Not yet implemented")
    }

    override fun shouldEndGame(): Boolean {
        TODO("Not yet implemented")
    }

}
