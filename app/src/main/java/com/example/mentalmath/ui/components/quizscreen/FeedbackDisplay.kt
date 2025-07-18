package com.example.mentalmath.ui.components.quizscreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FeedbackDisplay(feedback: String) {
    if (feedback.isNotEmpty()) {
        Text(feedback)
    }
}