package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.managers.QuizFactory
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.ProblemMode
import com.example.mentalmath.logic.models.quiz.TimerType
import kotlin.time.Duration

class SurvivalModeHandler: GameModeHandler {
    val quizFactory: QuizFactory = QuizFactory()

    private var mistakes = 0
    private val lives = 3
    override fun startGame(modeConfiguration: ModeConfiguration): List<MathProblem> {
        mistakes = 0;
        return quizFactory.generateQuizByGameMode(modeConfiguration)
    }

    override fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem {
        return quizFactory.generateProblemByGameMode(modeConfiguration)!!
    }

    override fun timerType(): TimerType = TimerType.NONE
    override fun problemMode(): ProblemMode = ProblemMode.INFINITE

    override fun timeLimit(): Duration? = null

    override fun onAnswerSubmitted(wasCorrect: Boolean) {
        if(!wasCorrect) mistakes++
    }

    override fun shouldEndGame(): Boolean {
        return mistakes == lives
    }
}