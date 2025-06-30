package com.example.mentalmath.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
    fun ScoreScreen(
        navController: NavController,
        viewModel: QuizViewModel,
        modifier : Modifier = Modifier

    ){


    val scoreText = "You scored ${viewModel.score} / ${viewModel.quiz.size}"
    Column(
            modifier = modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = scoreText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

        Button(onClick = {
            navController.navigate("landing")
        }) {
            Text("Take me back")
        }
        }

    }