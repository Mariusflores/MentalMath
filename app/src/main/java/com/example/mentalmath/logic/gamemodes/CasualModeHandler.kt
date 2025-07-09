package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.managers.QuizFactory
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.GameState
import com.example.mentalmath.logic.models.quiz.ProblemMode
import com.example.mentalmath.logic.models.quiz.TimerType
import kotlin.time.Duration

class CasualModeHandler: GameModeHandler {
    private val quizFactory: QuizFactory = QuizFactory()
    private var quiz: List<MathProblem> = emptyList()
    private var index = 0
    private var score = 0
    private var isFinished = false

    val getIndex get() = index

    override fun startGame(modeConfiguration: ModeConfiguration) {
        index = 0; score = 0; isFinished = false
        quiz = quizFactory.generateQuizByGameMode(modeConfiguration)
    }

    override fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem {
        return quiz[index]
    }

    override fun timerType(): TimerType = TimerType.STOPWATCH
    override fun problemMode(): ProblemMode = ProblemMode.FINITE

    override fun timeLimit(): Duration? = null

    override fun onAnswerSubmitted(wasCorrect: Boolean) {
        if(wasCorrect) score++
        index++
    }
    private fun shouldEndGame(): Boolean = index > quiz.size - 1

    override fun getGameState(timeLeft: Duration?): GameState {
        if(!isFinished) isFinished = shouldEndGame()
        return GameState.Casual(index, quiz.size, score, isFinished)
    }

    override fun endGameEarly() {
        isFinished = true
    }

}