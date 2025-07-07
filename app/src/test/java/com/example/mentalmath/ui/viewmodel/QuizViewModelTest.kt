package com.example.mentalmath.ui.viewmodel

import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.utils.GameStateParser
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private fun config(
        difficulty: Difficulty,
        operators: Array<Operator>,
        length: Int = 0,
        gameMode: GameMode
    ) =
        ModeConfiguration(difficulty, operators, length, gameMode)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * General Test Cases
     * */
    @Test
    fun setAnswer_updatesState() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val input = "7"

        quizViewModel.setAnswer(input)

        assertEquals(input, quizViewModel.answer.value)
    }

    @Test
    fun onSubmitClick_setInvalidInputOnNullValue() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val operatorStringList: List<String> = listOf("+", "×", "÷")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val difficulty = Difficulty.EASY
        val length = 10
        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, length, gameMode)


        quizViewModel.startQuiz(config)

        quizViewModel.setAnswer(")")

        quizViewModel.onSubmitClick()

        assertEquals("Please enter a valid number.", quizViewModel.inputError.value)
    }

    @Test
    fun onSubmitClick_InvalidInputOnNonIntegerNumber() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val operatorStringList: List<String> = listOf("+", "×", "÷")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val difficulty = Difficulty.EASY
        val length = 10
        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, length, gameMode)


        quizViewModel.startQuiz(config)

        quizViewModel.setAnswer("3.5")

        quizViewModel.onSubmitClick()

        assertEquals("Please enter a valid number.", quizViewModel.inputError.value)
    }

    @Test
    fun onSubmitClick_InvalidInputOnEmptyString() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val operatorStringList: List<String> = listOf("+", "×", "÷")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val difficulty = Difficulty.EASY
        val length = 10
        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, length, gameMode)


        quizViewModel.startQuiz(config)

        quizViewModel.setAnswer("")

        quizViewModel.onSubmitClick()

        assertEquals("Please enter a valid number.", quizViewModel.inputError.value)
    }

    @Test
    fun startQuiz_setDefaultStates() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val operatorStringList: List<String> = listOf("+", "×", "÷")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val difficulty = Difficulty.EASY
        val length = 10
        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, length, gameMode)


        quizViewModel.startQuiz(config)


        assertTrue(quizViewModel.quiz.isNotEmpty())
        assertEquals(length, quizViewModel.quiz.size)
        assertTrue(quizViewModel.quiz.all { it.operator in operatorArray })
        assertEquals(0, quizViewModel.gameStateIndex)
        assertEquals(false, quizViewModel.isGameFinished)
        assertEquals("", quizViewModel.answer.value)
        assertEquals("", quizViewModel.inputError.value)
    }

    @Test
    fun onSubmitClick_IncrementScore() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val operatorStringList: List<String> = listOf("+")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val difficulty = Difficulty.EASY
        val length = 10
        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, length, gameMode)


        quizViewModel.startQuiz(config)

        val problem = quizViewModel.currentOrNextProblem!!
        quizViewModel.setAnswer(problem.correctAnswer.toString())

        quizViewModel.onSubmitClick()

        val correct = GameStateParser.getCorrectProperty(quizViewModel.gameState)

        assertEquals(1, correct, "Should increment score by 1")

    }

    @Test
    fun onEndClick_updateStatesOnEnd() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val operatorStringList: List<String> = listOf("+")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val difficulty = Difficulty.EASY
        val length = 5
        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, length, gameMode)


        quizViewModel.startQuiz(config)

        quizViewModel.onEndClick()

        assertEquals(0, quizViewModel.scoreCardCorrect)

        assertEquals(0, quizViewModel.gameStateIndex)
        assertTrue(quizViewModel.isGameFinished)
        assertEquals(length, quizViewModel.scoreCardTotal)

    }

    @Test
    fun onSubmitClick_currentOrNextProblem_infiniteMode() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.Practice
        val config = config(difficulty, operatorArray, gameMode = gameMode)

        quizViewModel.startQuiz(config)

        repeat(10) {
            val problem = quizViewModel.currentOrNextProblem!!
            quizViewModel.setAnswer(problem.correctAnswer.toString())
            quizViewModel.onSubmitClick()
        }

        assertEquals(
            10, GameStateParser.getCorrectProperty(quizViewModel.gameState),
            "Correct should be 10"
        )
        assertEquals(
            10, GameStateParser.getTotalProperty(quizViewModel.gameState),
            "Total should be 10"
        )
    }

    /**
     * End Game Logic
     * */
    @Test
    fun survivalMode_endGameOnMistakes() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.Survival
        val config = config(difficulty, operatorArray, gameMode = gameMode)

        quizViewModel.startQuiz(config)

        val mistakes = 3
        repeat(mistakes) {
            val problem = quizViewModel.currentOrNextProblem!!
            quizViewModel.setAnswer((problem.correctAnswer - 1).toString())
            quizViewModel.onSubmitClick()
        }

        assertEquals(
            mistakes, GameStateParser.getMistakesProperty(quizViewModel.gameState),
            "Correct should be 3"
        )
        assertTrue(
            "isFinished should set true",
            GameStateParser.getIsFinishedProperty(quizViewModel.gameState)
        )
    }

    /**
     * Timer Logic
     * */
    @Test
    fun timerShouldIncrement_stopwatchMode() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = true)

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, 10, gameMode = gameMode)

        quizViewModel.startQuiz(config)

        delay(50)

        quizViewModel.onEndClick()

        val elapsed = quizViewModel.scoreCardTime

        assertTrue(elapsed > Duration.ZERO)
    }

    @Test
    fun timerShouldDecrease_countdownMode() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = true)

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.TimeAttack
        val config = config(difficulty, operatorArray, gameMode = gameMode)

        quizViewModel.startQuiz(config)

        delay(50)

        quizViewModel.onEndClick()

        val elapsed = quizViewModel.scoreCardTime

        assertTrue(elapsed < 60.seconds)
    }

    /**
     * Last Answer Correct Test Cases
     * */
    @Test
    fun lastAnswerCorrect_shouldUpdateCorrectly_casual() {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY
        val length = 10

        val gameMode = GameMode.Casual
        lastAnswerCorrectTest(difficulty, operatorArray, length, gameMode, quizViewModel = quizViewModel)

    }

    @Test
    fun lastAnswerCorrect_shouldUpdateCorrectly_timeAttack() {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.TimeAttack
        lastAnswerCorrectTest(difficulty, operatorArray, gameMode = gameMode, quizViewModel = quizViewModel)
    }

    @Test
    fun lastAnswerCorrect_shouldUpdateCorrectly_survival() {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.Survival
        lastAnswerCorrectTest(difficulty, operatorArray, gameMode = gameMode, quizViewModel = quizViewModel)
    }

    @Test
    fun lastAnswerCorrect_shouldUpdateCorrectly_practice() {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.Practice
        lastAnswerCorrectTest(difficulty, operatorArray, gameMode = gameMode, quizViewModel = quizViewModel)
    }

    @Test
    fun lastAnswerCorrect_startQuizMidGame_shouldReset(){

        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = true)

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.TimeAttack
        var config = config(difficulty, operatorArray, gameMode = gameMode)

        quizViewModel.startQuiz(config)

        val problem = quizViewModel.currentOrNextProblem!!
        quizViewModel.setAnswer((problem.correctAnswer - 1).toString())
        quizViewModel.onSubmitClick()
        assertFalse(
            "Should be false",
            quizViewModel.lastAnswerCorrect
        )
        val newGameMode = GameMode.Practice
        config = config(difficulty, operatorArray, gameMode = newGameMode)

        quizViewModel.startQuiz(config)

        // True by default (for UI)
        assertTrue(
            "lastAnswer should be true by default",
            quizViewModel.lastAnswerCorrect
        )
    }


    /**
     * Parameterized test
     * */

    private fun lastAnswerCorrectTest(
        difficulty: Difficulty,
        operatorArray: Array<Operator>,
        length: Int = 0,
        gameMode: GameMode,
        quizViewModel: QuizViewModel
    ) {
        val config = config(difficulty, operatorArray, length, gameMode)

        quizViewModel.startQuiz(config)

        repeat(5) {
            var problem = quizViewModel.currentOrNextProblem!!
            quizViewModel.setAnswer(problem.correctAnswer.toString())
            quizViewModel.onSubmitClick()
            assertTrue(
                "Should be true",
                quizViewModel.lastAnswerCorrect
            )

            problem = quizViewModel.currentOrNextProblem!!
            quizViewModel.setAnswer((problem.correctAnswer - 1).toString())
            quizViewModel.onSubmitClick()
            assertFalse(
                "Should be false",
                quizViewModel.lastAnswerCorrect
            )
        }
    }


}
