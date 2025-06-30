package com.example.mentalmath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mentalmath.ui.QuizScreen
import com.example.mentalmath.ui.LandingScreen
import com.example.mentalmath.ui.QuizViewModel
import com.example.mentalmath.ui.ScoreScreen
import com.example.mentalmath.ui.theme.MentalMathTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MentalMathTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: QuizViewModel = viewModel()
                    NavHost(
                        navController,
                        startDestination = "landing",
                        modifier = Modifier.padding(innerPadding)


                    ) {
                        composable("landing") { LandingScreen(navController) }
                        composable("quiz") { QuizScreen(navController, viewModel) }
                        composable("score") { ScoreScreen(navController, viewModel) }
                    }

                }
            }
        }
    }
}

