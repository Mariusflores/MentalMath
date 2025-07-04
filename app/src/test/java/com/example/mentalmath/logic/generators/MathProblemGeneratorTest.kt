package com.example.mentalmath.logic.generators

import com.example.mentalmath.logic.models.core.Difficulty
import com.example.mentalmath.logic.models.core.Operator
import junit.framework.TestCase.assertTrue
import org.junit.Test


class MathProblemGeneratorTest {


    @Test
    fun generateAdditionOperands_operandsWithinExpectedRange() {

        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.EASY, arrayOf(Operator.ADD))
            assertAdditionOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.EASY)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.ADD)

        }
        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.MEDIUM, arrayOf(Operator.ADD))
            assertAdditionOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.MEDIUM)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.ADD)
        }
        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.HARD, arrayOf(Operator.ADD))
            assertAdditionOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.HARD)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.ADD)
        }

    }

    @Test
    fun generateSubtractionOperands_operandsWithinExpectedRange() {

        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.EASY, arrayOf(Operator.SUB))
            assertSubtractionOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.EASY)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.SUB)
        }
        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.MEDIUM, arrayOf(Operator.SUB))
            assertSubtractionOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.MEDIUM)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.SUB)
        }
        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.HARD, arrayOf(Operator.SUB))
            assertSubtractionOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.HARD)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.SUB)
        }
    }

    @Test
    fun generateMultiplicationOperands_operandsWithinExpectedRange() {

        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.EASY, arrayOf(Operator.MUL))
            assertMultiplicationOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.EASY)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.MUL)
        }
        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.MEDIUM, arrayOf(Operator.MUL))
            assertMultiplicationOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.MEDIUM)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.MUL)
        }
        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.HARD, arrayOf(Operator.MUL))
            assertMultiplicationOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.HARD)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.MUL)
        }
    }

    @Test
    fun generateDivisionOperands_operandsWithinExpectedRange() {

        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.EASY, arrayOf(Operator.DIV))
            assertDivisionOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.EASY)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.DIV)
        }
        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.MEDIUM, arrayOf(Operator.DIV))
            assertDivisionOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.MEDIUM)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.DIV)
        }
        repeat(100) {
            val mathProblem =
                MathProblemGenerator.generateRandomProblem(Difficulty.HARD, arrayOf(Operator.DIV))
            assertDivisionOperands(mathProblem.operandA, mathProblem.operandB, Difficulty.HARD)
            assertAnswer(mathProblem.correctAnswer, mathProblem.operandA, mathProblem.operandB, Operator.DIV)
        }
    }

    private fun assertAnswer(correctAnswer: Int, operand1: Int, operand2: Int, operator: Operator) {
        when (operator) {
            Operator.ADD -> assertTrue(
                "$correctAnswer not matching sum: ${operand1 + operand2}",
                correctAnswer == (operand1 + operand2)
            )

            Operator.SUB -> assertTrue(
                "$correctAnswer not matching difference: ${operand1 - operand2}",
                correctAnswer == (operand1 - operand2)
            )

            Operator.MUL -> assertTrue(
                "$correctAnswer not matching product: ${operand1 * operand2}",
                correctAnswer == (operand1 * operand2)
            )

            Operator.DIV -> assertTrue(
                "$correctAnswer not matching quotient: ${operand1 / operand2}",
                correctAnswer == (operand1 / operand2)
            )

        }
    }

    private fun assertAdditionOperands(operand1: Int, operand2: Int, difficulty: Difficulty) {
        when (difficulty) {
            Difficulty.EASY -> {
                assertTrue("Operand 1 out of range: $operand1", operand1 in 1..29)
                assertTrue("Operand 1 out of range: $operand2", operand2 in 1..24)
            }

            Difficulty.MEDIUM -> {
                assertTrue("Operand 1 out of range: $operand1", operand1 in 10..59)
                assertTrue("Operand 1 out of range: $operand2", operand2 in 10..54)
            }

            Difficulty.HARD -> {
                assertTrue("Operand 1 out of range: $operand1", operand1 in 30..119)
                assertTrue("Operand 1 out of range: $operand2", operand2 in 30..114)
            }
        }

    }

    private fun assertSubtractionOperands(operand1: Int, operand2: Int, difficulty: Difficulty) {
        when (difficulty) {
            Difficulty.EASY -> {
                assertTrue(
                    "The subtrahend $operand2 is bigger then $operand1, resulting in negative number",
                    operand1 >= operand2
                )
                assertTrue(
                    "Difference ${operand1 - operand2} is negative number",
                    (operand1 - operand2) >= 0)
                assertTrue(
                    "Operand 1 out of range: $operand1",
                    operand1 in operand2..24
                )
                assertTrue(
                    "Operand 1 out of range: $operand2",
                    operand2 in 1..19
                )
            }

            Difficulty.MEDIUM -> {
                assertTrue(
                    "The subtrahend $operand2 is bigger then $operand1, resulting in negative number",
                    operand1 >= operand2
                )
                assertTrue(
                    "Difference ${operand1 - operand2} is negative number",
                    (operand1 - operand2) >= 0)
                assertTrue("Operand 1 out of range: $operand1", operand1 in operand2..54)
                assertTrue("Operand 1 out of range: $operand2", operand2 in 10..49)
            }

            Difficulty.HARD -> {
                assertTrue(
                    "The subtrahend $operand2 is bigger then $operand1, resulting in negative number",
                    operand1 >= operand2
                )
                assertTrue(
                    "Difference ${operand1 - operand2} is negative number",
                    (operand1 - operand2) >= 0)
                assertTrue("Operand 1 out of range: $operand1", operand1 in operand2..100)
                assertTrue("Operand 1 out of range: $operand2", operand2 in 30..99)
            }
        }

    }

    private fun assertMultiplicationOperands(operand1: Int, operand2: Int, difficulty: Difficulty) {
        when (difficulty) {
            Difficulty.EASY -> {
                assertTrue("Operand 1 out of range: $operand1", operand1 in 1..11)
                assertTrue("Operand 1 out of range: $operand2", operand2 in 1..6)
            }

            Difficulty.MEDIUM -> {
                assertTrue("Operand 1 out of range: $operand1", operand1 in 7..16)
                assertTrue("Operand 1 out of range: $operand2", operand2 in 4..10)
            }

            Difficulty.HARD -> {
                assertTrue("Operand 1 out of range: $operand1", operand1 in 10..21)
                assertTrue("Operand 1 out of range: $operand2", operand2 in 6..16)
            }
        }

    }

    private fun assertDivisionOperands(operand1: Int, operand2: Int, difficulty: Difficulty) {
        when (difficulty) {
            Difficulty.EASY -> {
                assertTrue(
                    "Division results in non-whole number ${operand1 % operand2}",
                    operand1 % operand2 == 0)
                val quotient = operand1 / operand2
                assertTrue("Operand 1 out of range: $operand2", operand2 in 1..6)
                assertTrue("Operand 1 out of range: $quotient", quotient in 1..11)
            }

            Difficulty.MEDIUM -> {
                assertTrue(
                    "Division results in non-whole number ${operand1 % operand2}",
                    operand1 % operand2 == 0)
                val quotient = operand1 / operand2
                assertTrue("Operand 1 out of range: $operand2", operand2 in 3..13)
                assertTrue("Operand 1 out of range: $quotient", quotient in 5..16)
            }

            Difficulty.HARD -> {
                assertTrue(
                    "Division results in non-whole number ${operand1 % operand2}",
                    operand1 % operand2 == 0)
                val quotient = operand1 / operand2
                assertTrue("Operand 1 out of range: $operand2", operand2 in 5..21)
                assertTrue("Operand 1 out of range: $quotient", quotient in 10..26)
            }
        }

    }
}