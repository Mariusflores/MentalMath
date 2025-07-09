package com.example.mentalmath.logic.gamemode

import com.example.mentalmath.logic.gamemodes.PracticeModeHandler
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.utils.GameStateParser
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

class PracticeModeHandlerTest {
    private fun config() =
        ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(), null, GameMode.Practice)

    private fun config(operators: Array<Operator>) =
        ModeConfiguration(Difficulty.EASY, operators, null, GameMode.Practice)

    /**
     * General Test Cases
     * */

    @Test
    fun startGame_shouldResetVariables() {
        val handler = PracticeModeHandler()
        val config = config()

        handler.startGame(config)
        repeat(3) {
            handler.onAnswerSubmitted(true)
        }

        var gameState = handler.getGameState()

        var total = GameStateParser.getTotalProperty(gameState)
        var correct = GameStateParser.getCorrectProperty(gameState)
        var currentStreak = GameStateParser.getStreakProperty(gameState)
        var maxStreak = GameStateParser.getMaxStreakProperty(gameState)

        assertEquals(3, total, "Total: $total should be 3")
        assertEquals(3, correct, "correct: $correct should be 3")
        assertEquals(3, currentStreak, "Streak: $currentStreak should be 3")
        assertEquals(3, maxStreak, "HighestStreak: $maxStreak should be 3")

        handler.startGame(config)

        gameState = handler.getGameState()
        total = GameStateParser.getTotalProperty(gameState)
        correct = GameStateParser.getCorrectProperty(gameState)
        currentStreak = GameStateParser.getStreakProperty(gameState)
        maxStreak = GameStateParser.getMaxStreakProperty(gameState)

        assertEquals(0, total, "Total: $total should be 0 by default")
        assertEquals(0, correct, "Correct: $correct should be 0 by default")
        assertEquals(0, currentStreak, "Streak: $currentStreak should be 0 by default")
        assertEquals(0, maxStreak, "HighestStreak: $maxStreak should be 0 by default")
    }

    @Test
    fun endGameEarly_shouldSetFinishedTrue() {
        val handler = PracticeModeHandler()
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
    fun noAnswerSubmitted_gameShouldNotBeFinished() {
        val handler = PracticeModeHandler()
        val config = config()
        handler.startGame(config)

        val state = handler.getGameState()
        val isFinished = GameStateParser.getIsFinishedProperty(state)

        assertFalse(
            "Game should not be finished by default",
            isFinished
        )
    }

    @Test
    fun getNextProblem_checkOperatorsInArray() {
        val handler = PracticeModeHandler()
        val allowedOperators = Operator.OperatorConverter.toOperatorArray(listOf("+", "-"))
        val config = config(allowedOperators)

        handler.startGame(config)

        repeat(100) {
            val problem = handler.getNextProblem(config)
            assertTrue(
                "Problems with operators outside of given array should not be returned",
                problem.operator in allowedOperators
            )
        }
    }

    /**
     * Mode Unique Test Cases
     * */
    @Test
    fun onLowerStreak_highestStreakShouldNotUpdate() {
        val handler = PracticeModeHandler()
        val config = config()

        val higherStreak = 10

        handler.startGame(config)
        repeat(higherStreak) {
            handler.onAnswerSubmitted(true)
        }
        var gameState = handler.getGameState()
        var currentStreak = GameStateParser.getStreakProperty(gameState)
        var maxStreak = GameStateParser.getMaxStreakProperty(gameState)

        assertEquals(higherStreak, currentStreak, "Streak: $currentStreak should be $higherStreak")
        assertEquals(higherStreak, maxStreak, "HighestStreak: $maxStreak should be $higherStreak")

        handler.onAnswerSubmitted(false)

        val lowerStreak = 5
        repeat(lowerStreak) {
            handler.onAnswerSubmitted(true)
        }
        gameState = handler.getGameState()
        currentStreak = GameStateParser.getStreakProperty(gameState)
        maxStreak = GameStateParser.getMaxStreakProperty(gameState)

        assertEquals(lowerStreak, currentStreak, "Streak: $currentStreak should be $lowerStreak")
        assertEquals(higherStreak, maxStreak, "HighestStreak: $maxStreak should be $higherStreak")
    }

    @Test
    fun mixedAnswers_shouldTrackStreaksCorrectly() {
        val handler = PracticeModeHandler()
        val config = config()

        handler.startGame(config)
        repeat(4) { handler.onAnswerSubmitted(true) }
        handler.onAnswerSubmitted(false)

        repeat(6) { handler.onAnswerSubmitted(true) }
        handler.onAnswerSubmitted(false)

        val gameState = handler.getGameState()
        val currentStreak = GameStateParser.getStreakProperty(gameState)
        val maxStreak = GameStateParser.getMaxStreakProperty(gameState)

        assertEquals(
            0, currentStreak,
            "CurrentStreak should be 0 after last incorrect answer"
        )
        assertEquals(
            6, maxStreak,
            "Max streak should be 6"
        )
    }

    @Test
    fun alternatingAnswers_shouldTrackCorrectlyAndResetStreak() {
        val handler = PracticeModeHandler()
        val config = config()
        handler.startGame(config)

        repeat(5) {
            handler.onAnswerSubmitted(true)
            handler.onAnswerSubmitted(false)
        }

        val state = handler.getGameState()
        val correct = GameStateParser.getCorrectProperty(state)
        val streak = GameStateParser.getStreakProperty(state)
        val maxStreak = GameStateParser.getMaxStreakProperty(state)

        assertEquals(5, correct, "Should have 5 correct answers")
        assertEquals(0, streak, "Final streak should be 0 after last incorrect answer")
        assertEquals(1, maxStreak, "Max streak should be 1 due to alternation")
    }

}