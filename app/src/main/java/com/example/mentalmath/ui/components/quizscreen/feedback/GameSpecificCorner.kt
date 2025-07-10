package com.example.mentalmath.ui.components.quizscreen.feedback

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.ui.viewmodel.QuizViewModel
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun GameSpecificCorner(
    viewModel: QuizViewModel
) {

    when(viewModel.gameMode){
        GameMode.Casual -> {
            CasualModeCorner(viewModel)
        }
        GameMode.TimeAttack -> {
            TimeAttackModeCorner(viewModel)
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
                contentDescription = "Favorite",
                tint = Color.Red
            )
        }
    }
}
private fun formatTime(duration: Duration): String {
    val totalSeconds = duration.inWholeSeconds
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
@Composable
fun CasualModeCorner(viewModel: QuizViewModel) {
    Row(
    ) {
        Icon(
            imageVector = Icons.Filled.Timer,
            contentDescription = "Timer"
        )

        Text(
            text = formatTime(viewModel.elapsedTime.value)
        )
    }
}

@Composable
fun TimeAttackModeCorner(viewModel: QuizViewModel) {
    val isTimeLow = viewModel.elapsedTime.value < 10.seconds
    val color: Color =
        if (isTimeLow){
            Color.Red
        }else if(viewModel.elapsedTime.value < 30.seconds){
            Color(0xFFF9A825)


        }else MaterialTheme.colorScheme.primary


    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing)
        ),
        label = "scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing)
        ),
        label = "alpha"
    )
    Row(
    ) {
        if(isTimeLow){
            Icon(
                modifier = Modifier.scale(scale).alpha(alpha),
                imageVector = Icons.Filled.HourglassTop,
                contentDescription = "Hourglass",
                tint = color
            )
        }else{
            Icon(
                imageVector = Icons.Filled.HourglassTop,
                contentDescription = "Hourglass",
                tint = color
            )
        }


        Text(
            text = formatTime(viewModel.elapsedTime.value),
            color= color
        )
    }
}
