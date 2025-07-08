package com.example.mentalmath.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration

private const val DIFFICULTY_DEFAULT = "Easy"

private val QUIZ_OPERATORS_DEFAULT = listOf("+", "-", "×", "÷")


class SettingsViewModel : ViewModel() {

    private val _difficulty = mutableStateOf(DIFFICULTY_DEFAULT)
    private val _quizLength = mutableStateOf(10)
    private val _operators: MutableState<List<String>> = mutableStateOf(QUIZ_OPERATORS_DEFAULT)

    val difficulty: State<String> get() = _difficulty
    val quizLength: State<Int> get() = _quizLength
    val operators: State<List<String>> get() = _operators

    fun setDifficulty(selectedDifficulty: String) {
        _difficulty.value = selectedDifficulty
    }

    fun setQuizLength(quizLength: Int) {
        _quizLength.value = quizLength
    }

    fun toggleOperator(operator: String) {
        _operators.value = _operators.value.toMutableList().apply {
            if (contains(operator)) remove(operator) else add(operator)
        }
    }
    fun getModeConfiguration(gameMode: GameMode): ModeConfiguration{
        return ModeConfiguration(
            difficulty = Difficulty.DifficultyConverter.toDifficulty(difficulty.value),
            operators = Operator.OperatorConverter.toOperatorArray(operators.value),
            length = quizLength.value,
            gameMode = gameMode

        )
    }

}