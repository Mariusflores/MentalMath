package com.example.mentalmath.logic.models.gamemode

import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator

data class ModeConfiguration(
    val difficulty: Difficulty,
    val operators: Array<Operator>,
    val quizLength: Int? = 0,
    val gameMode: GameMode
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ModeConfiguration

        if (quizLength != other.quizLength) return false
        if (difficulty != other.difficulty) return false
        if (!operators.contentEquals(other.operators)) return false
        if (gameMode != other.gameMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = quizLength ?: 0
        result = 31 * result + difficulty.hashCode()
        result = 31 * result + operators.contentHashCode()
        result = 31 * result + gameMode.hashCode()
        return result
    }
}
