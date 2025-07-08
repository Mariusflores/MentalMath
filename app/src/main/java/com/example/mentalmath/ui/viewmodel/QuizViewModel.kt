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
import com.example.mentalmath.logic.models.quiz.GameState
import com.example.mentalmath.logic.models.quiz.ScoreCard
import com.example.mentalmath.logic.models.quiz.TimerType
import com.example.mentalmath.logic.utils.GameStateParser
import com.example.mentalmath.logic.utils.ScoreCardParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.TimeSource


class QuizViewModel(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    timerScope: CoroutineScope = CoroutineScope(dispatcher + SupervisorJob()),
    timeSource: TimeSource = TimeSource.Monotonic,
    private val enableTimer: Boolean = true

) : ViewModel() {

    private val progressionManager: QuizProgressionManager = QuizProgressionManager()
    private val timerManager: TimerManager = TimerManager(dispatcher, timerScope, timeSource)
    private lateinit var handler: GameModeHandler


    var modeConfig: ModeConfiguration = ModeConfiguration(
        Difficulty.EASY, Operator.entries.toTypedArray(), 0,
        GameMode.Casual
    )

    private val _elapsedTime = mutableStateOf(Duration.ZERO)
    val elapsedTime: State<Duration> get() = _elapsedTime

    init {
        viewModelScope.launch {
            timerManager.elapsedTime.collect { time ->
                _elapsedTime.value = time

                if (::handler.isInitialized) {
                    if (handler.timerType() == TimerType.COUNTDOWN) {
                        _gameState.value = handler.getGameState(elapsedTime.value)

                    }
                }
            }
        }
    }

    // Private mutable variables
    private val _answer = mutableStateOf("")
    private val _inputError = mutableStateOf("")
    private val _gameState = mutableStateOf<GameState?>(null)
    private val _scoreCard: MutableState<ScoreCard> =
        mutableStateOf(ScoreCard.Casual(0, 0, 0.0, Duration.ZERO))

    private var _currentProblem: MutableState<MathProblem?>  = mutableStateOf(null)
    var lastAnswerCorrect by mutableStateOf(true)


    //Public read-only variables
    //State for compose reactivity
    val answer: State<String> get() = _answer
    val inputError: State<String> get() = _inputError

    val currentProblem: State<MathProblem?> get() = requireNotNull(_currentProblem)

    val gameStateIndex: Int get() = GameStateParser.getIndexProperty(gameState)
    val gameStateTotal: Int get() = GameStateParser.getTotalProperty(gameState)
    val isGameFinished: Boolean get() = GameStateParser.getIsFinishedProperty(gameState)

    val survivalLives: Int get() = GameStateParser.getLivesProperty(gameState)
    val survivalMistakes: Int get() = GameStateParser.getMistakesProperty(gameState)


    // No need for reactivity
    val scoreCardCorrect: Int get() = ScoreCardParser.getCorrectProperty(_scoreCard.value)
    val scoreCardTotal: Int get() = ScoreCardParser.getTotalProperty(_scoreCard.value)
    val scoreCardTime: Duration get() = ScoreCardParser.getTimeElapsedOrRemaining(_scoreCard.value)
    val scoreCardAccuracy: Double get() = ScoreCardParser.getAccuracyProperty(_scoreCard.value)

    val gameState get() = requireNotNull(_gameState.value)
    val gameMode get() = modeConfig.gameMode



    fun startQuiz(modeConfiguration: ModeConfiguration) {
        lastAnswerCorrect = true
        modeConfig = modeConfiguration
        handler = GameModeHandlerFactory.create(modeConfig)

        handler.startGame(modeConfig)
        _currentProblem.value = handler.getNextProblem(modeConfiguration)

        initiateTimer()

        _gameState.value = handler.getGameState(null)
        resetInput()
    }

    /**
     * Core Functions
     * */
    fun onSubmitClick() {
        val userAnswer = answer.value.toIntOrNull()
        if (userAnswer == null) {
            _inputError.value = progressionManager.returnInvalidInputString()
            return
        }
        // Make sure current problem is not null

        verifyCorrect(userAnswer, currentProblem.value!!)


        _gameState.value = handler.getGameState(elapsedTime.value)
        if(!isGameFinished){
            _currentProblem.value = handler.getNextProblem(modeConfig)

        }

        _answer.value = progressionManager.resetAnswer()

        endOnQuizFinished()
    }

    fun onEndClick() {
        if (isGameFinished) return
        handler.endGameEarly()
        _gameState.value = handler.getGameState()
        fetchResults()
    }

    /**
     * Helper Functions
     * */
    private fun fetchResults() {
        val elapsed = finalizeTimerIfNeeded()

        _scoreCard.value = progressionManager.getScoreCardFromGameState(gameState, elapsed)
    }

    private fun endOnQuizFinished() {
        if (isGameFinished) {
            fetchResults()
        }
    }

    private fun verifyCorrect(
        userAnswer: Int,
        problem: MathProblem
    ) {
        val answerCorrect = progressionManager.checkAnswer(userAnswer, problem.correctAnswer)
        submitToHandler(answerCorrect)
        lastAnswerCorrect = answerCorrect
    }

    /**
     * Timer
     * */
    private fun submitToHandler(answerCorrect: Boolean) {
        handler.onAnswerSubmitted(answerCorrect)
    }

    private fun initiateTimer() {
        when (handler.timerType()) {
            TimerType.STOPWATCH -> if (enableTimer) timerManager.startStopwatch()
            TimerType.COUNTDOWN -> if (enableTimer) timerManager.startCountDown(handler.timeLimit())
            TimerType.NONE -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerManager.dispose()
    }

    private fun finalizeTimerIfNeeded(): Duration {
        val elapsed = when (handler.timerType()) {
            TimerType.STOPWATCH -> {
                if (_elapsedTime.value != Duration.ZERO) {
                    timerManager.stopTimer()
                    val time = timerManager.elapsedTime.value
                    timerManager.resetTimer()
                    time
                } else {
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

    // Reset
    private fun resetInput() {
        _answer.value = ""
        _inputError.value = ""
    }

    //Setter
    fun setAnswer(value: String) {
        _answer.value = value
    }


}