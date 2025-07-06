package com.example.mentalmath.logic.managers

import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class QuizFactoryTest {

    val quizFactory: QuizFactory = QuizFactory()

    @Test
    fun generateQuizByGameMode_GameModeCasual (){

        val length = 10
        var allowedOperators = Operator.entries.toTypedArray()
        var modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, length,
            GameMode.Casual )


        var quiz = quizFactory.generateQuizByGameMode(modeConfig)
        assertEquals(quiz.size, length)
        assertTrue(quiz.all { it.operator in allowedOperators })

        val operatorList = listOf("×", "÷")

        allowedOperators = Operator.OperatorConverter.toOperatorArray(operatorList)
        modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, length,
            GameMode.Casual )
        quiz = quizFactory.generateQuizByGameMode(modeConfig)
        assertTrue(quiz.all { it.operator in allowedOperators })

    }
}