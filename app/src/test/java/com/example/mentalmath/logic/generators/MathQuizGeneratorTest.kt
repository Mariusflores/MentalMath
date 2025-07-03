package com.example.mentalmath.logic.generators

import com.example.mentalmath.logic.models.Difficulty
import com.example.mentalmath.logic.models.MathProblem
import com.example.mentalmath.logic.models.Operator
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.random.Random

class MathQuizGeneratorTest {

    @Test
    fun generateRandomQuiz_returnsListOfProblemsByGivenLengthAndDifficulty() {
        repeat(100) {
            val range = Random.nextInt(1, 100)
            val quiz = MathQuizGenerator.generateRandomOperatorQuiz(
                Difficulty.MEDIUM,
                Operator.entries.toTypedArray(),
                range
            )
            assertTrue(
                "Generated Quiz size not matching given size",
                quiz.size == range
            )
        }
    }
}

