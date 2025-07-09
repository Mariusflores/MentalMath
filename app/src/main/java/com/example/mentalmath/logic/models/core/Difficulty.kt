package com.example.mentalmath.logic.models.core

enum class Difficulty {
    EASY, MEDIUM, HARD;

    override fun toString(): String{
        return when(this){
            EASY -> "Easy"
            MEDIUM -> "Medium"
            HARD -> "Hard"
        }
    }

    object DifficultyConverter{
        fun toDifficulty(difficulty: String): Difficulty{
            return when(difficulty.lowercase()){
                "easy" -> EASY
                "medium" -> MEDIUM
                "hard" -> HARD
                else -> throw IllegalArgumentException("Unknown Difficulty $difficulty")
            }
        }
    }

}
