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
import com.example.mentalmath.logic.gamemodes.factory.ScoreCardParser
import com.example.mentalmath.logic.managers.QuizProgressionManager
import com.example.mentalmath.logic.managers.TimerManager
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import com.example.mentalmath.logic.models.quiz.GameState
import com.example.mentalmath.logic.models.quiz.ProblemMode
import com.example.mentalmath.logic.models.quiz.QuizConfiguration
import com.example.mentalmath.logic.models.quiz.ScoreCard
import com.example.mentalmath.logic.models.quiz.TimerType
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
    private lateinit var handler: GameModeHandler


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
    private val _gameState = mutableStateOf<GameState?>(null)
    private val _scoreCard: MutableState<ScoreCard> =
        mutableStateOf(ScoreCard.Casual(0, 0, 0.0, Duration.ZERO))


    var lastAnswerCorrect by mutableStateOf<Boolean?>(null)


    //Public read-only variables
    //State for compose reactivity
    val score: State<Int> get() = _score
    val answer: State<String> get() = _answer
    val inputError: State<String> get() = _inputError

    val scoreCardCorrect: Int = ScoreCardParser.getCorrectProperty(_scoreCard.value)
    val scoreCardTotal: Int = ScoreCardParser.getTotalProperty(_scoreCard.value)
    val scoreCardTime: Duration = ScoreCardParser.getTimeElapsedOrRemaining(_scoreCard.value)
    val scoreCardAccuracy: Double = ScoreCardParser.getAccuracyProperty(_scoreCard.value)
    val gameState: State<GameState?> get() = _gameState


    // No need for reactivity
    val quizIndex: Int get() = when(val state = _gameState.value){
        is GameState.Casual -> state.index
        is GameState.TimeAttack -> state.index
        is GameState.Survival -> state.mistakes
        is GameState.Practice -> state.total
        null -> 0
    }
    val isGameFinished: Boolean get() = when(val state = _gameState.value){
        is GameState.Casual -> state.isFinished
        is GameState.TimeAttack -> state.isFinished
        is GameState.Survival -> state.isFinished
        is GameState.Practice -> state.isFinished
        null -> false
    }
    val quiz: List<MathProblem> get() = _quiz.value
    val currentOrNextProblem: MathProblem?
        get() = if(handler.problemMode()== ProblemMode.FINITE){
            if (quiz.isNotEmpty() && quizIndex in quiz.indices) quiz[quizIndex] else null
        }else{
            handler.getNextProblem(modeConfig)
        }

    fun startQuiz(quizConfiguration: QuizConfiguration) {
        modeConfig = quizConfiguration.toModeConfiguration()
        handler = GameModeHandlerFactory.create(modeConfig)

        _quiz.value = handler.startGame(modeConfig)
        initiateTimer()

        _gameState.value = handler.getGameState(null)
        resetInput()
    }



    fun onSubmitClick() {
        val userAnswer = answer.value.toIntOrNull()
        if (userAnswer == null) {
            _inputError.value = progressionManager.returnInvalidInputString()
            return
        }
        // Make sure current problem is not null
        val problem = currentOrNextProblem ?: return


        incrementScoreIfNeeded(userAnswer, problem)

        _gameState.value = handler.getGameState()
        _answer.value = progressionManager.resetAnswer()

        endOnQuizFinished()
    }
    fun onEndClick() {
        handler.endGameEarly()
        _gameState.value = handler.getGameState()
        fetchResults()
    }

    fun fetchResults(){
        val elapsed = finalizeTimerIfNeeded()

        val safeGameState: GameState = gameState.value ?: GameState.Casual(0,0,0,false)
        _scoreCard.value = progressionManager.getScoreCardFromGameState(safeGameState, elapsed)
    }

    private fun endOnQuizFinished() {
        if(isGameFinished){
            fetchResults()
        }
    }
    private fun incrementScoreIfNeeded(
        userAnswer: Int,
        problem: MathProblem
    ) {
       val answerCorrect = progressionManager.checkAnswer(userAnswer, problem.correctAnswer)
        handler.onAnswerSubmitted(answerCorrect)
        when(modeConfig.gameMode){
            GameMode.Casual, GameMode.TimeAttack -> {
                if (answerCorrect) {
                    _score.value++; lastAnswerCorrect = true
                } else {
                    lastAnswerCorrect = false

                }
            }
            GameMode.Survival, GameMode.Practice -> {
                lastAnswerCorrect = if (answerCorrect) {
                    true
                } else {
                    false

                }
            }
        }
    }
    private fun initiateTimer() {
        when (handler.timerType()) {
            TimerType.STOPWATCH -> if(enableTimer)timerManager.startStopwatch()
            TimerType.COUNTDOWN -> if (enableTimer)timerManager.startCountDown(handler.timeLimit())
            TimerType.NONE -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerManager.dispose()
    }

    private fun finalizeTimerIfNeeded(): Duration {
        val elapsed =  when (handler.timerType()) {
            TimerType.STOPWATCH -> {
                if (_elapsedTime.value != Duration.ZERO){
                    timerManager.stopTimer()
                    val time = timerManager.elapsedTime.value
                    timerManager.resetTimer()
                    time
                }else{
                    Duration.ZERO
                }
            }
            TimerType.COUNTDOWN -> {
                timerManager.stopTimer()
                val time = timerManager.elapsedTime.value
                timerManager.resetTimer()
                time
            }
            TimerType.NONE -> {
                Duration.ZERO
            }
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