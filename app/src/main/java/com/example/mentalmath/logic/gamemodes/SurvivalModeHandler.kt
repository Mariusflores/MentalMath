package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.managers.QuizFactory
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.GameState
import com.example.mentalmath.logic.models.quiz.ProblemMode
import com.example.mentalmath.logic.models.quiz.TimerType
import kotlin.time.Duration

class SurvivalModeHandler: GameModeHandler {
    private val quizFactory: QuizFactory = QuizFactory()

    private var mistakes = 0
    private val lives = lives()
    private var total = 0
    private var isFinished = false

    override fun startGame(modeConfiguration: ModeConfiguration): List<MathProblem> {
        mistakes = 0; total = 0; isFinished = false
        return quizFactory.generateQuizByGameMode(modeConfiguration)
    }

    override fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem {
        return quizFactory.generateProblemByGameMode(modeConfiguration)!!
    }

    override fun timerType(): TimerType = TimerType.NONE
    override fun problemMode(): ProblemMode = ProblemMode.INFINITE

    private fun lives(): Int = 3

    override fun timeLimit(): Duration? = null

    override fun onAnswerSubmitted(wasCorrect: Boolean) {
        if(!wasCorrect) mistakes++
        total++
    }
    private fun shouldEndGame(): Boolean = mistakes == lives

    override fun getGameState(timeLeft: Duration?): GameState {
        if(!isFinished) isFinished = shouldEndGame()

        return GameState.Survival(mistakes, lives, total, isFinished)
    }

    override fun endGameEarly() {
        isFinished = true
    }
}