package day9

import readToList

private val input = readToList("day9.txt")
    .map { line -> line.map { char -> Character.getNumericValue(char) } }

fun main() {
//    println(part1())

    val p2Input = input.map {
        it.map { height ->
            Height(height = height, valid = height != 9)
        }
    }

    val part2 = part2(p2Input)
    println(part2)

//    val message = p2Input.map { line -> line.map { if (it.lowest) it.height.toString() else "0" } }
//    println(message.joinToString(separator = "\n"))
}

private fun part2(input: List<List<Height>>): Long {
    val basins = mutableListOf<Int>()
    for ((listIndex, line) in input.withIndex()) {
        for ((index, height) in line.withIndex()) {
            if (height.lowest || !height.valid) continue
            val size = setLowest(index, input, listIndex)
            if (size > 0) {
                basins.add(size)
                println(size)
            }
        }
    }
    return basins.sortedDescending()
        .take(3)
        .map { it.toLong() }
        .reduce { a, b -> a * b }

}


private fun setLowest(
    columnIndex: Int,
    heightMatrix: List<List<Height>>,
    lineIndex: Int
): Int {
    val currentItem = heightMatrix.getOrNull(lineIndex)?.getOrNull(columnIndex)
    if (currentItem == null || currentItem.lowest || !currentItem.valid) return 0;

    val line = heightMatrix[lineIndex]
    val left = line.getOrNull(columnIndex - 1)
    val right = line.getOrNull(columnIndex + 1)
    val up = heightMatrix.getOrNull(lineIndex - 1)?.get(columnIndex)
    val down = heightMatrix.getOrNull(lineIndex + 1)?.get(columnIndex)

    val lowest = arrayOf(left, right, down, up)
        .filterNotNull()
        .filter { it.valid }
        .all { it.lowest || currentItem.height < it.height }

    var basinSize = 0
    if (lowest) {
        currentItem.lowest = true
        basinSize += 1
        basinSize += setLowest(columnIndex - 1, heightMatrix, lineIndex)
        basinSize += setLowest(columnIndex + 1, heightMatrix, lineIndex)
        basinSize += setLowest(columnIndex, heightMatrix, lineIndex - 1)
        basinSize += setLowest(columnIndex, heightMatrix, lineIndex + 1)
    }
    return basinSize
}

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

private data class Height(val height: Int, var lowest: Boolean = false, val valid: Boolean)
