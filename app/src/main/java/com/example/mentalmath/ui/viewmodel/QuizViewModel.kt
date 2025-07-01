package com.example.mentalmath.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mentalmath.logic.managers.QuizManager
import com.example.mentalmath.logic.models.MathProblem
import com.example.mentalmath.logic.models.QuizState


class QuizViewModel : ViewModel() {

    private val quizManager: QuizManager = QuizManager()

    // Private mutable variables
    private val _quiz: MutableState<List<MathProblem>> = mutableStateOf(emptyList())
    private val _score = mutableStateOf(0)
    private val _answer = mutableStateOf("")
    private val _inputError = mutableStateOf("")
    private val _quizState = mutableStateOf(QuizState(0, false))
    private val _difficulty = mutableStateOf("Easy")


    var lastAnswerCorrect by mutableStateOf<Boolean?>(null)


    //Public read-only variables
    //State for compose reactivity
    val score: State<Int> get() = _score
    val answer: State<String> get() = _answer
    val inputError: State<String> get() = _inputError
    val difficulty: State<String> get() = _difficulty

    // No need for reactivity
    val quizIndex: Int get() = _quizState.value.quizIndex
    val isQuizFinished: Boolean get() = _quizState.value.quizFinished
    val quiz: List<MathProblem> get() = _quiz.value
    val currentProblem: MathProblem?
        get() = if (quiz.isNotEmpty() && quizIndex in quiz.indices) quiz[quizIndex] else null



    fun onSubmitClick() {
        val userAnswer = answer.value.toIntOrNull()
        if(userAnswer == null){
            _inputError.value = "Please enter a valid number."
            return
        }
        // Make sure current problem is not null
        val problem = currentProblem ?: return

        if(quizManager.checkAnswer(userAnswer, problem.correctAnswer)){
            _score.value++
            lastAnswerCorrect = true
        }else{
            lastAnswerCorrect = false

        }
        _quizState.value = quizManager.progressQuiz(quizIndex, quiz.size)
        _answer.value = quizManager.resetAnswer()
    }
    fun onEndClick() {
        _quizState.value = quizManager.endQuiz(_quizState.value.quizIndex)
    }

    fun startQuiz(){
        _quiz.value = quizManager.getQuizByDifficulty(difficulty.value)
        _quizState.value = QuizState(0, false)
        _score.value = 0
        _answer.value = ""
        _inputError.value = ""

    }

    fun setDifficulty(selectedDifficulty: String){
        _difficulty.value = selectedDifficulty
    }

    //Setter
    fun setAnswer(value: String){
        _answer.value = value
    }


}