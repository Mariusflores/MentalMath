package com.example.mentalmath.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mentalmath.logic.models.MathProblem
import com.example.mentalmath.logic.generators.MathQuizGenerator
import com.example.mentalmath.logic.managers.QuizManager
import com.example.mentalmath.logic.models.QuizState


class QuizViewModel : ViewModel() {

    // Private mutable variables
    private val _quiz = mutableStateOf( MathQuizGenerator.generateRandomOperatorQuiz())
    private val _score = mutableStateOf(0)
    private val _answer = mutableStateOf("")
    private val _inputError = mutableStateOf("")
    private val _quizState = mutableStateOf(QuizState(0, false))

    private val quizManager: QuizManager = QuizManager()

    var lastAnswerCorrect by mutableStateOf<Boolean?>(null)


    //Public read-only variables
    //State for compose reactivity
    val score: State<Int> get() = _score
    val answer: State<String> get() = _answer
    val inputError: State<String> get() = _inputError

    // No need for reactivity
    val quizIndex: Int get() = _quizState.value.quizIndex
    val isQuizFinished: Boolean get() = _quizState.value.quizFinished
    val quiz: List<MathProblem> get() = _quiz.value
    val currentProblem: MathProblem get() = quiz[quizIndex]


    fun onSubmitClick() {
        val userAnswer = answer.value.toIntOrNull()
        if(userAnswer == null){
            _inputError.value = "Please enter a valid number."
            return
        }

        if(quizManager.checkAnswer(userAnswer, currentProblem.correctAnswer)){
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

    fun resetQuiz(){
        _quiz.value = MathQuizGenerator.generateRandomOperatorQuiz()
        _quizState.value = QuizState(0, false)
        _score.value = 0
        _answer.value = ""
        _inputError.value = ""

    }

    //Setter
    fun setAnswer(value: String){
        _answer.value = value
    }


}