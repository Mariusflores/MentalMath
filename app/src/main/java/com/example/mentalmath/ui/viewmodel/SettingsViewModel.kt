package com.example.mentalmath.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

private const val DIFFICULTY_DEFAULT = "Easy"

private const val QUIZ_LENGTH_DEFAULT = "10"

class SettingsViewModel: ViewModel() {

    private val _difficulty = mutableStateOf(DIFFICULTY_DEFAULT)
    private val _quizLength = mutableStateOf(QUIZ_LENGTH_DEFAULT)
    private val _operators: MutableState<List<String>> = mutableStateOf(emptyList())

    val difficulty: State<String> get() = _difficulty
    val quizLength: State<String> get() = _quizLength
    val operators: State<List<String>> get() = _operators

    fun setDifficulty(selectedDifficulty: String) {
        _difficulty.value = selectedDifficulty
    }

    fun setQuizLength(questionCount: String) {
        _quizLength.value = questionCount
    }

    fun setOperators(operators : List<String>){
        _operators.value = operators
    }

}