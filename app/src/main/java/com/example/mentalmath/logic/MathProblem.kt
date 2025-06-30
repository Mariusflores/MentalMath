package com.example.mentalmath.logic

data class MathProblem(
    val a: Int,
    val b: Int,
    val operator: Operator,
    val questionText: String,
    val correctAnswer: Int
)