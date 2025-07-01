package com.example.mentalmath.logic.models

enum class Difficulty {
    EASY, MEDIUM, HARD;

    override fun toString(): String{
        return when(this){
            EASY -> "Easy"
            MEDIUM -> "Medium"
            HARD -> "Hard"
        }
    }

    object EnumConverter{
        fun toEnum(difficulty: String): Difficulty{
            return when(difficulty){
                "easy" -> EASY
                "medium" -> MEDIUM
                "hard" -> HARD
                else -> throw IllegalArgumentException("Unknown Difficulty $difficulty")
            }
        }
    }

}
