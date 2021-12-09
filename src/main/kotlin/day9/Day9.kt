package day9

import readToList

private val input = readToList("day9.txt")
    .map { line -> line.map { char -> Character.getNumericValue(char) } }

fun main() {
    println(part1())

    val p2Input = input.map {
        it.map { height ->
            Height(height, height == 9)
        }
    }


}

private fun part2(): Int {
    val lowestPoints = mutableListOf<Int>()

    for ((listIndex, line) in input.withIndex()) {
        for ((index, height) in line.withIndex()) {
            val lowest = isLowest(index, input, listIndex, height)
            if (lowest) {
                lowestPoints.add(height)
            }
        }
    }

    return lowestPoints.sumOf { it + 1 }
}

private data class Height(val height: Int, val used: Boolean = false)


private fun part1(): Int {
    val lowestPoints = mutableListOf<Int>()

    for ((listIndex, line) in input.withIndex()) {
        for ((index, height) in line.withIndex()) {
            val lowest = isLowest(index, input, listIndex, height)
            if (lowest) {
                lowestPoints.add(height)
            }
        }
    }

    return lowestPoints.sumOf { it + 1 }
}

private fun isLowest(
    index: Int,
    convertedInput: List<List<Int>>,
    lineIndex: Int,
    height: Int
): Boolean {
    val line = convertedInput[lineIndex]
    val prev = line.getOrNull(index - 1)
    val next = line.getOrNull(index + 1)
    val down = convertedInput.getOrNull(lineIndex - 1)
        ?.get(index)
    val up = convertedInput.getOrNull(lineIndex + 1)
        ?.get(index)
    val lowest = arrayOf(prev, next, down, up).filterNotNull().all { height < it }
    return lowest
}