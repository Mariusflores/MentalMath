package com.example.mentalmath.logic.gamemodes.factory

import com.example.mentalmath.logic.gamemodes.CasualModeHandler
import com.example.mentalmath.logic.gamemodes.GameModeHandler
import com.example.mentalmath.logic.gamemodes.PracticeModeHandler
import com.example.mentalmath.logic.gamemodes.SurvivalModeHandler
import com.example.mentalmath.logic.gamemodes.TimeAttackModeHandler
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration

object GameModeHandlerFactory{
    fun create(modeConfiguration: ModeConfiguration): GameModeHandler {

        return when(modeConfiguration.gameMode){

            GameMode.Casual -> CasualModeHandler()
            GameMode.TimeAttack -> TimeAttackModeHandler()
            GameMode.Survival -> SurvivalModeHandler()
            GameMode.Practice -> PracticeModeHandler()
        }

    }

    fun startQuiz(currentHandler: GameModeHandler, modeConfig: ModeConfiguration
    ): List<MathProblem>{
        return when (modeConfig.gameMode){

            GameMode.Casual -> {
                currentHandler.startGame(modeConfig)
            }
            GameMode.TimeAttack -> {
                currentHandler.startGame(modeConfig)

            }
            GameMode.Survival -> {
                currentHandler.startGame(modeConfig)

            }
            GameMode.Practice -> {
                currentHandler.startGame(modeConfig)
            }
        }
    }

    fun getNextProblem(currentHandler: GameModeHandler, modeConfig: ModeConfiguration): MathProblem{
        return when(modeConfig.gameMode){
            GameMode.Casual -> currentHandler.getNextProblem(modeConfig)
            GameMode.TimeAttack -> currentHandler.getNextProblem(modeConfig)
            GameMode.Survival -> currentHandler.getNextProblem(modeConfig)
            GameMode.Practice -> currentHandler.getNextProblem(modeConfig)
        }
    }
}