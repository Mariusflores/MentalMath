package com.example.mentalmath.logic.gamemode

import com.example.mentalmath.logic.gamemodes.TimeAttackModeHandler
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.utils.GameStateParser
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TimeAttackModeHandlerTest {

    private fun config(difficulty: Difficulty) = ModeConfiguration(
        difficulty, Operator.entries.toTypedArray(), null,
        GameMode.TimeAttack
    )
    /**
     * General Test Cases
     * */

    @Test
    fun startGame_ShouldResetVariables() {
        val gameHandler = TimeAttackModeHandler()

        val modeConfig = config(Difficulty.MEDIUM)

        gameHandler.startGame(modeConfig)

        repeat(5) {
            gameHandler.onAnswerSubmitted(true)
        }

        var gameState = gameHandler.getGameState(15.seconds)

        var index = GameStateParser.getIndexProperty(gameState)
        var correct = GameStateParser.getCorrectProperty(gameState)
        val total = GameStateParser.getTotalProperty(gameState)
        val timeLeft = GameStateParser.getTimeLeftProperty(gameState)

        assertEquals(5, index, "Index:$index should be 5")
        assertEquals(5, correct, "Correct:$correct should be 5")
        //Difficulty.MEDIUM has constant length of 15 in TimeAttack
        assertEquals(15, total, "Total:$total should be 15")
        assertEquals(15.seconds, timeLeft, "Time left:$timeLeft should be 15s")

        gameHandler.startGame(modeConfig)

        gameState = gameHandler.getGameState()

        index = GameStateParser.getIndexProperty(gameState)
        correct = GameStateParser.getCorrectProperty(gameState)

        assertEquals(0, index, "Index:$index should be 0 by default")
        assertEquals(0, correct, "Correct:$correct should be 0 by default")
        assertEquals(15.seconds, timeLeft, "Time left:$timeLeft should be 60s by default")

    }

    @Test
    fun endGameEarly_shouldSetFinishedTrue() {
        val gameHandler = TimeAttackModeHandler()

        val modeConfig = config(Difficulty.EASY)

        gameHandler.startGame(modeConfig)

        var gameState = gameHandler.getGameState(50.seconds)
        var isFinished = GameStateParser.getIsFinishedProperty(gameState)
        assertFalse(
            "IsFinished:$isFinished should be false by default",
            isFinished
        )

        gameHandler.endGameEarly()

        gameState = gameHandler.getGameState()

        isFinished = GameStateParser.getIsFinishedProperty(gameState)
        assertTrue(
            "IsFinished:$isFinished should be set true on end game early",
            isFinished
        )
    }

    @Test
    fun onFinishingQuiz_shouldSetFinishedTrue() {
        val gameHandler = TimeAttackModeHandler()

        val modeConfig = config(Difficulty.EASY)

        gameHandler.startGame(modeConfig)

        repeat(10) {
            gameHandler.onAnswerSubmitted(true)
        }

        val gameState = gameHandler.getGameState(5.seconds)
        val isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertTrue(
            "Should set true on finishing quiz",
            isFinished
        )
    }
    @Test
    fun incrementsIndexCorrectly(){
        val handler = TimeAttackModeHandler()
        val modeConfig = config(Difficulty.MEDIUM)

        handler.startGame(modeConfig)

        repeat(3){
            handler.onAnswerSubmitted(true)
        }
        repeat(3){
            handler.onAnswerSubmitted(false)
        }

        assertEquals(6, handler.getIndex)
    }

    @Test
    fun startGame_afterEnd_resetsStateCorrectly() {

        val handler = TimeAttackModeHandler()
        val modeConfig = config(Difficulty.HARD)

        handler.startGame(modeConfig)
        // Hard mode default length = 20
        repeat(20) {
            handler.onAnswerSubmitted(true)
        }

        var gameState = handler.getGameState()

        var isFinished = GameStateParser.getIsFinishedProperty(gameState)
        assertTrue(
            "isFinished should be set to true",
            isFinished
        )

        handler.startGame(modeConfig)

        gameState = handler.getGameState()


        isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertFalse(
            "isFinished should be reset to false",
            isFinished
        )
    }

    @Test
    fun onIncorrectAnswer_shouldNotIncrement() {
        val handler = TimeAttackModeHandler()
        val modeConfig = config(Difficulty.EASY)

        handler.startGame(modeConfig)

        repeat(10) {
            handler.onAnswerSubmitted(false)
        }

        val gameState = handler.getGameState()

        val correct = GameStateParser.getCorrectProperty(gameState)
        val index = GameStateParser.getIndexProperty(gameState)
        assertEquals(0, correct)
        assertEquals(10, index)
    }

    @Test
    fun partialProgress_shouldNotSetFinished() {
        val handler = TimeAttackModeHandler()
        val modeConfig = config(Difficulty.EASY)

        handler.startGame(modeConfig)

        repeat(5) {
            handler.onAnswerSubmitted(true)
        }

        val gameState = handler.getGameState(10.seconds)

        val isFinished = GameStateParser.getIsFinishedProperty(gameState)
        val index = GameStateParser.getIndexProperty(gameState)
        val correct = GameStateParser.getCorrectProperty(gameState)

        assertFalse(
            "Game should not be finished after partial progress",
            isFinished
        )
        assertEquals(5, index)
        assertEquals(5, correct)
    }

    @Test
    fun mixCorrectIncorrect_updatesStateCorrectly() {
        val handler = TimeAttackModeHandler()
        val modeConfig = config(Difficulty.EASY)

        handler.startGame(modeConfig)

        repeat(5) {
            handler.onAnswerSubmitted(true)
            handler.onAnswerSubmitted(false)
        }
        val gameState = handler.getGameState(30.seconds)


        val index = GameStateParser.getIndexProperty(gameState)
        val correct = GameStateParser.getCorrectProperty(gameState)


        assertEquals(10, index)
        assertEquals(5, correct)
    }
    @Test(expected = IndexOutOfBoundsException::class)
    fun getNextProblem_afterQuizEnds_shouldThrow() {
        val handler = TimeAttackModeHandler()
        val config = config(Difficulty.EASY)
        handler.startGame(config)
        repeat(5){
            handler.onAnswerSubmitted(true)
            handler.onAnswerSubmitted(false)
        }
        handler.onAnswerSubmitted(true) // Out of bounds
        handler.getNextProblem(config)  // Should throw
    }

    /**
     * Mode Unique Test Cases
     * */
    @Test
    fun onCountdownZero_shouldSetFinishedTrue() {
        val gameHandler = TimeAttackModeHandler()

        val modeConfig = config(Difficulty.EASY)

        gameHandler.startGame(modeConfig)

        val gameState = gameHandler.getGameState(0.milliseconds)

        val isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertTrue(
            "Should set finished on timeLeft Zero",
            isFinished
        )
    }

    @Test
    fun getGameState_nullValue() {
        val handler = TimeAttackModeHandler()
        val modeConfig = config(Difficulty.EASY)

        handler.startGame(modeConfig)

        var gameState = handler.getGameState(20.seconds)

        var timeLeft = GameStateParser.getTimeLeftProperty(gameState)

        assertEquals(20.seconds, timeLeft, "Time left: $timeLeft should be 20s")

        gameState = handler.getGameState(null)
        timeLeft = GameStateParser.getTimeLeftProperty(gameState)

        assertEquals(20.seconds, timeLeft, "Null should not have broken the time left state")
    }


}