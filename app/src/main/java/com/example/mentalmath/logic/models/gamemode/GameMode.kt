package com.example.mentalmath.logic.models.gamemode

import com.example.mentalmath.logic.models.quiz.ProblemMode

private const val CASUAL_DESCRIPTION = "A relaxed mode with no time pressure — just solve at your own pace."
private const val TIME_ATTACK_DESCRIPTION = "Race against the clock! Solve as many problems as possible before time runs out."
private const val SURVIVAL_DESCRIPTION = "You have limited lives — make too many mistakes and it’s game over."
private const val PRACTICE_DESCRIPTION = "Focused practice mode to improve accuracy and build skills with instant feedback."
sealed class GameMode(
    val displayName: String,
    val modeEmoji: String,
    val description: String,
    val problemMode: ProblemMode
) {

    object Casual: GameMode(
        "Casual",
        "\uD83C\uDFB2",
        CASUAL_DESCRIPTION,
        ProblemMode.FINITE)
    object TimeAttack: GameMode(
        "Time Attack",
        "⏱\uFE0F",
        TIME_ATTACK_DESCRIPTION,
        ProblemMode.FINITE)
    object Survival: GameMode(
        "Survival",
        "❤\uFE0F",
        SURVIVAL_DESCRIPTION,
        ProblemMode.INFINITE)
    object Practice: GameMode(
        "Practice",
        "\uD83E\uDDE0",
        PRACTICE_DESCRIPTION,
        ProblemMode.INFINITE)
}