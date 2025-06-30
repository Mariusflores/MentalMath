package com.example.mentalmath.logic

import kotlin.random.Random

object MathProblemGenerator {

    /**
     * Generator Functions
     * */
    fun generateAdditionProblem(): MathProblem {

        val operator = Operator.ADD
        val (a, b) = generateOperands(operator)
        val questionText = String.format("%i %s %i", a, Operator.ADD.toSymbol(), b)
        val correctAnswer = (a + b)

        return MathProblem(
            a,
            b,
            operator,
            questionText,
            correctAnswer
        )
    }
    fun generateRandomProblem() : MathProblem {

        val operator = Operator.entries.toTypedArray().random()
        val (operand1, operand2) = generateOperands(operator)
        val questionText = String.format("%i %s %i", operand1, operator.toSymbol(), operand2)
        val answer =  when (operator){
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
    private fun generateOperands(operator: Operator): Pair<Int, Int> {

        return when (operator){
            Operator.ADD -> generateAdditionOperands()
            Operator.SUB -> generateSubtractionOperands()
            Operator.MUL -> generateMultiplicationOperands()
            Operator.DIV -> generateDivisionOperands()
        }
    }

    private fun generateDivisionOperands(): Pair<Int, Int> {
        val divisor = Random.nextInt(1, 13)
        val dividend = divisor * Random.nextInt(1, 11)

        return Pair(divisor, dividend)
    }

    private fun generateMultiplicationOperands(): Pair<Int, Int> {
        // Multiplicand upper limit 15
        val multiplicand = Random.nextInt(1, 16)
        // Multiplicator upper limit 5
        val multiplicator = Random.nextInt(1, 6)

        return Pair(multiplicand, multiplicator)
    }

    private fun generateSubtractionOperands(): Pair<Int, Int> {

        val subtrahend = Random.nextInt(1, 100)
        val minuend = Random.nextInt(subtrahend, 101)

        return Pair(minuend, subtrahend)
    }

    private fun generateAdditionOperands(): Pair<Int, Int> {
        val addend1 = Random.nextInt(1, 100)
        val addend2 = Random.nextInt(1, 100)

        return Pair(addend1, addend2)
    }
}