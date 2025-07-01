package com.example.mentalmath.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mentalmath.logic.generators.MathProblemGenerator
import com.example.mentalmath.ui.components.ButtonRow
import com.example.mentalmath.ui.components.FeedbackDisplay
import com.example.mentalmath.ui.components.InputBox
import com.example.mentalmath.ui.components.ProblemDisplay
import com.example.mentalmath.ui.theme.MentalMathTheme
import com.example.mentalmath.ui.viewmodel.QuizViewModel


@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier

)
 {
    val progress = (viewModel.quizIndex + 1).toFloat() / viewModel.quiz.size
     val color = when (viewModel.lastAnswerCorrect){
         true -> Color.Green
         false -> Color.Red
         null -> Color.Gray
     }

     LaunchedEffect(viewModel.isQuizFinished) {
         if (viewModel.isQuizFinished) {
             navController.navigate("score")
         }
     }

    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize()
    ) {

        LinearProgressIndicator(

            progress = {progress},
            modifier = modifier.fillMaxWidth(0.7f),
            color = color
        )

        ProblemDisplay(viewModel.currentProblem)

        InputBox(
            answer = viewModel.answer.value,
            onAnswerChange= {viewModel.setAnswer(it)}
        )

        Spacer(modifier = Modifier.height(16.dp))

        ButtonRow(
            onSubmitClick = {viewModel.onSubmitClick()},
            onEndClick = {viewModel.onEndClick() })

        FeedbackDisplay(viewModel.inputError.value)


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


