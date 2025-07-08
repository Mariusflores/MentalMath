package com.example.mentalmath.ui.components.QuizScreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InputBox(answer: String, onAnswerChange: (String) -> Unit) {

    OutlinedTextField(
        value = answer,
        onValueChange = { newValue -> onAnswerChange(newValue) },
        label = { Text("Write answer here.") },
        modifier = Modifier.fillMaxWidth()
    )

}