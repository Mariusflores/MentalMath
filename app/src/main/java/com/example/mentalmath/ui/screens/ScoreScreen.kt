package com.example.mentalmath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.ui.components.scorescreen.GameSpecificScoreScreen
import com.example.mentalmath.ui.viewmodel.QuizViewModel
import kotlin.time.Duration

@Composable
fun ScoreScreen(
    navController: NavController,
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier

) {





    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GameSpecificScoreScreen(viewModel)

        Button(onClick = {
            navController.navigate("landing")
        }) {
            Text("Take me back")
        }
    }

}