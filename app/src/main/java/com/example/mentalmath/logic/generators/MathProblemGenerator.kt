package com.example.mentalmath.logic.generators

import android.annotation.SuppressLint
import com.example.mentalmath.logic.models.Difficulty
import com.example.mentalmath.logic.models.MathProblem
import com.example.mentalmath.logic.models.Operator
import kotlin.random.Random

object MathProblemGenerator {

    /**
     * Generator Functions
     * */
    fun generateAdditionProblem(): MathProblem {

        val operator = Operator.ADD
        val (a, b) = generateOperands(operator, Difficulty.EASY)
        val questionText = generateQuestionString(a, b, operator.toSymbol())
        val correctAnswer = (a + b)

        return MathProblem(
            a,
            b,
            operator,
            questionText,
            correctAnswer
        )
    }

    fun generateRandomProblem(quizDifficulty: Difficulty, operators :Array<Operator>): MathProblem {

        val operator = operators.random()
        val (operand1, operand2) = generateOperands(operator, quizDifficulty)
        val questionText = generateQuestionString(operand1, operand2, operator.toSymbol())
        val answer = when (operator) {
            Operator.ADD -> operand1 + operand2
            Operator.SUB -> operand1 - operand2
            Operator.MUL -> operand1 * operand2
            Operator.DIV -> operand1 / operand2
        }

        return MathProblem(
            operand1,
            operand2,
            operator,
            questionText,
            answer
        )
    }

    /**
     * Helper Functions
     * */

    @SuppressLint("DefaultLocale")
    private fun generateQuestionString(
        operand1: Int,
        operand2: Int,
        operator: String
    ): String {

        return String.format("%d %s %d", operand1, operator, operand2)
    }

    private fun generateOperands(operator: Operator, quizDifficulty: Difficulty): Pair<Int, Int> {

        return when (operator) {
            Operator.ADD -> generateAdditionOperands(quizDifficulty)
            Operator.SUB -> generateSubtractionOperands(quizDifficulty)
            Operator.MUL -> generateMultiplicationOperands(quizDifficulty)
            Operator.DIV -> generateDivisionOperands(quizDifficulty)
        }
    }

    private fun generateDivisionOperands(quizDifficulty: Difficulty): Pair<Int, Int> {

        return when (quizDifficulty) {
            Difficulty.EASY -> {
                val divisor = Random.nextInt(1, 6)
                val quotient = Random.nextInt(1, 11)
                val dividend = divisor * quotient
                Pair(dividend, divisor)
            }

            Difficulty.MEDIUM -> {
                val divisor = Random.nextInt(3, 13)
                val quotient = Random.nextInt(5, 16)
                val dividend = divisor * quotient
                Pair(dividend, divisor)
            }

            Difficulty.HARD -> {
                val divisor = Random.nextInt(5, 21)
                val quotient = Random.nextInt(10, 26)
                val dividend = divisor * quotient
                Pair(dividend, divisor)
            }
        }
    }

    private fun generateMultiplicationOperands(quizDifficulty: Difficulty): Pair<Int, Int> {

        return when (quizDifficulty) {
            Difficulty.EASY -> {
                val multiplicand = Random.nextInt(1, 11)
                val multiplicator = Random.nextInt(1, 6)
                Pair(multiplicand, multiplicator)
            }

            Difficulty.MEDIUM -> {
                val multiplicand = Random.nextInt(7, 16)
                val multiplicator = Random.nextInt(4, 10)
                Pair(multiplicand, multiplicator)
            }

            Difficulty.HARD -> {
                val multiplicand = Random.nextInt(10, 21)
                val multiplicator = Random.nextInt(6, 16)
                Pair(multiplicand, multiplicator)
            }
        }


    }

    private fun generateSubtractionOperands(quizDifficulty: Difficulty): Pair<Int, Int> {

        return when (quizDifficulty) {
            Difficulty.EASY -> {
                val subtrahend = Random.nextInt(1, 20)
                val minuend = Random.nextInt(subtrahend, 25)
                Pair(minuend, subtrahend)
            }

            Difficulty.MEDIUM -> {
                val subtrahend = Random.nextInt(10, 50)
                val minuend = Random.nextInt(subtrahend, 55)
                Pair(minuend, subtrahend)
            }

            Difficulty.HARD -> {
                val subtrahend = Random.nextInt(30, 100)
                val minuend = Random.nextInt(subtrahend, 101)
                Pair(minuend, subtrahend)
            }
        }


    }

    private fun generateAdditionOperands(quizDifficulty: Difficulty): Pair<Int, Int> {

        return when (quizDifficulty) {
            Difficulty.EASY -> {
                val addend1 = Random.nextInt(1, 30)
                val addend2 = Random.nextInt(1, 25)
                Pair(addend1, addend2)
            }

            Difficulty.MEDIUM -> {
                val addend1 = Random.nextInt(10, 60)
                val addend2 = Random.nextInt(10, 55)
                Pair(addend1, addend2)
            }

            Difficulty.HARD -> {
                val addend1 = Random.nextInt(30, 120)
                val addend2 = Random.nextInt(30, 115)
                Pair(addend1, addend2)
            }
        }


    }
}