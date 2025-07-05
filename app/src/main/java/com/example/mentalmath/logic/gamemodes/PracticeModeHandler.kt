package com.example.mentalmath.logic.gamemodes

import com.example.mentalmath.logic.managers.QuizFactory
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.quiz.TimerType
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.GameState
import com.example.mentalmath.logic.models.quiz.ProblemMode
import kotlin.time.Duration

class PracticeModeHandler: GameModeHandler {
    var total = 0
    var correct = 0
    var isFinished = false

    var currentStreak = 0
    var highestStreak = 0

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
        if(wasCorrect) {
            correct++; currentStreak++
        }else{
            if(currentStreak > highestStreak) highestStreak = currentStreak
        }
        total++
    }
    private fun shouldEndGame(): Boolean =  false


    override fun getGameState(timeLeft: Duration?): GameState {
        if(!isFinished) isFinished = shouldEndGame()
        return GameState.Practice(correct, total, currentStreak,highestStreak,isFinished)
    }

    override fun endGameEarly() {
        isFinished = true
    }

}
