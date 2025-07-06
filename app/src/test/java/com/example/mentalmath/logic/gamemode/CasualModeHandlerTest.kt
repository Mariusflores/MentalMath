package com.example.mentalmath.logic.gamemode

import com.example.mentalmath.logic.gamemodes.CasualModeHandler
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.utils.GameStateParser
import org.junit.Test
import kotlin.test.assertEquals

class CasualModeHandlerTest {

    @Test
    fun startGame_ShouldReturnQuiz(){
        val gameHandler = CasualModeHandler()
        val length = 10
        val modeConfig = ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(),
            length, GameMode.Casual)

        val quiz = gameHandler.startGame(modeConfig)

        assertEquals(
            length, quiz.size,
            "Quiz size ${quiz.size} should match given length: $length"

        )
    }

    @Test
    fun startGame_shouldResetStateVariables(){
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(),
            length, GameMode.Casual)

        val quiz = handler.startGame(modeConfig)

        repeat(5){
            handler.onAnswerSubmitted(true)
        }

        var gameState = handler.getGameState()
        var index = GameStateParser.getIndexProperty(gameState)
        var correct = GameStateParser.getCorrectProperty(gameState)
        val total = GameStateParser.getTotalProperty(gameState)

        assertEquals(5, index, "Index: $index should be 4")
        assertEquals(5, correct, "Correct: $correct should be 5")
        assertEquals(length, total, "Total questions should be 10")
        assertEquals(total, quiz.size)


        handler.startGame(modeConfig)
        gameState = handler.getGameState()

        correct = GameStateParser.getCorrectProperty(gameState)
        index = GameStateParser.getIndexProperty(gameState)

        assertEquals(0, index, "Index: $index should be reset to 0")
        assertEquals(0, correct, "Correct: $correct should be reset to 0")

    }
    @Test
    fun endGameEarly_setsFinishedTrue(){
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(),
            length, GameMode.Casual)

        handler.startGame(modeConfig)

        var gameState = handler.getGameState()

        var isFinished = GameStateParser.getIsFinishedProperty(gameState)
        assertEquals(false, isFinished, "isFinished should initially be false")


        handler.endGameEarly()

        gameState = handler.getGameState()


        isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertEquals(true, isFinished, "isFinished should be set to true")
    }

    @Test
    fun startGame_afterEnd_resetsStateCorrectly(){

        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(),
            length, GameMode.Casual)

        handler.startGame(modeConfig)

        handler.endGameEarly()

        var gameState = handler.getGameState()

        var isFinished = GameStateParser.getIsFinishedProperty(gameState)
        assertEquals(true, isFinished, "isFinished should be set to true")

        handler.startGame(modeConfig)

        gameState = handler.getGameState()


        isFinished = GameStateParser.getIsFinishedProperty(gameState)

        assertEquals(false, isFinished, "isFinished should be reset to false")
    }

    @Test
    fun shouldSetIsFinished(){
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(),
            length, GameMode.Casual)

        handler.startGame(modeConfig)

        repeat(length){
            handler.onAnswerSubmitted(true)
        }

        val gameState = handler.getGameState()

        val isFinished = GameStateParser.getIsFinishedProperty(gameState)
        assertEquals(true, isFinished)
    }

    @Test
    fun shouldNotIncrementCorrectOnIncorrectAnswer(){
        val handler = CasualModeHandler()
        val length = 10
        val modeConfig = ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(),
            length, GameMode.Casual)

        handler.startGame(modeConfig)

        repeat(length){
            handler.onAnswerSubmitted(false)
        }

        val gameState = handler.getGameState()

        val correct = GameStateParser.getCorrectProperty(gameState)
        val index = GameStateParser.getIndexProperty(gameState)
        assertEquals(0, correct)
        assertEquals(length, index)
    }
    @Test
    fun zeroLengthQuiz_shouldFinishImmediately() {
        val handler = CasualModeHandler()
        val config = ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(), 0, GameMode.Casual)

        handler.startGame(config)
        val gameState = handler.getGameState()

        assertEquals(true, GameStateParser.getIsFinishedProperty(gameState), "Zero-length quiz should be immediately finished")
    }


}