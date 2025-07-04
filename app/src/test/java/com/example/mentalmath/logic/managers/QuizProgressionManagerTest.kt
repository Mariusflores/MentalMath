package com.example.mentalmath.logic.managers

import com.example.mentalmath.logic.generators.MathProblemGenerator
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.quiz.QuizState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.random.Random
import kotlin.time.Duration

class QuizProgressionManagerTest {
    private val quizProgressionManager: QuizProgressionManager = QuizProgressionManager()

    @Test
    fun checkAnswer_returnsTrueForCorrectAnswer(){
        val mathProblem = MathProblemGenerator.generateRandomProblem(Difficulty.EASY, arrayOf(Operator.ADD))
        val inputAnswer = mathProblem.operandA + mathProblem.operandB
        val wrongAnswer = mathProblem.correctAnswer + 1

        assertTrue(quizProgressionManager.checkAnswer(inputAnswer, mathProblem.correctAnswer))
        assertFalse(quizProgressionManager.checkAnswer(wrongAnswer, mathProblem.correctAnswer))

    }

    @Test
    fun verifyQuizFinished_returnsTrueIfQuizFinished(){
        val finishedQuizState = QuizState(4, true)
        val unfinishedQuizState = QuizState(4, false)

        assertTrue(quizProgressionManager.verifyQuizFinished(finishedQuizState))
        assertTrue(!quizProgressionManager.verifyQuizFinished(unfinishedQuizState))

    }

    @Test
    fun getScoreCard_returnsScoreValues(){
        val score = Random.nextInt(1, 20)
        val problemCount = Random.nextInt(score, 25)
        val percentage = (score.toDouble() / problemCount) * 100
        val elapsedTime = Duration.ZERO

        val scoreCard = quizProgressionManager.getScoreCard(score, problemCount, elapsedTime)

        assertEquals(scoreCard.score, score)
        assertEquals(scoreCard.quizLength, problemCount)
        assertEquals(scoreCard.elapsedTime, elapsedTime)
        assertEquals(scoreCard.percentage, (percentage))
    }

    @Test
    fun progressQuiz_incrementsQuizIndexOrSetsQuizFinished(){
        var index = 5
        var problemCount = 8

        var quizState = quizProgressionManager.progressQuiz(index, problemCount)
        assertEquals(quizState.quizIndex, index + 1)
        assertTrue(!quizState.quizFinished)


        index = 10
        problemCount = 10
        quizState = quizProgressionManager.progressQuiz(index, problemCount)

        assertEquals(quizState.quizIndex, index)
        assertTrue(quizState.quizFinished)
    }

    @Test
    fun endQuiz_returnsQuizStateWithQuizFinishedTrue(){
        val quizState = quizProgressionManager.endQuiz(4)

        assertTrue(quizState.quizFinished)
    }

    @Test
    fun resetAnswer_returnsEmptyString(){
        assertEquals(quizProgressionManager.resetAnswer(), "")
    }


}