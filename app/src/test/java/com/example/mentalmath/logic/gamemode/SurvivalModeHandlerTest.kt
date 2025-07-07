package com.example.mentalmath.logic.gamemode

import com.example.mentalmath.logic.gamemodes.SurvivalModeHandler
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.utils.GameStateParser
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

class SurvivalModeHandlerTest {

    private fun config() =
        ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(), null, GameMode.Survival)

    private fun config(operators: Array<Operator>) =
        ModeConfiguration(Difficulty.EASY, operators, null, GameMode.Survival)

    /**
     * General Test Cases
     * */
    @Test
    fun startGame_returnsEmptyList() {
        val handler = SurvivalModeHandler()
        val modeConfig = config()

        val quiz = handler.startGame(modeConfig)

        assertTrue(
            "Should return empty list",
            quiz.isEmpty()
        )
    }

    @Test
    fun startGame_shouldResetVariables() {
        val handler = SurvivalModeHandler()
        val config = config()

        handler.startGame(config)
        repeat(3) {
            handler.onAnswerSubmitted(true)
        }
        repeat(2) {
            handler.onAnswerSubmitted(false)
        }

        var gameState = handler.getGameState()

        var total = GameStateParser.getTotalProperty(gameState)
        var mistakes = GameStateParser.getMistakesProperty(gameState)

        assertEquals(5, total, "Total: $total should be 5")
        assertEquals(2, mistakes, "Mistakes: $mistakes should be 2")

        handler.startGame(config)

        gameState = handler.getGameState()
        total = GameStateParser.getTotalProperty(gameState)
        mistakes = GameStateParser.getMistakesProperty(gameState)

        assertEquals(0, total, "Total $total should be 0 by default")
        assertEquals(0, mistakes, "Mistakes $mistakes should be 0 by default")
    }

    @Test
    fun endGameEarly_shouldSetFinishedTrue() {
        val handler = SurvivalModeHandler()
        val config = config()

        handler.startGame(config)

        var gameState = handler.getGameState()

        var isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertFalse(
            "isFinished should be false on start game",
            isFinished
        )

        handler.endGameEarly()

        gameState = handler.getGameState()
        isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertTrue(
            "isFinished should be set true on endGameEarly()",
            isFinished
        )
    }

    @Test
    fun getNextProblem_checkOperatorsInArray() {
        val handler = SurvivalModeHandler()
        val allowedOperators = Operator.OperatorConverter.toOperatorArray(listOf("+", "-"))
        val config = config(allowedOperators)

        handler.startGame(config)

        repeat(100) {
            val problem = handler.getNextProblem(config)
            assertTrue(
                "Problems with operators outside of given array should not be returned",
                problem.operator in allowedOperators)
        }
    }

    /**
     * Mode Unique Test Cases
     * */

    @Test
    fun onMistakesEqualLives_setFinishedTrue() {
        val handler = SurvivalModeHandler()
        val config = config()

        handler.startGame(config)

        var gameState = handler.getGameState()

        var isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertFalse(
            "isFinished should be false on start game",
            isFinished
        )

        repeat(handler.lives()) {
            handler.onAnswerSubmitted(false)
        }

        gameState = handler.getGameState()

        isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertTrue(
            "isFinished should be true on running out of lives",
            isFinished
        )
    }

    @Test
    fun onCorrectAnswer_shouldNotIncrementMistakes() {
        val handler = SurvivalModeHandler()
        val config = config()

        handler.startGame(config)

        repeat(100) {
            handler.onAnswerSubmitted(true)
        }
        val gameState = handler.getGameState()
        val total = GameStateParser.getTotalProperty(gameState)
        val mistakes = GameStateParser.getMistakesProperty(gameState)
        val isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertEquals(100, total, "Total: $total should be 100")
        assertEquals(0, mistakes, "Mistakes: $mistakes should be 0")
        assertFalse(
            "IsFinished should be false on continuous correct answers",
            isFinished
        )
    }
}