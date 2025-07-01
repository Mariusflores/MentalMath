package com.example.mentalmath.logic.managers

import com.example.mentalmath.logic.models.QuizState

class QuizManager {

    fun checkAnswer(inputAnswer: Int, answer: Int ): Boolean{

        return inputAnswer == answer
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
}