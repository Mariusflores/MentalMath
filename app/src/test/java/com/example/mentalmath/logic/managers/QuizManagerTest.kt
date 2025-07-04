package com.example.mentalmath.logic.managers

import com.example.mentalmath.logic.generators.MathProblemGenerator
import com.example.mentalmath.logic.models.Difficulty
import com.example.mentalmath.logic.models.Operator
import com.example.mentalmath.logic.models.QuizState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.random.Random
import kotlin.time.Duration

class QuizManagerTest {
    private val quizManager: QuizManager = QuizManager()

    @Test
    fun checkAnswer_returnsTrueForCorrectAnswer(){
        val mathProblem = MathProblemGenerator.generateRandomProblem(Difficulty.EASY, arrayOf(Operator.ADD))
        val inputAnswer = mathProblem.a + mathProblem.b
        val wrongAnswer = mathProblem.correctAnswer + 1

        assertTrue(quizManager.checkAnswer(inputAnswer, mathProblem.correctAnswer))
        assertFalse(quizManager.checkAnswer(wrongAnswer, mathProblem.correctAnswer))

    }

    @Test
    fun verifyQuizFinished_returnsTrueIfQuizFinished(){
        val finishedQuizState = QuizState(4, true)
        val unfinishedQuizState = QuizState(4, false)

        assertTrue(quizManager.verifyQuizFinished(finishedQuizState))
        assertTrue(!quizManager.verifyQuizFinished(unfinishedQuizState))

    }

    @Test
    fun getScoreCard_returnsScoreValues(){
        val score = Random.nextInt(1, 20)
        val problemCount = Random.nextInt(score, 25)
        val percentage = (score.toDouble() / problemCount) * 100
        val elapsedTime = Duration.ZERO

        val scoreCard = quizManager.getScoreCard(score, problemCount, elapsedTime)

        assertEquals(scoreCard.score, score)
        assertEquals(scoreCard.quizLength, problemCount)
        assertEquals(scoreCard.elapsedTime, elapsedTime)
        assertEquals(scoreCard.percentage, (percentage))
    }

    @Test
    fun progressQuiz_incrementsQuizIndexOrSetsQuizFinished(){
        var index = 5
        var problemCount = 8

        var quizState = quizManager.progressQuiz(index, problemCount)
        assertEquals(quizState.quizIndex, index + 1)
        assertTrue(!quizState.quizFinished)


        index = 10
        problemCount = 10
        quizState = quizManager.progressQuiz(index, problemCount)

        assertEquals(quizState.quizIndex, index)
        assertTrue(quizState.quizFinished)
    }

    @Test
    fun endQuiz_returnsQuizStateWithQuizFinishedTrue(){
        val quizState = quizManager.endQuiz(4)

        assertTrue(quizState.quizFinished)
    }

    @Test
    fun resetAnswer_returnsEmptyString(){
        assertEquals(quizManager.resetAnswer(), "")
    }

    @Test
    fun getQuizByDifficulty_convertsStringsAndReturnsQuiz(){
        var operatorList: List<String> = listOf("+", "-", "×", "÷")
        val difficulty = "Easy"
        val length = "7"
        var allowedOperators = Operator.OperatorConverter.toOperatorArray(operatorList)

        var quiz = quizManager.getQuizByDifficulty(difficulty, operatorList, length)
        assertEquals(quiz.size, length.toInt())
        assertTrue(quiz.all { it.operator in allowedOperators })

        operatorList = listOf("×", "÷")
        allowedOperators = Operator.OperatorConverter.toOperatorArray(operatorList)
        quiz = quizManager.getQuizByDifficulty(difficulty, operatorList, length)
        assertTrue(quiz.all { it.operator in allowedOperators })



    }
}