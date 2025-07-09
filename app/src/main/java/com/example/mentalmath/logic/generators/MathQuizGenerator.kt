package com.example.mentalmath.logic.generators

import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.core.Operator

object MathQuizGenerator {

    fun generateRandomOperatorQuiz(
        quizDifficulty: Difficulty,
        operators: Array<Operator>,
        length: Int): List<MathProblem> {
        val quiz: MutableList<MathProblem> = mutableListOf()
        for (i in 1..length) {
            quiz.add(MathProblemGenerator.generateRandomProblem(quizDifficulty, operators))
        }

        return quiz.toList()
    }
}