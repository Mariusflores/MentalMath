package com.example.mentalmath.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mentalmath.logic.MathProblemGenerator


class QuizViewModel : ViewModel() {

    var problem by  mutableStateOf(MathProblemGenerator.generateAdditionProblem())
    var answer by mutableStateOf("")
    var feedback by mutableStateOf("")
    var correctAnswer by mutableStateOf(false)

    fun onSubmitClick () {
        val userAnswer = answer.toIntOrNull()
        when (userAnswer) {
            null -> {
                feedback = "Please enter a valid number."
                correctAnswer = false
            }
            problem.correctAnswer -> {
                feedback = "Correct!"
                correctAnswer = true
            }
            else -> {
                feedback = "Wrong, please try again!"
                correctAnswer = false
            }
        }
    }
    fun onNextProblemClick() {
        problem = MathProblemGenerator.generateAdditionProblem()
        feedback = ""
        correctAnswer = false
        answer = ""
    }


}