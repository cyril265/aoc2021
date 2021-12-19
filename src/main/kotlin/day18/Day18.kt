package day18

import popUntil
import readToList
import java.util.*

val input = readToList("day18.txt")

fun main() {
    var current = createPairExpression(input.first())
    input.drop(1).forEach { line ->
        val parsed = createPairExpression(line)
        current += parsed
    }
    println("magnitude " + current.magnitude())

    var currentMax = 0L
    for (line1 in input) {
        for (line2 in input) {
            if (line1 === line2) continue

            val pairSum = createPairExpression(line1) + createPairExpression(line2)
            val magnitude = pairSum.magnitude()
            if (magnitude > currentMax) currentMax = magnitude
        }
    }

    println("max $currentMax")
}


fun createPairExpression(input: String): CoolPair {
    val pair = parsePair(input)
    pair.reduce()
    return pair
}

fun parsePair(input: String): CoolPair {
    val stack = Stack<Token>()
    for (char in input) {
        when (char) {
            '[' -> {
                stack.push(OpenBracket)
            }
            ']' -> {
                val pair = createNextPair(stack)
                stack.push(pair)
            }
            ',' -> {
                stack.push(Comma)
            }
            else -> {
                stack.push(Digit.ofChar(char))
            }

        }
    }
    return stack.pop() as CoolPair
}

fun createNextPair(stack: Stack<Token>): CoolPair {
    val (left, right) = stack.popUntil { it is OpenBracket }
        .reversed()
        .filterIsInstance<PairComponent>()
    return CoolPair(left, right)
}


sealed interface Token
object OpenBracket : Token
object Comma : Token
interface PairComponent : Token

data class Digit(var number: Int) : PairComponent {
    companion object {
        fun ofChar(char: Char) = Digit(Character.getNumericValue(char))
    }

    override fun toString(): String {
        return number.toString()
    }
}

