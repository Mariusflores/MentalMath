package com.example.mentalmath.logic.managers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class TimerManager(
    private val dispatcher: CoroutineDispatcher,
    private val timerScope: CoroutineScope = CoroutineScope(dispatcher + SupervisorJob()),
    private val timeSource: TimeSource = TimeSource.Monotonic
) {
    private var isRunning = false

    //Stopwatch variables
    private var startMark: TimeMark? = null
    private var accumulated = Duration.ZERO
    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime: StateFlow<Duration> get() = _elapsedTime

    // Countdown variables
    private var isCountdown = false
    private var timeLimit = Duration.ZERO


    fun startStopwatch() {
        if (isRunning) return

        isRunning = true
        startMark = timeSource.markNow()

        launchStopwatchLoop()
    }

    fun stopTimer() {
        if (!isRunning || startMark == null) return

        val elapsed = stopwatchElapsedSinceStart()
        if (isCountdown) {
            timeLimit -= elapsed
        } else {
            accumulated += elapsed

        }
        isRunning = false
    }

    fun pauseStopwatch() = stopTimer()
    fun resetTimer() {
        stopTimer()
        accumulated = Duration.ZERO
        _elapsedTime.value = Duration.ZERO
    }

    fun dispose() = timerScope.cancel()

    private fun launchStopwatchLoop() = timerScope.launch {
        while (isRunning) {
            _elapsedTime.value = accumulated + stopwatchElapsedSinceStart()
            delay(50)
        }
    }

    private fun stopwatchElapsedSinceStart(): Duration = startMark?.elapsedNow() ?: Duration.ZERO

    fun startCountDown(timeLimit: Duration?) {
        if (isRunning || timeLimit == null) return


        isRunning = true
        isCountdown = true
        this.timeLimit = timeLimit
        startMark = timeSource.markNow()

        launchCountdownLoop()

    }

     fun resumeCountdown(){
        if(timeLimit == Duration.ZERO || !isCountdown) return

        isRunning = true
        startMark = timeSource.markNow()

         launchCountdownLoop()
    }

    private fun launchCountdownLoop() = timerScope.launch {
        while (isRunning) {
            val elapsed = stopwatchElapsedSinceStart()
            val remaining = timeLimit - elapsed

            if (remaining <= Duration.ZERO) {
                _elapsedTime.value = Duration.ZERO
                isRunning = false
                break
            } else {
                _elapsedTime.value = remaining
            }

            delay(50)
        }
    }

}