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
    private var startMark: TimeMark? = null
    private var accumulated = Duration.ZERO
    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime: StateFlow<Duration> get() = _elapsedTime

     fun startStopwatch() {
        if (isRunning) return

        isRunning = true
        startMark = timeSource.markNow()

         launchStopwatchLoop()
    }

    fun stopStopwatch() {
        if (!isRunning || startMark == null) return
        accumulated += stopwatchElapsedSinceStart()
        isRunning = false
    }
    fun pauseStopwatch() = stopStopwatch()
    fun resetTimer() {
        stopStopwatch()
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
    fun startCountDown() {
        TODO("Not yet implemented")
    }
}