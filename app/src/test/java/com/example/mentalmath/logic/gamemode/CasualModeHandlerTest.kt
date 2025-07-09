package com.example.mentalmath.logic.gamemode

import com.example.mentalmath.logic.gamemodes.CasualModeHandler
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.utils.GameStateParser
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class CasualModeHandlerTest {

    private fun config(length: Int) = ModeConfiguration(
        Difficulty.EASY, Operator.entries.toTypedArray(), length,
        GameMode.Casual
    )

    /**
     * General Test Cases
     * */

    @Test
    fun startGame_shouldResetStateVariables() {
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = config(length)

        handler.startGame(modeConfig)

        repeat(5) {
            handler.onAnswerSubmitted(true)
        }

        var gameState = handler.getGameState()
        var index = GameStateParser.getIndexProperty(gameState)
        var correct = GameStateParser.getCorrectProperty(gameState)
        val total = GameStateParser.getTotalProperty(gameState)

        assertEquals(5, index, "Index: $index should be 5")
        assertEquals(5, correct, "Correct: $correct should be 5")
        assertEquals(length, total, "Total questions should be 10")

        handler.startGame(modeConfig)
        gameState = handler.getGameState()

        correct = GameStateParser.getCorrectProperty(gameState)
        index = GameStateParser.getIndexProperty(gameState)

        assertEquals(0, index, "Index: $index should be reset to 0")
        assertEquals(0, correct, "Correct: $correct should be reset to 0")

    }

    @Test
    fun endGameEarly_setsFinishedTrue() {
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = config(length)

        handler.startGame(modeConfig)

        var gameState = handler.getGameState()

        var isFinished = GameStateParser.getIsFinishedProperty(gameState)
        assertFalse(
            "isFinished should initially be false",
            isFinished
        )

        handler.endGameEarly()

        gameState = handler.getGameState()

        isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertTrue(
            "isFinished should be set to true",
            isFinished
        )
    }

    @Test
    fun onFinishingQuiz_shouldSetIsFinished() {
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = config(length)

        handler.startGame(modeConfig)

        repeat(length) {
            handler.onAnswerSubmitted(true)
        }

        val gameState = handler.getGameState()

        val isFinished = GameStateParser.getIsFinishedProperty(gameState)
        assertEquals(true, isFinished)
        assertTrue(
            "Should set isFinished = True on finishing quiz",
            isFinished
        )
    }

    @Test
    fun incrementsIndexCorrectly() {
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = config(length)

        handler.startGame(modeConfig)

        repeat(3) {
            handler.onAnswerSubmitted(true)
        }
        repeat(3) {
            handler.onAnswerSubmitted(false)
        }

        assertEquals(6, handler.getIndex)
    }

    @Test
    fun startGame_afterEnd_resetsStateCorrectly() {

        val handler = CasualModeHandler()
        val length = 5
        val modeConfig = config(length)

        handler.startGame(modeConfig)

        repeat(5) {
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
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = config(length)

        handler.startGame(modeConfig)

        repeat(length) {
            handler.onAnswerSubmitted(false)
        }

        val gameState = handler.getGameState()

        val correct = GameStateParser.getCorrectProperty(gameState)
        val index = GameStateParser.getIndexProperty(gameState)
        assertEquals(0, correct)
        assertEquals(length, index)
    }

    @Test
    fun partialProgress_shouldNotSetFinished() {
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = config(length)

        handler.startGame(modeConfig)

        repeat((length / 2)) {
            handler.onAnswerSubmitted(true)
        }

        val gameState = handler.getGameState()

        val isFinished = GameStateParser.getIsFinishedProperty(gameState)
        val index = GameStateParser.getIndexProperty(gameState)
        val correct = GameStateParser.getCorrectProperty(gameState)

        assertFalse(
            "Game should not be finished after partial progress",
            isFinished
        )
        assertEquals((length / 2), index)
        assertEquals((length / 2), correct)
    }

    @Test
    fun mixCorrectIncorrect_updatesStateCorrectly() {
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = config(length)

        handler.startGame(modeConfig)

        repeat((length / 2)) {
            handler.onAnswerSubmitted(true)
            handler.onAnswerSubmitted(false)
        }
        val gameState = handler.getGameState()


        val index = GameStateParser.getIndexProperty(gameState)
        val correct = GameStateParser.getCorrectProperty(gameState)


        assertEquals(length, index)
        assertEquals((length / 2), correct)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun getNextProblem_afterQuizEnds_shouldThrow() {
        val handler = CasualModeHandler()
        val config = config(2)
        handler.startGame(config)
        handler.onAnswerSubmitted(true)
        handler.onAnswerSubmitted(false)
        handler.onAnswerSubmitted(true) // Out of bounds
        handler.getNextProblem(config)  // Should throw
    }

    /**
     * Mode Unique Test Cases
     * */
    @Test
    fun zeroLengthQuiz_shouldFinishImmediately() {
        val handler = CasualModeHandler()
        val length = 0
        val config = config(length)

        handler.startGame(config)
        val gameState = handler.getGameState()

        assertTrue(
            "Zero-length quiz should be immediately finished",
            GameStateParser.getIsFinishedProperty(gameState)
        )
    }


}