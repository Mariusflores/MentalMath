package com.example.mentalmath.logic.models.gamemode

sealed class GameMode(
    val displayName: String,
) {

    object Casual: GameMode("Casual")
    object TimeAttack: GameMode("Time Attack")
    object Survival: GameMode("Survival")
    object Practice: GameMode("Practice")
}