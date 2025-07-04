package com.example.mentalmath.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mentalmath.logic.gamemodes.GameModeHandler
import com.example.mentalmath.logic.gamemodes.factory.GameModeHandlerFactory
import com.example.mentalmath.logic.managers.QuizProgressionManager
import com.example.mentalmath.logic.managers.TimerManager
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.ProblemMode
import com.example.mentalmath.logic.models.quiz.TimerType
import com.example.mentalmath.logic.models.quiz.QuizConfiguration
import com.example.mentalmath.logic.models.quiz.QuizState
import com.example.mentalmath.logic.models.quiz.ScoreCard
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Duration



class QuizViewModel(
     dispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val enableTimer: Boolean = true

) : ViewModel() {

    private val progressionManager: QuizProgressionManager = QuizProgressionManager()
    private val timerManager: TimerManager = TimerManager(dispatcher)
    var handler: GameModeHandler? = null
    var modeConfig: ModeConfiguration =ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(), 0,
        GameMode.Casual)

    private val _elapsedTime = mutableStateOf(Duration.ZERO)
    val elapsedTime: State<Duration> get() = _elapsedTime

    init {
        viewModelScope.launch {
            timerManager.elapsedTime.collect { time ->
                _elapsedTime.value = time

            }
        }
    }
    // Private mutable variables
    private val _quiz: MutableState<List<MathProblem>> = mutableStateOf(emptyList())
    private val _score = mutableStateOf(0)
    private val _answer = mutableStateOf("")
    private val _inputError = mutableStateOf("")
    private val _quizState = mutableStateOf(QuizState(0, false))
    private val _scoreCard = mutableStateOf(ScoreCard(0, 0, 0.0, Duration.ZERO))

    var lastAnswerCorrect by mutableStateOf<Boolean?>(null)


    //Public read-only variables
    //State for compose reactivity
    val score: State<Int> get() = _score
    val answer: State<String> get() = _answer
    val inputError: State<String> get() = _inputError
    val scoreCard: State<ScoreCard> get() = _scoreCard

    // No need for reactivity
    val quizIndex: Int get() = _quizState.value.quizIndex
    val isQuizFinished: Boolean get() = _quizState.value.quizFinished
    val quiz: List<MathProblem> get() = _quiz.value
    val currentProblem: MathProblem?
        get() = if (quiz.isNotEmpty() && quizIndex in quiz.indices) quiz[quizIndex] else null

    fun startQuiz(quizConfiguration: QuizConfiguration) {
        modeConfig = quizConfiguration.toModeConfiguration()
        handler = GameModeHandlerFactory.create(modeConfig)

        _quiz.value = GameModeHandlerFactory.startQuiz(handler!!, modeConfig)
        initiateTimer()

        _quizState.value = QuizState(0, false)
        resetInput()
    }

    private fun initiateTimer() {
        when (handler!!.timerType()) {
            TimerType.STOPWATCH -> timerManager.startStopwatch()
            TimerType.COUNTDOWN -> timerManager.startCountDown()
            TimerType.NONE -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerManager.dispose()
    }

    fun onSubmitClick() {
        val userAnswer = answer.value.toIntOrNull()
        if (userAnswer == null) {
            _inputError.value = progressionManager.returnInvalidInputString()
            return
        }
        // Make sure current problem is not null
        var problem = currentProblem ?: return

        if(handler!!.problemMode() == ProblemMode.FINITE){
            TODO()
        }



        incrementScore(userAnswer, problem)

        _quizState.value = progressionManager.progressQuiz(quizIndex, quiz.size)
        _answer.value = progressionManager.resetAnswer()

        endOnQuizFinished()
    }
    fun onEndClick() {
        _quizState.value = progressionManager.endQuiz(_quizState.value.quizIndex)
        val elapsed = finalizeTimerIfNeeded()
        _scoreCard.value = progressionManager.getScoreCard(score.value, quiz.size, elapsed)
    }

    private fun endOnQuizFinished() {
        if (progressionManager.verifyQuizFinished(_quizState.value)) {
            val elapsed = finalizeTimerIfNeeded()
            _scoreCard.value = progressionManager.getScoreCard(score.value, quiz.size, elapsed
            )

        }
    }
    private fun incrementScore(
        userAnswer: Int,
        problem: MathProblem
    ) {
        if (progressionManager.checkAnswer(userAnswer, problem.correctAnswer)) {
            _score.value++; lastAnswerCorrect = true
        } else {
            lastAnswerCorrect = false

        }
    }

    private fun finalizeTimerIfNeeded(): Duration {
        val elapsed = if (enableTimer || _elapsedTime.value != Duration.ZERO){
            timerManager.stopStopwatch()
            val time = timerManager.elapsedTime.value
            timerManager.resetTimer()
            time
        }else{
            Duration.ZERO
        }
        return elapsed
    }
    private fun resetInput() {
        _score.value = 0
        _answer.value = ""
        _inputError.value = ""
    }

    //Setters
    fun setAnswer(value: String) {
        _answer.value = value
    }


}