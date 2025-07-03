package com.example.mentalmath.viewmodel

import com.example.mentalmath.logic.models.Operator
import com.example.mentalmath.logic.models.QuizConfiguration
import com.example.mentalmath.ui.viewmodel.QuizViewModel
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
    fun tearDown(){
        Dispatchers.resetMain()
}
    @Test
    fun startQuiz_setDefaultStates() = runTest{
        val quizViewModel = QuizViewModel(dispatcher = testDispatcher)
        val operatorStringList: List<String> = listOf("+", "×", "÷")
        val operatorArray = Operator.OperatorConverter.toOperatorArray(operatorStringList)
        val quizLength = "6"
        val difficulty = "Hard"
        val quizConfiguration = QuizConfiguration(difficulty, quizLength, operatorStringList)

        quizViewModel.startQuiz(quizConfiguration)

        testScheduler.advanceUntilIdle()

        assertTrue(quizViewModel.quiz.isNotEmpty())
        assertEquals(quizViewModel.quiz.size, quizLength.toInt())
        assertTrue(quizViewModel.quiz.all { it.operator in operatorArray })
        assertEquals(quizViewModel.quizIndex, 0)
        assertEquals(quizViewModel.isQuizFinished, false)
        assertEquals(quizViewModel.score.value, 0)
        assertEquals(quizViewModel.answer.value, "")
        assertEquals(quizViewModel.inputError.value, "")
    }
}