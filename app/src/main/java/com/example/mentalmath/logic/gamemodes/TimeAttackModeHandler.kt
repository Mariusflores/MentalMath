package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.managers.QuizFactory
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.quiz.TimerType
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.GameState
import com.example.mentalmath.logic.models.quiz.ProblemMode
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TimeAttackModeHandler: GameModeHandler {
    var quiz: List<MathProblem> = emptyList()
    var index = 0
    var score = 0
    var isFinished = false
    val quizFactory: QuizFactory = QuizFactory()
    override fun startGame(modeConfiguration: ModeConfiguration): List<MathProblem> {
        index = 0
        quiz =  quizFactory.generateQuizByGameMode(modeConfiguration)
        return quiz
    }

    // For Semantics - Not to be used
    override fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem {
        return quiz[index++]
    }

    override fun timerType(): TimerType = TimerType.COUNTDOWN
    override fun problemMode(): ProblemMode = ProblemMode.FINITE


    override fun timeLimit(): Duration? = 60.seconds

    override fun onAnswerSubmitted(wasCorrect: Boolean) {
        if(wasCorrect) score++
        index++
    }

    private fun shouldEndGame(): Boolean =  index > quiz.size - 1


    override fun getGameState(timeLeft: Duration?): GameState {
        if(!isFinished) isFinished = shouldEndGame()
        return GameState.TimeAttack(index, quiz.size, score, isFinished, timeLeft)
    }

    override fun endGameEarly() {
        isFinished = true
    }
}