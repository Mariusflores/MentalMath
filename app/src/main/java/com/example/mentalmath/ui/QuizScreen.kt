package com.example.mentalmath.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mentalmath.logic.MathProblemGenerator
import com.example.mentalmath.ui.components.ButtonRow
import com.example.mentalmath.ui.components.FeedbackDisplay
import com.example.mentalmath.ui.components.InputBox
import com.example.mentalmath.ui.components.ProblemDisplay
import com.example.mentalmath.ui.theme.MentalMathTheme


@Composable
fun QuizScreen(
    navController: NavController,
    modifier: Modifier = Modifier
)
 {
    val viewModel : QuizViewModel = viewModel()

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
    ) {
        ProblemDisplay(viewModel.problem)

        InputBox(
            answer = viewModel.answer,
            onAnswerChange= {viewModel.answer = it}
        )

        Spacer(modifier = Modifier.height(16.dp))

        ButtonRow(
            viewModel.correctAnswer,
            onSubmitClick = {viewModel.onSubmitClick()},
            onNextProblemClick = {viewModel.onNextProblemClick() })

        FeedbackDisplay(viewModel.feedback)


    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    val sampleProblem = MathProblemGenerator.generateAdditionProblem()
    val sampleAnswer = "42"

    MentalMathTheme {
        Column(modifier = Modifier.padding(24.dp)) {
            ProblemDisplay(sampleProblem)
            InputBox(
                answer = sampleAnswer,
                onAnswerChange = {}
            )
        }
    }
}


