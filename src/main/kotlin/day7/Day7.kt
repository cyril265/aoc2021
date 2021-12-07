package day7

import readToList
import kotlin.math.abs


val input = readToList("day7.txt").first().split(",").map { it.toInt() }


fun main() {
    println(part1())
    println(part2())
}

private fun part1(): Int {
    val sorted = input.sorted()
    val size = sorted.size
    val median = if (size % 2 == 0) {
        (sorted[size / 2] + sorted[size / 2 - 1]) / 2.0
    } else {
        sorted[size / 2].toDouble()
    }.toInt()

    return sorted.sumOf { pos -> abs(pos - median) }
}

private fun part2(): Int {
    val minPosition = input.minOf { it }
    val maxPosition = input.maxOf { it }

    return (minPosition..maxPosition)
        .minOf { possiblePosition ->
            input.sumOf { crabPosition -> gaussSum(abs(crabPosition - possiblePosition)) }
        }
}

private fun gaussSum(n: Int) = (n * n + n) / 2

