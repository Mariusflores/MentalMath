package com.example.mentalmath.ui.components.QuizScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mentalmath.logic.models.core.MathProblem

@Composable
fun ProblemDisplay(problem: MathProblem) {
    Text(
        text = problem.questionText,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold

    )
}