package day10

import readToList
import java.util.*

private val input = readToList("day10.txt")
    .map { it.toCharArray() }


fun main() {
    println(part1())
    println(part2())
}

private fun part1(): Int {
    return input.sumOf { line -> calcInvalidLinePoints(line) }
}

private fun part2(): Long {
    val scores = mutableListOf<Long>()

    input.filter { line -> calcInvalidLinePoints(line) == 0 }
        .forEach { line ->
            val stack = Stack<Char>()

            for (char in line) {
                if (isOpenBracket(char)) {
                    stack.push(char)
                } else {
                    stack.pop()
                }
            }

            if (stack.isNotEmpty()) {
                val lineScore = stack
                    .reversed()
                    .map { char -> openToCloseMap[char]!! }
                    .fold(0L) { acc, char -> acc * 5 + p2ScoreMap[char]!! }

                scores.add(lineScore)
            }
        }

    return calculateMedian(scores)
}

private fun calcInvalidLinePoints(line: CharArray): Int {
    var points = 0
    val stack = Stack<Char>()
    for (char in line) {
        if (isOpenBracket(char)) {
            stack.push(char)
        } else {
            val openBracket = stack.pop()
            val expectedClose = openToCloseMap[openBracket]

            if (char != expectedClose) {
                points += p1ScoreMap[char]!!
            }
        }
    }
    return points
}


private fun isOpenBracket(c: Char) = openToCloseMap.keys.contains(c)

private val openToCloseMap = mapOf(
    '{' to '}',
    '(' to ')',
    '[' to ']',
    '<' to '>'
)

private val p1ScoreMap = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

private val p2ScoreMap = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)

private fun calculateMedian(scores: List<Long>): Long {
    val sorted = scores.sorted()
    val size = sorted.size
    val median = if (size % 2 == 0) {
        (sorted[size / 2] + sorted[size / 2 - 1]) / 2.0
    } else {
        sorted[size / 2].toDouble()
    }.toLong()
    return median
}
