package com.example.mentalmath.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mentalmath.logic.managers.QuizManager
import com.example.mentalmath.logic.models.MathProblem
import com.example.mentalmath.logic.models.QuizState
import com.example.mentalmath.logic.models.ScoreCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.TimeSource


class QuizViewModel : ViewModel() {

    private val quizManager: QuizManager = QuizManager()

    // Private mutable variables
    private val _quiz: MutableState<List<MathProblem>> = mutableStateOf(emptyList())
    private val _score = mutableStateOf(0)
    private val _answer = mutableStateOf("")
    private val _inputError = mutableStateOf("")
    private val _quizState = mutableStateOf(QuizState(0, false))
    private val _difficulty = mutableStateOf("Easy")
    private val _quizLength = mutableStateOf("10")
    private val _scoreCard = mutableStateOf(ScoreCard(0, 0, 0.0, Duration.ZERO))

    var lastAnswerCorrect by mutableStateOf<Boolean?>(null)


    //Public read-only variables
    //State for compose reactivity
    val score: State<Int> get() = _score
    val answer: State<String> get() = _answer
    val inputError: State<String> get() = _inputError
    val difficulty: State<String> get() = _difficulty
    val quizLength: State<String> get() = _quizLength
    val scoreCard: State<ScoreCard> get() = _scoreCard

    // No need for reactivity
    val quizIndex: Int get() = _quizState.value.quizIndex
    val isQuizFinished: Boolean get() = _quizState.value.quizFinished
    val quiz: List<MathProblem> get() = _quiz.value
    val currentProblem: MathProblem?
        get() = if (quiz.isNotEmpty() && quizIndex in quiz.indices) quiz[quizIndex] else null

    //Timer variables
    private val _elapsedTime = mutableStateOf(Duration.ZERO)
    private var isTimerRunning by mutableStateOf(false)
    private var startMark by mutableStateOf(TimeSource.Monotonic.markNow())
    private var accumulated = Duration.ZERO
    val elapsedTime: State<Duration> get() = _elapsedTime


    fun startQuiz() {
        startTimer()
        _quiz.value = quizManager.getQuizByDifficulty(difficulty.value, quizLength.value)
        _quizState.value = QuizState(0, false)
        _score.value = 0
        _answer.value = ""
        _inputError.value = ""
    }

    private fun startTimer() {
        if (isTimerRunning) return

        isTimerRunning = true
        startMark = TimeSource.Monotonic.markNow()

        viewModelScope.launch {
            while (isTimerRunning) {
                _elapsedTime.value = accumulated + startMark.elapsedNow()
                delay(50)
            }
        }
    }

    private fun stopTimer() {
        if (!isTimerRunning) return
        accumulated += startMark.elapsedNow()
        isTimerRunning = false
    }

    fun resetTimer() {
        isTimerRunning = false
        accumulated = Duration.ZERO
        _elapsedTime.value = Duration.ZERO
    }

    fun onSubmitClick() {
        val userAnswer = answer.value.toIntOrNull()
        if (userAnswer == null) {
            _inputError.value = "Please enter a valid number."
            return
        }
        // Make sure current problem is not null
        val problem = currentProblem ?: return

        if (quizManager.checkAnswer(userAnswer, problem.correctAnswer)) {
            _score.value++; lastAnswerCorrect = true
        } else {
            lastAnswerCorrect = false

        }
        _quizState.value = quizManager.progressQuiz(quizIndex, quiz.size)
        _answer.value = quizManager.resetAnswer()
        if (quizManager.verifyQuizFinished(_quizState.value)) {
            stopTimer()
            _scoreCard.value = quizManager.getScoreCard(score.value, quiz.size, elapsedTime.value)
            resetTimer()
        }

    }

    fun onEndClick() {
        _quizState.value = quizManager.endQuiz(_quizState.value.quizIndex)
        stopTimer()
        _scoreCard.value = quizManager.getScoreCard(score.value, quiz.size, elapsedTime.value)
        resetTimer()
    }

    fun setDifficulty(selectedDifficulty: String) {
        _difficulty.value = selectedDifficulty
    }

    fun setQuizLength(questionCount: String) {
        _quizLength.value = questionCount
    }

    //Setter
    fun setAnswer(value: String) {
        _answer.value = value
    }


}