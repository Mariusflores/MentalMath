package com.example.mentalmath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mentalmath.ui.components.DropdownTemplate
import com.example.mentalmath.ui.viewmodel.QuizViewModel

@Composable
fun QuizSelectorScreen(
    navController: NavController,
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    var selectedDifficulty by remember { mutableStateOf(viewModel.difficulty.value) }
    val difficultyList = listOf("Easy", "Medium", "Hard")
    val difficultyLabel = "Select Difficulty"
    var quizLength by remember { mutableStateOf(viewModel.quizLength.value) }
    val countList = listOf("5", "10", "15", "20")
    val countLabel = "No. Questions"

    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DropdownTemplate(
                selectedDifficulty, difficultyLabel, difficultyList,
                onSelected = { selectedDifficulty = it },
                modifier = Modifier.weight(0.6f)
            )
            Spacer(modifier = modifier.width(10.dp))
            DropdownTemplate(
                quizLength, countLabel, countList,
                onSelected = { quizLength = it },
                modifier = Modifier.weight(0.4f)

            )
        }


        Spacer(modifier = modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.setDifficulty(selectedDifficulty)
                viewModel.setQuizLength(quizLength)
                viewModel.startQuiz()
                navController.navigate("quiz")
            }
        ) {
            Text("Start Quiz")
        }
    }
}