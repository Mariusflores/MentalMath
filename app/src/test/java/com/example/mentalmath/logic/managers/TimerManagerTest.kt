package com.example.mentalmath.logic.managers

import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.concurrent.timer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource

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
    /**
     * Stopwatch tests
     * */
    @Test
    fun stopwatch_accumulatesCorrectTime() = runTest{
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this)
        timer.startStopwatch()
        advanceTimeBy(150)
        timer.stopTimer()
        assertTrue(
            "${timer.elapsedTime.first()} should be less than ${Duration.ZERO}",
            timer.elapsedTime.first() > Duration.ZERO)
    }
    @Test
    fun startStopwatch_multipleCalls_onlyStartsOnce() = runTest{
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this)
        timer.startStopwatch()
        timer.startStopwatch()
        advanceTimeBy(100)
        timer.stopTimer()

        val elapsed = timer.elapsedTime.first()
        val d: Duration = 200.milliseconds
        advanceTimeBy(100)
        assertTrue(
            "Elapsed: $elapsed should be less than $d",
            elapsed < d)
    }

    @Test
    fun resetTimer_clearsAccumulatedTime() = runTest{
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this)

        timer.startStopwatch()
        advanceTimeBy(200)
        timer.resetTimer()
        assertTrue(
            "Elapsed: ${timer.elapsedTime.first()} should be reset to ${Duration.ZERO}",
            timer.elapsedTime.first() == Duration.ZERO)
    }

    @Test
    fun pauseTimer_stopsWithoutResetting() = runTest {
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this)
        timer.startStopwatch()

        advanceTimeBy(200)
        timer.pauseStopwatch()
        val elapsedAfterPause = timer.elapsedTime.first()
        advanceTimeBy(100)
        assertTrue(
            "Elapsed: ${timer.elapsedTime.first()} should equal elapsed after pausing: $elapsedAfterPause",
            timer.elapsedTime.first() == elapsedAfterPause)
    }

    @Test
    fun dispose_cancelsTimerScope() = runTest {
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = backgroundScope)

        timer.startStopwatch()
        advanceTimeBy(50)
        timer.dispose()

        advanceTimeBy(100)
        assertTrue(timer.elapsedTime.first() > Duration.ZERO)
    }

    @Test
    fun resumeTimer_continuesFromPausedTime() = runTest {
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this)
        timer.startStopwatch()

        advanceTimeBy(100)
        timer.pauseStopwatch()
        val pausedTime = timer.elapsedTime.first()
        timer.startStopwatch()
        advanceTimeBy(100)
        timer.stopTimer()
        val resumedTime = timer.elapsedTime.first()

        assertTrue(
            "time after resuming: $resumedTime should be higher than time after pausing: $pausedTime",
            resumedTime > pausedTime)
    }

    /**
     * Countdown Tests
     * */

    @Test
    fun countdown_StartsCountDown()= runTest{
        val fakeTimeSource = TestTimeSource()

        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this, timeSource = fakeTimeSource)
        val timeLimit = 10.seconds
        timer.startCountDown(timeLimit)

        fakeTimeSource += 5.seconds
        runCurrent()
        timer.stopTimer()

        val remaining = timer.elapsedTime.first()

        assertTrue(
            "ElapsedTime should be higher than ${Duration.ZERO} and less than $timeLimit",
            remaining in Duration.ZERO..timeLimit, )
    }

    @Test
    fun countdown_StopsAtZero() = runTest {
        val fakeTimeSource = TestTimeSource()
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this, timeSource = fakeTimeSource)
        val timeLimit = 3.seconds

        timer.startCountDown(timeLimit)
        fakeTimeSource += 3.seconds
        runCurrent()

        val remainingTime = timer.elapsedTime.first()

        // Accept tiny margin due to coroutine delay resolution
        assertTrue(
            "Expected time close to zero, but was: $remainingTime",
            remainingTime <= 50.milliseconds
        )
    }

    @Test
    fun countdown_multipleCalls_OnlyStartsOnce() = runTest{

        val fakeTimeSource = TestTimeSource()
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this, timeSource = fakeTimeSource)
        val timeLimit = 200.milliseconds

        timer.startCountDown(timeLimit)
        timer.startCountDown(timeLimit)
        fakeTimeSource += 100.milliseconds
        runCurrent()

        timer.stopTimer()

        val elapsed = timer.elapsedTime.first()
        val d: Duration = 200.milliseconds
        fakeTimeSource += 100.milliseconds
        assertTrue(
            "Elapsed time: $elapsed should not start two coroutines ",
            elapsed < d)
    }
    @Test
    fun countdown_multipleCalls_doesNotRestart() = runTest{

        val fakeTimeSource = TestTimeSource()
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this, timeSource = fakeTimeSource)
        val timeLimit = 200.milliseconds

        timer.startCountDown(timeLimit)
        fakeTimeSource += 100.milliseconds
        runCurrent()
        timer.startCountDown(timeLimit)


        timer.stopTimer()

        val elapsed = timer.elapsedTime.first()
        assertTrue(
            "Should not start new countdown state when already running",
            elapsed < timeLimit)
    }

    @Test
    fun countdown_stopsCountdown() = runTest{

        val fakeTimeSource = TestTimeSource()
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this, timeSource = fakeTimeSource)
        val timeLimit = 200.milliseconds

        timer.startCountDown(timeLimit)
        fakeTimeSource += 100.milliseconds
        runCurrent()

        timer.stopTimer()

        val elapsed = timer.elapsedTime.first()
        val d: Duration = 200.milliseconds
        fakeTimeSource += 100.milliseconds
        assertTrue(
            "Timer should not continue after stopping",
            elapsed < d)
    }
    @Test
    fun resumeCountdown_continuesFromPausedTime() = runTest {
        val fakeTimeSource = TestTimeSource()
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this, timeSource = fakeTimeSource)
        val timeLimit = 1.seconds

        timer.startCountDown(timeLimit)
        fakeTimeSource += 500.milliseconds
        runCurrent()
        timer.stopTimer()

        val midTime = timer.elapsedTime.first()
        assertTrue(
            "Time after pausing: $midTime should be between 400-600ms",
            midTime in 400.milliseconds..600.milliseconds)

        timer.resumeCountdown()
        fakeTimeSource += 400.milliseconds
        runCurrent()

        timer.stopTimer()
        val finalTime = timer.elapsedTime.first()
        assertTrue(
            "final time: $finalTime should be between 0-200 ms",
            finalTime in 0.milliseconds..200.milliseconds)

    }
    @Test
    fun countdown_resetTimerResets() = runTest{

        val fakeTimeSource = TestTimeSource()
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = this, timeSource = fakeTimeSource)
        val timeLimit = 200.milliseconds

        timer.startCountDown(timeLimit)
        fakeTimeSource += 100.milliseconds
        runCurrent()

        timer.stopTimer()

        timer.resetTimer()

        assertTrue(
            "Elapsed: ${timer.elapsedTime.first()} should reset to ${Duration.ZERO}",
            timer.elapsedTime.first() == Duration.ZERO)
    }

    @Test
    fun countdown_disposeCancelsTimerScope() = runTest{
        val fakeTimeSource = TestTimeSource()
        val timer = TimerManager(dispatcher = testDispatcher, timerScope = backgroundScope, timeSource = fakeTimeSource)
        val timeLimit = 200.milliseconds

        timer.startCountDown(timeLimit)
        fakeTimeSource += 100.milliseconds
        runCurrent()


        timer.dispose()
        val disposedTime = timer.elapsedTime.first()

        fakeTimeSource += 50.milliseconds

        assertTrue(
            "Should cancel count down timer scope, and not allow further advancing",
            timer.elapsedTime.first() == disposedTime)
    }



}