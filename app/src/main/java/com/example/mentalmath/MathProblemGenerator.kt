package com.example.mentalmath

import kotlin.random.Random

object MathProblemGenerator {
    fun generateAdditionProblem(): MathProblem{
        val a = Random.nextInt(1, 100)
        val b = Random.nextInt(1, 100)

        return MathProblem(
            question = "$a + $b",
            correctAnswer = (a + b)
        )
    }
}