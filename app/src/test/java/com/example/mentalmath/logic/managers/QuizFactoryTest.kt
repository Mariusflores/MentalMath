package com.example.mentalmath.logic.managers

import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.test.assertFailsWith

class QuizFactoryTest {

    val quizFactory: QuizFactory = QuizFactory()

    @Test
    fun generateQuizByGameMode_GenerateCasualModeQuiz (){

        val length = 10
        val allowedOperators = Operator.entries.toTypedArray()
        val modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, length,
            GameMode.Casual )


        val quiz = quizFactory.generateQuizByGameMode(modeConfig)
        assertEquals(length, quiz.size)
        assertTrue(quiz.all { it.operator in allowedOperators })

    }
    @Test
    fun generateQuizByGameMode_GameModeTimeAttackEasy (){

        val length = 10
        val allowedOperators = Operator.entries.toTypedArray()
        val modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, null,
            GameMode.TimeAttack )


        val quiz = quizFactory.generateQuizByGameMode(modeConfig)
        assertEquals(length, quiz.size)
    }
    @Test
    fun generateQuizByGameMode_GameModeTimeAttackMedium (){

        val length = 15
        val allowedOperators = Operator.entries.toTypedArray()
        val modeConfig = ModeConfiguration(Difficulty.MEDIUM, allowedOperators, null,
            GameMode.TimeAttack )


        val quiz = quizFactory.generateQuizByGameMode(modeConfig)
        assertEquals(length, quiz.size)
    }
    @Test
    fun generateQuizByGameMode_GameModeTimeAttackHard (){

        val length = 20
        val allowedOperators = Operator.entries.toTypedArray()
        val modeConfig = ModeConfiguration(Difficulty.HARD, allowedOperators, null,
            GameMode.TimeAttack )


        val quiz = quizFactory.generateQuizByGameMode(modeConfig)
        assertEquals(length, quiz.size)
    }
    @Test
    fun generateQuizByGameMode_GameModeSurvival (){

        val length = 10
        val allowedOperators = Operator.entries.toTypedArray()
        val modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, length,
            GameMode.Survival )


        val quiz = quizFactory.generateQuizByGameMode(modeConfig)
        assertTrue(quiz.isEmpty())

    }
    @Test
    fun generateQuizByGameMode_GameModePractice (){


        val length = 10
        val allowedOperators = Operator.entries.toTypedArray()
        val modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, length,
            GameMode.Practice )


        val quiz = quizFactory.generateQuizByGameMode(modeConfig)
        assertTrue(quiz.isEmpty())
    }
    @Test
    fun generateProblemByGameMode_Survival(){

        val allowedOperators = Operator.entries.toTypedArray()
        val modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, null,
            GameMode.Survival )

        val problem = quizFactory.generateProblemByGameMode(modeConfig)

        assertNotNull(problem)
    }
    @Test
    fun generateProblemByGameMode_Practice(){

        val allowedOperators = Operator.entries.toTypedArray()
        val modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, null,
            GameMode.Practice )

        val problem = quizFactory.generateProblemByGameMode(modeConfig)

        assertNotNull(problem)
    }
    @Test
    fun generateProblemByGameMode_CasualError(){

        val allowedOperators = Operator.entries.toTypedArray()
        val modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, null,
            GameMode.Casual )
        val errorMessage = "generateProblemByGameMode called with unsupported game mode: ${modeConfig.gameMode}"


        val exception = assertFailsWith<IllegalStateException>{
            quizFactory.generateProblemByGameMode(modeConfig)
        }

        assertEquals(errorMessage, exception.message)
    }
    @Test
    fun generateProblemByGameMode_TimeAttackError(){

        val allowedOperators = Operator.entries.toTypedArray()
        val modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, null,
            GameMode.TimeAttack )
        val errorMessage = "generateProblemByGameMode called with unsupported game mode: ${modeConfig.gameMode}"


        val exception = assertFailsWith<IllegalStateException>{
            quizFactory.generateProblemByGameMode(modeConfig)
        }

        assertEquals(errorMessage, exception.message)
    }

    @Test
    fun generateQuizByGameMode_OperatorSelectedCheck (){

        val length = 10
        val operatorList = listOf("×", "÷")

        val allowedOperators = Operator.OperatorConverter.toOperatorArray(operatorList)
        val modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, length,
            GameMode.Casual )

        val quiz = quizFactory.generateQuizByGameMode(modeConfig)
        assertTrue(quiz.all { it.operator in allowedOperators })

    }
    @Test
    fun generateProblemByGameMode_OperatorSelectedCheck (){
        val operatorList = listOf("×", "÷")

        val allowedOperators = Operator.OperatorConverter.toOperatorArray(operatorList)
        val modeConfig = ModeConfiguration(Difficulty.EASY, allowedOperators, null,
            GameMode.Survival )


        repeat(50){
            val problem = quizFactory.generateProblemByGameMode(modeConfig)
            assertTrue(problem!!.operator in allowedOperators)
        }


    }
}