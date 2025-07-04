package com.example.mentalmath.logic.managers

import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class TimerManagerTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun timer_accumulatesCorrectTime() = runTest{
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this)
        timer.startTimer()
        advanceTimeBy(150)
        timer.stopTimer()
        assertTrue(timer.elapsedTime.first() > Duration.ZERO)
    }
    @Test
    fun startTimer_multipleCalls_onlyStartsOnce() = runTest{
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this)
        timer.startTimer()
        timer.startTimer()
        advanceTimeBy(100)
        timer.stopTimer()

        val elapsed = timer.elapsedTime.first()
        val d: Duration = 200.milliseconds
        advanceTimeBy(100)
        assertTrue(elapsed < d)
    }

    @Test
    fun resetTimer_clearsAccumulatedTime() = runTest{
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this)

        timer.startTimer()
        advanceTimeBy(200)
        timer.resetTimer()
        assertTrue(timer.elapsedTime.first() == Duration.ZERO)
    }

    @Test
    fun pauseTimer_stopsWithoutResetting() = runTest {
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this)
        timer.startTimer()

        advanceTimeBy(200)
        timer.pauseTimer()
        val elapsedAfterPause = timer.elapsedTime.first()
        advanceTimeBy(100)
        assertTrue(timer.elapsedTime.first() == elapsedAfterPause)
    }

    @Test
    fun dispose_cancelsTimerScope() = runTest {
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = backgroundScope)

        timer.startTimer()
        advanceTimeBy(50)
        timer.dispose()

        advanceTimeBy(100)
        assertTrue(timer.elapsedTime.first() > Duration.ZERO)
    }

    @Test
    fun resumeTimer_continuesFromPausedTime() = runTest {
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this)
        timer.startTimer()

        advanceTimeBy(100)
        timer.pauseTimer()
        val pausedTime = timer.elapsedTime.first()
        timer.startTimer()
        advanceTimeBy(100)
        timer.stopTimer()
        val resumedTime = timer.elapsedTime.first()

        assertTrue(resumedTime > pausedTime)
    }

}