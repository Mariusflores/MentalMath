package com.example.mentalmath.viewmodel

import com.example.mentalmath.ui.viewmodel.SettingsViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SettingsViewModelTest {

    @Test
    fun setDifficulty_updatesState(){
        val settingsViewModel = SettingsViewModel()
        val difficulty = "Hard"
        settingsViewModel.setDifficulty(difficulty)

        assertEquals(settingsViewModel.difficulty.value, difficulty)
    }

    @Test
    fun setQuizLength_updateState(){
        val settingsViewModel = SettingsViewModel()
        val quizLength = "20"

        settingsViewModel.setQuizLength(quizLength)

        assertEquals(settingsViewModel.quizLength.value, quizLength)
    }

    @Test
    fun toggleOperator_removeOrAddFromState(){
        val settingsViewModel = SettingsViewModel()
        val operatorList = listOf("+", "-", "×", "÷")
        val operator = "+"

        assertTrue(operatorList.all { it in settingsViewModel.operators.value })

        settingsViewModel.toggleOperator(operator)

        assertTrue(!operatorList.all { it in settingsViewModel.operators.value })

        settingsViewModel.toggleOperator(operator)

        assertTrue(operatorList.all { it in settingsViewModel.operators.value })





    }
}