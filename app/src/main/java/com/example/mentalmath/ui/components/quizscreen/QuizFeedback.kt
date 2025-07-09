package com.example.mentalmath.ui.components.quizscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mentalmath.logic.models.quiz.ProblemMode
import com.example.mentalmath.ui.viewmodel.QuizViewModel
import com.example.mentalmath.ui.components.quizscreen.feedback.ProgressBar
import com.example.mentalmath.ui.components.quizscreen.feedback.GameSpecificCorner



@Composable
fun QuizFeedBack(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {

    Row (
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        if (viewModel.gameMode.problemMode == ProblemMode.FINITE){
            ProgressBar(viewModel, modifier)

            Spacer(modifier = Modifier.Companion.width(16.dp))
        }
        GameSpecificCorner(viewModel)


    }
}