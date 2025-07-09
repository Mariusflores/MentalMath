package com.example.mentalmath.ui.components.quizscreen.feedback

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.ui.viewmodel.QuizViewModel
import kotlin.time.Duration

@Composable
fun GameSpecificCorner(
    viewModel: QuizViewModel
) {
    when(viewModel.gameMode){
        GameMode.Casual -> {
            TimerModeCorner(viewModel, Icons.Filled.Timer, "Timer")
        }
        GameMode.TimeAttack -> {
            TimerModeCorner(viewModel, Icons.Filled.HourglassTop, "Hourglass")
        }
        GameMode.Survival -> {
            SurvivalModeCorner(viewModel)
        }
        GameMode.Practice -> {
            PracticeModeCorner(viewModel)
        }
    }
}

@Composable
fun PracticeModeCorner(viewModel: QuizViewModel) {
    fun getStreakFeedback(streak: Int): String?{
        return when{
            streak > 0 && streak % 3 == 0 -> "🔥 You're on fire!"
            streak > 0 && streak % 2 == 0 -> "💪 Great streak!"
            else -> null
        }
    }
    val streak = viewModel.practiceStreak

    val feedback = getStreakFeedback(streak)

    if (feedback != null){
        Text(
            text = feedback,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun SurvivalModeCorner(
    viewModel: QuizViewModel
) {

    fun getLives(): Int{
        return viewModel.survivalLives - viewModel.survivalMistakes
    }

    Row {
        (1..getLives()).forEach { i ->
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Favorite"
            )
        }
    }
}

@Composable
private fun TimerModeCorner(viewModel: QuizViewModel, vector: ImageVector, description: String) {
    fun formatTime(duration: Duration): String {
        val totalSeconds = duration.inWholeSeconds
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }
    Row(
    ) {
        Icon(
            imageVector = vector,
            contentDescription = description
        )

        Text(
            text = formatTime(viewModel.elapsedTime.value)
        )
    }
}