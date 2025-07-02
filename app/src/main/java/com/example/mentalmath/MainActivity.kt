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
import com.example.mentalmath.ui.screens.QuizScreen
import com.example.mentalmath.ui.screens.LandingScreen
import com.example.mentalmath.ui.screens.QuizSelectorScreen
import com.example.mentalmath.ui.viewmodel.QuizViewModel
import com.example.mentalmath.ui.screens.ScoreScreen
import com.example.mentalmath.ui.theme.MentalMathTheme
import com.example.mentalmath.ui.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MentalMathTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val quizViewModel: QuizViewModel = viewModel()
                    val settingsViewModel: SettingsViewModel = viewModel()
                    NavHost(
                        navController,
                        startDestination = "landing",
                        modifier = Modifier.padding(innerPadding)


                    ) {
                        composable("landing") { LandingScreen(navController) }
                        composable("quiz") { QuizScreen(navController, quizViewModel) }
                        composable("score") { ScoreScreen(navController, quizViewModel) }
                        composable("quiz-selector") { QuizSelectorScreen(navController, settingsViewModel, quizViewModel) }
                    }

                }
            }
        }
    }
}

