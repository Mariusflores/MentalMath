package com.example.mentalmath.logic.managers

import com.example.mentalmath.logic.utils.ScoreCardParser
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

        val index = 5
        val total = 5
        val correct = 3
        val isFinished = true
        val gameState =  GameState.Casual(index, total, correct, isFinished)

        val time = 20.seconds
        val scoreCard = quizProgressionManager.getScoreCardFromGameState(gameState, time)

        val accuracy = (correct.toDouble() / total) * 100

        assertEquals(correct, ScoreCardParser.getCorrectProperty(scoreCard))
        assertEquals(total, ScoreCardParser.getTotalProperty(scoreCard))
        assertEquals(time, ScoreCardParser.getTimeElapsedOrRemaining(scoreCard))
        assertEquals(accuracy, ScoreCardParser.getAccuracyProperty(scoreCard))

    }
    @Test
    fun getScoreCard_returnsScoreValuesTimeAttack(){

        val index = 5
        val total = 5
        val correct = 4
        val isFinished = true
        val timeLeft = 30.seconds
        val gameState =  GameState.TimeAttack(index, total, correct, isFinished, timeLeft)

        val scoreCard = quizProgressionManager.getScoreCardFromGameState(gameState, timeLeft)

        val accuracy = (correct.toDouble() / total) * 100

        assertEquals(correct, ScoreCardParser.getCorrectProperty(scoreCard))
        assertEquals(total, ScoreCardParser.getTotalProperty(scoreCard))
        assertEquals(timeLeft, ScoreCardParser.getTimeElapsedOrRemaining(scoreCard))
        assertEquals(accuracy, ScoreCardParser.getAccuracyProperty(scoreCard))

    }
    @Test
    fun getScoreCard_returnsScoreValuesSurvival(){

        val mistakes = 3
        val lives = 3
        val total = 10
        val isFinished = true
        val gameState =  GameState.Survival(mistakes, lives, total, isFinished)

        val scoreCard = quizProgressionManager.getScoreCardFromGameState(gameState)

        val correct = total - mistakes

        val accuracy = (correct.toDouble() / 10.0) * 100

        assertEquals(mistakes, ScoreCardParser.getMistakesProperty(scoreCard))
        assertEquals(lives, ScoreCardParser.getLivesProperty(scoreCard))
        assertEquals(total, ScoreCardParser.getTotalProperty(scoreCard))
        assertEquals(accuracy, ScoreCardParser.getAccuracyProperty(scoreCard))

    }

    @Test
    fun getScoreCard_returnsScoreValuesPractice(){

        val correct = 10
        val total = 15
        val streak = 3
        val maxStreak = 6
        val isFinished = true
        val gameState =  GameState.Practice(correct, total, streak, maxStreak, isFinished)

        val scoreCard = quizProgressionManager.getScoreCardFromGameState(gameState, 20.seconds)

        val accuracy = (correct.toDouble() / total) * 100

        assertEquals(correct, ScoreCardParser.getCorrectProperty(scoreCard))
        assertEquals(total, ScoreCardParser.getTotalProperty(scoreCard))
        assertEquals(maxStreak, ScoreCardParser.getStreakProperty(scoreCard))
        assertEquals(accuracy, ScoreCardParser.getAccuracyProperty(scoreCard))

    }

    @Test
    fun resetAnswer_returnsEmptyString(){
        assertEquals("",quizProgressionManager.resetAnswer())
    }

    @Test
    fun returnInvalidInputString(){
        assertEquals("Please enter a valid number.", quizProgressionManager.returnInvalidInputString())
    }


}