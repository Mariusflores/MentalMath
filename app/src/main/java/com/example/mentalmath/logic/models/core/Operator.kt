package com.example.mentalmath.logic.models.core

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

    object OperatorConverter{
        fun toOperatorArray(operatorStringList : List<String>): Array<Operator>{
            val operatorList = mutableListOf<Operator>()

            operatorStringList.forEach{ operator ->

                when(operator){
                    "+" -> operatorList.add(ADD)
                    "-" -> operatorList.add(SUB)
                    "×" -> operatorList.add(MUL)
                    "÷" -> operatorList.add(DIV)
                }

            }
            return operatorList.toTypedArray()
        }
    }
}