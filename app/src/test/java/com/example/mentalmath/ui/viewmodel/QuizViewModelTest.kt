package com.example.mentalmath.ui.viewmodel

import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.quiz.QuizConfiguration
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun setAnswer_updatesState() = runTest{
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val input = "7"

        quizViewModel.setAnswer(input)

        assertEquals(input, quizViewModel.answer.value)
    }

    @Test
    fun startQuiz_setDefaultStates() = runTest{
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val operatorStringList: List<String> = listOf("+", "×", "÷")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val quizLength = "6"
        val difficulty = "Hard"
        val quizConfiguration = QuizConfiguration(difficulty, quizLength, operatorStringList,
            GameMode.Casual
        )

        quizViewModel.startQuiz(quizConfiguration)


        assertTrue(quizViewModel.quiz.isNotEmpty())
        assertEquals(quizViewModel.quiz.size, quizLength.toInt())
        assertTrue(quizViewModel.quiz.all { it.operator in operatorArray })
        assertEquals(quizViewModel.quizIndex, 0)
        assertEquals(quizViewModel.isQuizFinished, false)
        assertEquals(quizViewModel.score.value, 0)
        assertEquals(quizViewModel.answer.value, "")
        assertEquals(quizViewModel.inputError.value, "")
    }

    @Test
    fun onSubmitClick_setInvalidInputOnNullValue() = runTest{
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val operatorStringList: List<String> = listOf("+", "×", "÷")
        val quizLength = "6"
        val difficulty = "Hard"
        val quizConfiguration = QuizConfiguration(difficulty, quizLength, operatorStringList,
            GameMode.Casual)

        quizViewModel.startQuiz(quizConfiguration)

        quizViewModel.setAnswer(")")

        quizViewModel.onSubmitClick()

        assertEquals(quizViewModel.inputError.value, "Please enter a valid number.")
    }

    @Test
    fun onSubmitClick_IncrementScore() = runTest {
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val operatorStringList: List<String> = listOf("+")
        val quizLength = "2"
        val difficulty = "Easy"
        val quizConfiguration = QuizConfiguration(difficulty, quizLength, operatorStringList, GameMode.Casual)

        quizViewModel.startQuiz(quizConfiguration)

        val correctAnswer = quizViewModel.currentProblem!!.correctAnswer

        quizViewModel.setAnswer(correctAnswer.toString())

        quizViewModel.onSubmitClick()

    }

    @Test
    fun onEndClick_updateStatesOnEnd() = runTest{
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher, enableTimer = false)
        val operatorStringList: List<String> = listOf("+")
        val quizLength = "6"
        val difficulty = "Easy"
        val quizConfiguration = QuizConfiguration(difficulty, quizLength, operatorStringList, GameMode.Casual)

        quizViewModel.startQuiz(quizConfiguration)

        quizViewModel.onEndClick()

        assertEquals(0, quizViewModel.score.value)

        assertEquals(0, quizViewModel.quizIndex)
        assertTrue(quizViewModel.isQuizFinished)
        assertEquals(0, quizViewModel.scoreCard.value.score)
        assertEquals(6, quizViewModel.scoreCard.value.quizLength)

    }
}