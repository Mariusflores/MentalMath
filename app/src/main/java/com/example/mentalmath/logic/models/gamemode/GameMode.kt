package com.example.mentalmath.logic.models.gamemode

import com.example.mentalmath.logic.models.quiz.ProblemMode

sealed class GameMode(
    val displayName: String,
    val problemMode: ProblemMode
) {

    object Casual: GameMode("Casual", ProblemMode.FINITE)
    object TimeAttack: GameMode("Time Attack", ProblemMode.FINITE)
    object Survival: GameMode("Survival", ProblemMode.INFINITE)
    object Practice: GameMode("Practice", ProblemMode.INFINITE)
}