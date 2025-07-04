package com.example.mentalmath.logic.models.quiz

data class QuizState (
    val quizIndex: Int,
    val quizFinished: Boolean,
    val quizLength: Int? = null
)