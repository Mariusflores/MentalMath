package com.example.mentalmath.logic.generators

import com.example.mentalmath.logic.models.Difficulty
import com.example.mentalmath.logic.models.MathProblem

object MathQuizGenerator {

    fun generateRandomOperatorQuiz(quizDifficulty: Difficulty, length: Int): List<MathProblem> {
        val quiz: MutableList<MathProblem> = mutableListOf()
        for (i in 1..length) {
            quiz.add(MathProblemGenerator.generateRandomProblem(quizDifficulty))
        }

        return quiz.toList()
    }
}