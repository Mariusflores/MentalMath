package com.example.mentalmath.logic.managers

import com.example.mentalmath.logic.generators.MathQuizGenerator
import com.example.mentalmath.logic.models.Difficulty
import com.example.mentalmath.logic.models.MathProblem
import com.example.mentalmath.logic.models.Operator
import com.example.mentalmath.logic.models.QuizState
import com.example.mentalmath.logic.models.ScoreCard
import kotlin.time.Duration

class QuizManager {

    fun checkAnswer(inputAnswer: Int, answer: Int ): Boolean{

        return inputAnswer == answer
    }

    fun verifyQuizFinished(quizState: QuizState): Boolean {
        return quizState.quizFinished
    }

    fun getScoreCard(score: Int, problemCount : Int, elapsedTime: Duration): ScoreCard{
        val percentage: Double = calculatePercentage(score, problemCount)
        return ScoreCard(score, problemCount,percentage ,elapsedTime)
    }

    private fun calculatePercentage(score: Int, problemCount: Int): Double {
        return  (score.toDouble() / problemCount) * 100
    }

    fun progressQuiz(quizIndex: Int, quizLength: Int): QuizState{
        var index = quizIndex
        var quizFinished = false

        if(index < quizLength - 1){
            index++
        }else {
            quizFinished = true
        }

        return QuizState(index, quizFinished)
    }

    fun endQuiz(quizIndex: Int) : QuizState{

        return QuizState(quizIndex, true)
    }

    fun resetAnswer(): String{
        return ""
    }

    fun getQuizByDifficulty(
        difficulty: String,
        operatorsStringList: List<String>,
        quizLength: String): List<MathProblem> {
        val quizDifficulty: Difficulty = Difficulty.DifficultyConverter.toDifficulty(difficulty.lowercase())
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorsStringList)
        val length: Int = quizLength.toInt()

        return MathQuizGenerator.generateRandomOperatorQuiz(quizDifficulty, operatorArray, length)
    }

}