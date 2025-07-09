package com.example.mentalmath.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mentalmath.ui.components.quizscreen.ButtonRow
import com.example.mentalmath.ui.components.quizscreen.FeedbackDisplay
import com.example.mentalmath.ui.components.quizscreen.InputBox
import com.example.mentalmath.ui.components.quizscreen.ProblemDisplay
import com.example.mentalmath.ui.components.quizscreen.QuizFeedBack
import com.example.mentalmath.ui.viewmodel.QuizViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier

) {




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
        QuizFeedBack(viewModel)


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

