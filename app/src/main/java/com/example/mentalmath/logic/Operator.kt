package com.example.mentalmath.logic

enum class Operator {
    ADD, SUB, MUL, DIV;

    fun toSymbol(): String {
        return when (this){
            ADD -> "+"
            SUB -> "-"
            MUL -> "×"
            DIV -> "÷"
        }

    }
}