package day1

import readInput

private val input = readInput("1").map { it.toInt() }


fun main() {
    val part1 = part1(input)
    println(part1)

    val part2 = part2(input)
    println(part2)
}

fun part1(input: List<Int>): Int {
    return input
        .zipWithNext { a, b -> if (b > a) 1 else 0 }
        .sum()
}

fun part2(input: List<Int>): Int {
    val windowSums = input
        .mapIndexed { index, element ->
            if (index + 3 > input.size) {
                null
            } else {
                element + input[index + 1] + input[index + 2]
            }
        }
        .filterNotNull()
    return part1(windowSums)
}
