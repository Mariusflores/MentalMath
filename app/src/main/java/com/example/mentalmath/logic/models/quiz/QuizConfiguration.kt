package com.example.mentalmath.logic.models.quiz

import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration

data class QuizConfiguration (
    val difficulty: String,
    val quizLength: String,
    val operators: List<String>,
    val gameMode: GameMode
)
{
    fun toModeConfiguration(): ModeConfiguration{
        val difficulty = Difficulty.DifficultyConverter.toDifficulty(difficulty)
        val operators = Operator.OperatorConverter.toOperatorArray(operators)
        val quizLength = quizLength.toIntOrNull()
        val gameMode = gameMode

        return ModeConfiguration(difficulty, operators, quizLength, gameMode)
    }
}