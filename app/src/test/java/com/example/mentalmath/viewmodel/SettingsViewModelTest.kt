package com.example.mentalmath.viewmodel

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mentalmath.logic.models.Difficulty
import com.example.mentalmath.ui.viewmodel.SettingsViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SettingsViewModelTest {

    @Test
    fun setDifficulty_updatesState(){
        val settingsViewModel: SettingsViewModel = SettingsViewModel()
        val difficulty = "Hard"
        settingsViewModel.setDifficulty(difficulty)

        assertEquals(settingsViewModel.difficulty.value, difficulty)
    }
}