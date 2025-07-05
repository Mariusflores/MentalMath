package com.example.mentalmath.logic.managers

import com.example.mentalmath.logic.gamemodes.factory.ScoreCardParser
import com.example.mentalmath.logic.generators.MathProblemGenerator
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.quiz.GameState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

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
    fun getScoreCard_returnsScoreValuesCasual(){

        val gameState =  GameState.Casual(5, 5, 3, true)

        val scoreCard = quizProgressionManager.getScoreCardFromGameState(gameState, 20.seconds)

        var accuracy = (3.0 / 5.0) * 100



        assertEquals(3, ScoreCardParser.getCorrectProperty(scoreCard))
        assertEquals(5, ScoreCardParser.getTotalProperty(scoreCard))
        assertEquals(20.seconds, ScoreCardParser.getTimeElapsedOrRemaining(scoreCard))
        assertEquals(accuracy, ScoreCardParser.getAccuracyProperty(scoreCard))


    }

    @Test
    fun resetAnswer_returnsEmptyString(){
        assertEquals(quizProgressionManager.resetAnswer(), "")
    }


}