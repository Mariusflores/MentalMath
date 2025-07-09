package com.example.mentalmath.ui.components.quizscreen.feedback

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mentalmath.ui.viewmodel.QuizViewModel

@Composable
fun ProgressBar(
    viewModel: QuizViewModel,
    modifier: Modifier
) {
    val progress = (viewModel.gameStateIndex + 1).toFloat() / (viewModel.gameStateTotal + 1)
    val color = when (viewModel.lastAnswerCorrect) {

        true -> Color.Companion.Green
        false -> Color.Companion.Red
    }

    LinearProgressIndicator(

        progress = { progress },
        modifier = modifier.fillMaxWidth(0.7f),
        color = color
    )
}