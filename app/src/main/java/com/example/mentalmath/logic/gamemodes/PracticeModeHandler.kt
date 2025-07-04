package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.managers.QuizFactory
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.quiz.TimerType
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.ProblemMode
import kotlin.time.Duration

class PracticeModeHandler: GameModeHandler {
    var total = 0
    var correct = 0
    val quizFactory: QuizFactory = QuizFactory()
    override fun startGame(modeConfiguration: ModeConfiguration): List<MathProblem> {
        total = 0; correct = 0
        return quizFactory.generateQuizByGameMode(modeConfiguration)
    }

    override fun getNextProblem(modeConfiguration: ModeConfiguration): MathProblem {
        return quizFactory.generateProblemByGameMode(modeConfiguration )!!
    }

    override fun timerType(): TimerType = TimerType.NONE
    override fun problemMode(): ProblemMode = ProblemMode.INFINITE

    override fun timeLimit(): Duration? = null

    override fun onAnswerSubmitted(wasCorrect: Boolean) {
        if(wasCorrect) correct++
        total++
    }

    override fun shouldEndGame(): Boolean {
        TODO("Not yet implemented")
    }

}
