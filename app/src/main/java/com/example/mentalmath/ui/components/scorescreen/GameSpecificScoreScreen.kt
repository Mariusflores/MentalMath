package com.example.mentalmath.ui.components.scorescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.ui.viewmodel.QuizViewModel
import kotlin.time.Duration

@Composable
fun GameSpecificScoreScreen(viewModel: QuizViewModel){

    when(viewModel.gameMode){
        is GameMode.Casual -> CasualScoreScreen(viewModel)
        is GameMode.TimeAttack -> TimeAttackScoreScreen(viewModel)
        is GameMode.Survival -> SurvivalScoreScreen(viewModel)
        is GameMode.Practice -> PracticeScoreScreen(viewModel)
    }

}

@SuppressLint("DefaultLocale")
@Composable
fun PracticeScoreScreen(viewModel: QuizViewModel) {
    val scoreString = "You scored ${viewModel.scoreCardCorrect} / ${viewModel.scoreCardTotal} "
    val formatPercentage = String.format(" %.2f", viewModel.scoreCardAccuracy)
    val percentageString = "$formatPercentage%"
    val streakString = "HighestStreak: ${viewModel.scoreCardHighestStreak}"
    Text(
        text = scoreString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = percentageString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = streakString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@SuppressLint("DefaultLocale")
@Composable
fun SurvivalScoreScreen(viewModel: QuizViewModel) {
    val scoreString = "You scored ${viewModel.scoreCardCorrect} / ${viewModel.scoreCardTotal} "
    val formatPercentage = String.format(" %.2f", viewModel.scoreCardAccuracy)
    val percentageString = "$formatPercentage%"
    Text(
        text = scoreString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = percentageString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@SuppressLint("DefaultLocale")
@Composable
fun TimeAttackScoreScreen(viewModel: QuizViewModel) {
    val scoreString = "You scored ${viewModel.scoreCardCorrect} / ${viewModel.scoreCardTotal} "
    val formatPercentage = String.format(" %.2f", viewModel.scoreCardAccuracy)
    val percentageString = "$formatPercentage%"
    val elapsedString = "Time Remaining: ${formatTime(viewModel.scoreCardTime)}"
    Text(
        text = scoreString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = percentageString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = elapsedString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@SuppressLint("DefaultLocale")
@Composable
fun CasualScoreScreen(viewModel: QuizViewModel) {
    val scoreString = "You scored ${viewModel.scoreCardCorrect} / ${viewModel.scoreCardTotal} "
    val formatPercentage = String.format(" %.2f", viewModel.scoreCardAccuracy)
    val percentageString = "$formatPercentage%"
    val elapsedString = "Time Elapsed: ${formatTime(viewModel.scoreCardTime)}"

    Text(
        text = scoreString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = percentageString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = elapsedString,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
}
fun formatTime(duration: Duration): String {
    val totalSeconds = duration.inWholeSeconds
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}