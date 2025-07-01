package com.example.mentalmath.logic.generators

import com.example.mentalmath.logic.models.MathProblem

object MathQuizGenerator {

    fun generateRandomOperatorQuiz(): List<MathProblem>{
        val quiz: MutableList<MathProblem> = mutableListOf()
        for(i in 1..10){
            quiz.add(MathProblemGenerator.generateRandomProblem())
        }

        return quiz.toList()
    }
}