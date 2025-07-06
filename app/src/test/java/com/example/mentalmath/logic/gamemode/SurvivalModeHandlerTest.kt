package com.example.mentalmath.logic.gamemode

import com.example.mentalmath.logic.gamemodes.SurvivalModeHandler
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration
import junit.framework.TestCase.assertTrue
import kotlin.test.Test

class SurvivalModeHandlerTest {

    private fun config() = ModeConfiguration(Difficulty.EASY, Operator.entries.toTypedArray(), null,GameMode.Survival)


    @Test
    fun startGame_returnsEmptyList(){
        val handler = SurvivalModeHandler()
        val modeConfig = config()

        val quiz = handler.startGame(modeConfig)

        assertTrue(
            "Should return empty list",
            quiz.isEmpty()
        )
    }

}