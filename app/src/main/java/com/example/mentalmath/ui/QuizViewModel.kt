package com.example.mentalmath.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mentalmath.logic.MathQuizGenerator


class QuizViewModel : ViewModel() {

    var quiz by mutableStateOf( MathQuizGenerator.generateRandomOperatorQuiz())
    var quizIndex = 0
    var problem by mutableStateOf( quiz[quizIndex])

    var score by mutableStateOf(0)
    var lastAnswerCorrect by mutableStateOf<Boolean?>(null)

    var isQuizFinished by mutableStateOf(false)

    var answer by mutableStateOf("")
    var feedback by mutableStateOf("")


    fun onSubmitClick() {
        val userAnswer = answer.toIntOrNull()
        when (userAnswer) {
            null -> {
                feedback = "Please enter a valid number."
            }
            problem.correctAnswer -> {
                score++
                lastAnswerCorrect = true
            }
            else -> {
                lastAnswerCorrect = false
            }
        }
        advanceQuiz()
    }
    fun onEndClick() {
        isQuizFinished = true
    }

    private fun advanceQuiz(){
        if(quizIndex < quiz.size - 1){
            quizIndex ++
            problem =  quiz[quizIndex]
            answer = ""
        }else{
            isQuizFinished = true
        }
    }

    fun resetQuiz(){
        quiz = MathQuizGenerator.generateRandomOperatorQuiz()
        quizIndex = 0
        problem = quiz[quizIndex]
        score = 0
        isQuizFinished = false
        answer = ""
        feedback = ""

    }


}