package com.example.mentalmath.logic.models.core

data class MathProblem(
    val operandA: Int,
    val operandB: Int,
    val operator: Operator,
    val questionText: String,
    val correctAnswer: Int
)