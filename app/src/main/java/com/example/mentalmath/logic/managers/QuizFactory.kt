package com.example.mentalmath.logic.managers

import com.example.mentalmath.logic.generators.MathProblemGenerator
import com.example.mentalmath.logic.generators.MathQuizGenerator
import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.MathProblem
import com.example.mentalmath.logic.models.gamemode.GameMode
import com.example.mentalmath.logic.models.gamemode.ModeConfiguration


class QuizFactory {

    fun generateQuizByGameMode(modeConfig: ModeConfiguration): List<MathProblem> {

        return when(modeConfig.gameMode){
            GameMode.Casual -> {
                generateCasualQuiz(modeConfig)
            }
            GameMode.TimeAttack -> {
                generateTimeAttackQuiz(modeConfig)
            }
            // Used for modes that fetch new problems per question
            else -> emptyList()
        }
    }


    fun generateProblemByGameMode(modeConfig: ModeConfiguration): MathProblem?{
        return when(modeConfig.gameMode){
            GameMode.Practice, GameMode.Survival -> generateSingularProblem(modeConfig)
            else -> error("generateProblemByGameMode called with unsupported game mode: ${modeConfig.gameMode}")


        }
    }


    private fun generateTimeAttackQuiz(modeConfig: ModeConfiguration):List<MathProblem> {

        return when(modeConfig.difficulty){
            Difficulty.EASY -> {
                MathQuizGenerator.generateRandomOperatorQuiz(
                    modeConfig.difficulty,
                    modeConfig.operators,
                    10)
            }
            Difficulty.MEDIUM ->{
                MathQuizGenerator.generateRandomOperatorQuiz(
                    modeConfig.difficulty,
                    modeConfig.operators,
                    15)
            }
            Difficulty.HARD -> {
                MathQuizGenerator.generateRandomOperatorQuiz(
                    modeConfig.difficulty,
                    modeConfig.operators,
                    20)
            }
        }
    }

    private fun generateCasualQuiz(modeConfig: ModeConfiguration):List<MathProblem> {
        val length = modeConfig.length ?: 0
        return MathQuizGenerator.generateRandomOperatorQuiz(modeConfig.difficulty, modeConfig.operators, length )

    }

    private fun generateSingularProblem(modeConfig: ModeConfiguration): MathProblem? {
        return MathProblemGenerator.generateRandomProblem(modeConfig.difficulty, modeConfig.operators)
    }

}