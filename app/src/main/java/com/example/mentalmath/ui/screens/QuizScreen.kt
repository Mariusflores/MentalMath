package com.example.mentalmath.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mentalmath.ui.components.QuizScreen.ButtonRow
import com.example.mentalmath.ui.components.QuizScreen.FeedbackDisplay
import com.example.mentalmath.ui.components.QuizScreen.GameSpecificCorner
import com.example.mentalmath.ui.components.QuizScreen.InputBox
import com.example.mentalmath.ui.components.QuizScreen.ProblemDisplay
import com.example.mentalmath.ui.viewmodel.QuizViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier

) {

    val progress = (viewModel.gameStateIndex + 1).toFloat() / (viewModel.quiz.size + 1)
    val color = when (viewModel.lastAnswerCorrect) {

        true -> Color.Green
        false -> Color.Red
    }


    LaunchedEffect(viewModel.isGameFinished) {
        if (viewModel.isGameFinished) {
            navController.navigate("score")
        }
    }

    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize()
    ) {
        Row {
            LinearProgressIndicator(

                progress = { progress },
                modifier = modifier.fillMaxWidth(0.7f),
                color = color
            )

            Spacer(modifier = Modifier.width(16.dp))

            GameSpecificCorner(viewModel)


        }


        ProblemDisplay(requireNotNull(viewModel.currentProblem.value))




        InputBox(
            answer = viewModel.answer.value,
            onAnswerChange = { viewModel.setAnswer(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ButtonRow(
            onSubmitClick = { viewModel.onSubmitClick() },
            onEndClick = { viewModel.onEndClick() })

        FeedbackDisplay(viewModel.inputError.value)


    }
}

