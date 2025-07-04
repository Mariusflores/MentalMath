package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.managers.QuizFactory
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.quiz.TimerType
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TimeAttackModeHandler: GameModeHandler {
    var quiz: List<MathProblem> = emptyList()
    var index = 0
    val quizFactory: QuizFactory = QuizFactory()
    override fun startGame(modeConfiguration: ModeConfiguration): List<MathProblem> {
        quiz =  quizFactory.generateQuizByGameMode(modeConfiguration)
        return quiz
    }

    // For Semantics - Not to be used
    override fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem {
        return quiz[index++]
    }

    override fun timerType(): TimerType = TimerType.COUNTDOWN

    override fun timeLimit(): Duration? = 60.seconds

    override fun onAnswerSubmitted() {
        TODO("Not yet implemented")
    }

    override fun shouldEndGame(): Boolean {
        TODO("Not yet implemented")
    }
}