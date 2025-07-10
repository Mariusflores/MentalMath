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
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    /**
     * Test Structure:
     * 1. Setup and Helpers
     * 2. Input and Answer Handling
     * 3. Quiz Initialization
     * 4. Answer Submission and Progression
     * 5. Score Card and End Game Logic
     * 6. Timer Logic
     * 7. Answer Feedback (lastAnswerCorrect)
     * 8. Edge Cases
     **/
    private val testDispatcher = StandardTestDispatcher()

    /**
     * Setup and helpers
     * */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    private fun config(
        difficulty: Difficulty,
        operators: Array<Operator>,
        length: Int = 0,
        gameMode: GameMode
    ) =
        ModeConfiguration(difficulty, operators, length, gameMode)

    private fun getQuizViewModel(enableTimer: Boolean = false): QuizViewModel =
        QuizViewModel(dispatcher = testDispatcher, enableTimer = enableTimer)

    private fun inputValueTest(input: String) {
        val quizViewModel = getQuizViewModel()
        val operatorStringList: List<String> = listOf("+", "×", "÷")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val difficulty = Difficulty.EASY
        val length = 10
        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, length, gameMode)

        quizViewModel.startQuiz(config)

        quizViewModel.setAnswer(input)

        quizViewModel.onSubmitClick()

        assertEquals("Please enter a valid number.", quizViewModel.inputError.value)
    }
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
            var problem = quizViewModel.currentProblem.value!!
            quizViewModel.setAnswer(problem.correctAnswer.toString())
            quizViewModel.onSubmitClick()
            assertTrue(
                "Should be true",
                quizViewModel.lastAnswerCorrect
            )

            problem = quizViewModel.currentProblem.value!!
            quizViewModel.setAnswer((problem.correctAnswer - 1).toString())
            quizViewModel.onSubmitClick()
            assertFalse(
                "Should be false",
                quizViewModel.lastAnswerCorrect
            )
        }
    }

    /**
     * region: Input and Answer Handling
     * */
    @Test
    fun setAnswer_updatesState() = runTest {

        val quizViewModel = getQuizViewModel()
        val input = "7"

        quizViewModel.setAnswer(input)

        assertEquals(input, quizViewModel.answer.value)
    }

    @Test
    fun onSubmitClick_setInvalidInputOnNullValue() = runTest {
        val input = ")"
        inputValueTest(input)
    }

    @Test
    fun onSubmitClick_InvalidInputOnNonIntegerNumber() = runTest {
        val input = "3.5"
        inputValueTest(input)
    }

    @Test
    fun onSubmitClick_InvalidInputOnEmptyString() = runTest {
        val input = ""
        inputValueTest(input)
    }
    /**
     * region: Quiz Initialization
     * */
    @Test
    fun startQuiz_setDefaultStates() = runTest {
        val quizViewModel = getQuizViewModel()
        val operatorStringList: List<String> = listOf("+", "×", "÷")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val difficulty = Difficulty.EASY
        val length = 10
        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, length, gameMode)


        quizViewModel.startQuiz(config)


        assertEquals(0, quizViewModel.gameStateIndex)
        assertEquals(false, quizViewModel.isGameFinished)
        assertEquals("", quizViewModel.answer.value)
        assertEquals("", quizViewModel.inputError.value)
    }

    @Test
    fun startQuiz_shouldResetPreviousState_sameMode() = runTest {

        val quizViewModel = getQuizViewModel()

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY
        val length = 10

        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, length, gameMode = gameMode)

        quizViewModel.startQuiz(config)

        val problem = quizViewModel.currentProblem.value!!
        quizViewModel.setAnswer((problem.correctAnswer - 1).toString())
        quizViewModel.onSubmitClick()
        assertFalse(
            "Should be false",
            quizViewModel.lastAnswerCorrect
        )
        assertEquals(1, GameStateParser.getIndexProperty(quizViewModel.gameState))


        quizViewModel.startQuiz(config)

        // True by default (for UI)
        assertTrue(
            "lastAnswer should be true by default",
            quizViewModel.lastAnswerCorrect
        )
        assertEquals(0, GameStateParser.getIndexProperty(quizViewModel.gameState))
    }
    @Test
    fun lastAnswerCorrect_startQuizMidGame_shouldReset() = runTest {

        val quizViewModel = getQuizViewModel()

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.TimeAttack
        var config = config(difficulty, operatorArray, gameMode = gameMode)

        quizViewModel.startQuiz(config)

        val problem = quizViewModel.currentProblem.value!!
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
     * region: Answer Submission and Progression
     * */
    @Test
    fun onSubmitClick_IncrementScore() = runTest {
        val quizViewModel = getQuizViewModel()
        val operatorStringList: List<String> = listOf("+")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val difficulty = Difficulty.EASY
        val length = 10
        val gameMode = GameMode.Casual
        val config = config(difficulty, operatorArray, length, gameMode)


        quizViewModel.startQuiz(config)

        val problem = quizViewModel.currentProblem.value!!
        quizViewModel.setAnswer(problem.correctAnswer.toString())

        quizViewModel.onSubmitClick()

        val correct = GameStateParser.getCorrectProperty(quizViewModel.gameState)

        assertEquals(1, correct, "Should increment score by 1")

    }
    @Test
    fun onSubmitClick_currentProblem__infiniteMode() = runTest {
        val quizViewModel = getQuizViewModel()

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.Practice
        val config = config(difficulty, operatorArray, gameMode = gameMode)

        quizViewModel.startQuiz(config)

        repeat(10) {
            val problem = quizViewModel.currentProblem.value!!
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
     * region: Score Card and End Game Logic
     * */

    @Test
    fun onEndClick_updateStatesOnEnd() = runTest {
        val quizViewModel = getQuizViewModel()
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
    fun onEndGame_scoreCard_accuracyCalculatedCorrectly() {
        val quizViewModel = getQuizViewModel()
        val operatorStringList: List<String> = listOf("+")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val difficulty = Difficulty.EASY
        val gameMode = GameMode.Practice
        val config = config(difficulty, operatorArray, gameMode = gameMode)


        quizViewModel.startQuiz(config)

        repeat(3) {
            val problem = quizViewModel.currentProblem.value!!
            quizViewModel.setAnswer(problem.correctAnswer.toString())
            quizViewModel.onSubmitClick()
        }
        repeat(3) {
            val problem = quizViewModel.currentProblem.value!!
            quizViewModel.setAnswer((problem.correctAnswer - 1).toString())
            quizViewModel.onSubmitClick()
        }
        quizViewModel.onEndClick()
        // Calculate logic (correct / total) * 100
        assertEquals(50.0, quizViewModel.scoreCardAccuracy, "Accuracy should be 0.5")
    }
    @Test
    fun onEndClick_shouldBeIdempotent() = runTest {
        val quizViewModel = getQuizViewModel(true)
        val config = config(Difficulty.EASY, Operator.entries.toTypedArray(), 1, GameMode.Casual)
        quizViewModel.startQuiz(config)

        delay(100)
        quizViewModel.onEndClick()
        val time1 = quizViewModel.scoreCardTime

        quizViewModel.onEndClick()
        val time2 = quizViewModel.scoreCardTime

        assertEquals(time1, time2, "ScoreCard should not change on repeated calls")
    }

    @Test
    fun survivalMode_endGameOnMistakes() = runTest {
        val quizViewModel = getQuizViewModel()

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.Survival
        val config = config(difficulty, operatorArray, gameMode = gameMode)

        quizViewModel.startQuiz(config)

        val mistakes = 3
        repeat(mistakes) {
            val problem = quizViewModel.currentProblem.value!!
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
     * region Timer Logic
     * */
    @Test
    fun timerShouldIncrement_stopwatchMode() = runTest {
        val quizViewModel = getQuizViewModel(true)

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
        val fakeTimeSource = TestTimeSource()
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, timerScope = this, enableTimer = true, timeSource = fakeTimeSource)

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.TimeAttack
        val config = config(difficulty, operatorArray, gameMode = gameMode)

        quizViewModel.startQuiz(config)

        fakeTimeSource += 50.milliseconds

        quizViewModel.onEndClick()

        val elapsed = quizViewModel.scoreCardTime

        assertTrue(elapsed < 60.seconds)
    }

    @Test
    fun onTimerEnabledFalse_shouldNotUpdate() = runTest {
        val quizViewModel = getQuizViewModel()

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY
        val length = 10

        val gameMode = GameMode.Casual

        val config = config(difficulty, operatorArray, length, gameMode)

        quizViewModel.startQuiz(config)

        delay(100)

        quizViewModel.onEndClick()
        assertEquals(
            Duration.ZERO, quizViewModel.scoreCardTime,
            "Timer should not update"
        )
    }

    @Test
    fun startQuiz_shouldResetTimer_newQuiz() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, timerScope = this, enableTimer = true)

        val config =
            config(Difficulty.EASY, Operator.entries.toTypedArray(), 10, gameMode = GameMode.Casual)

        quizViewModel.startQuiz(config)
        advanceTimeBy(200)
        quizViewModel.onEndClick()

        val firstTime = quizViewModel.scoreCardTime

        quizViewModel.startQuiz(config)
        advanceTimeBy(100)
        quizViewModel.onEndClick()

        val secondTime = quizViewModel.scoreCardTime
        println("first: ${firstTime.inWholeMilliseconds}")
        println("second: ${secondTime.inWholeMilliseconds}")
        println(secondTime < firstTime)
        assertTrue(
            "Timer should reset on new quiz",
            secondTime < firstTime
        )
    }

    @Test
    fun timeAttack_endsOnCountdown()= runTest{
        val fakeTimeSource = TestTimeSource()
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, timerScope = this, timeSource = fakeTimeSource)
        val config =
            config(Difficulty.EASY, Operator.entries.toTypedArray(),  gameMode = GameMode.TimeAttack)

        quizViewModel.startQuiz(config)
        fakeTimeSource += 60.seconds
        runCurrent()


        assertTrue(
            "Should set gameFinished true, but was ${quizViewModel.isGameFinished} ",
            quizViewModel.isGameFinished
        )
    }
    /**
     * region: Answer Feedback (lastAnswerCorrect)
     * */
    @Test
    fun lastAnswerCorrect_shouldUpdateCorrectly_casual() = runTest {
        val quizViewModel = getQuizViewModel()

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY
        val length = 10

        val gameMode = GameMode.Casual
        lastAnswerCorrectTest(
            difficulty,
            operatorArray,
            length,
            gameMode,
            quizViewModel = quizViewModel
        )

    }

    @Test
    fun lastAnswerCorrect_shouldUpdateCorrectly_timeAttack() = runTest {
        val quizViewModel = getQuizViewModel()

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.TimeAttack
        lastAnswerCorrectTest(
            difficulty,
            operatorArray,
            gameMode = gameMode,
            quizViewModel = quizViewModel
        )
    }

    @Test
    fun lastAnswerCorrect_shouldUpdateCorrectly_survival() = runTest {
        val quizViewModel = getQuizViewModel()

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.Survival
        lastAnswerCorrectTest(
            difficulty,
            operatorArray,
            gameMode = gameMode,
            quizViewModel = quizViewModel
        )
    }

    @Test
    fun lastAnswerCorrect_shouldUpdateCorrectly_practice() = runTest {
        val quizViewModel = getQuizViewModel()

        val operatorArray = Operator.entries.toTypedArray()
        val difficulty = Difficulty.EASY

        val gameMode = GameMode.Practice
        lastAnswerCorrectTest(
            difficulty,
            operatorArray,
            gameMode = gameMode,
            quizViewModel = quizViewModel
        )
    }

    /**
     * region: Edge Cases
     * */
    /*
    @Test
    fun onSubmitClick_withoutStartingQuiz_shouldThrowException() = runTest {
        val quizViewModel = getQuizViewModel()
        quizViewModel.setAnswer("43")

        assertFailsWith<IllegalStateException> {
            quizViewModel.onSubmitClick()
        }
    }

     */

    @Test
    fun currentOrNextProblem_shouldReturnNull_indexOutOfBounds_finiteMode() = runTest {
        val quizViewModel = getQuizViewModel()

        val config = config(Difficulty.EASY, arrayOf(Operator.ADD), length = 1, GameMode.Casual)

        quizViewModel.startQuiz(config)

        val problem = quizViewModel.currentProblem.value!!

        quizViewModel.setAnswer(problem.correctAnswer.toString())
        quizViewModel.onSubmitClick()

    }

}
