package day9

import readToList

private val input = readToList("day9.txt")
    .map { line ->
        line.map { char ->
            val numericValue = Character.getNumericValue(char)
            Height(height = numericValue, valid = numericValue != 9)
        }
    }

fun main() {
    val p2Input = HeightMatrix(input)
//    val message = part1()
//    println(message)

    val part2 = part2(p2Input)
    println(part2)
}

private fun part2(input: HeightMatrix): Long {
    part1()
    val basins = mutableListOf<Int>()
    for ((columnIndex, line) in input.heightMatrix.withIndex()) {
        val lowestPoints = line.withIndex().filter { (_, height) -> height.lowest }
        for ((rowIndex, height) in lowestPoints) {
            val size = setBasin(input, rowIndex, columnIndex)
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


private fun setBasin(
    heightMatrix: HeightMatrix,
    columnIndex: Int,
    lineIndex: Int
): Int {
    val currentItem = heightMatrix.get(lineIndex, columnIndex)
    if (currentItem == null || currentItem.basinPart || !currentItem.valid) return 0;

    val left = heightMatrix.get(lineIndex, columnIndex - 1)
    val right = heightMatrix.get(lineIndex, columnIndex + 1)
    val up = heightMatrix.get(lineIndex - 1, columnIndex)
    val down = heightMatrix.get(lineIndex + 1, columnIndex)

    val lowest = arrayOf(left, right, down, up)
        .filterNotNull()
        .filter { it.valid }
        .all { it.basinPart || currentItem.height < it.height }

    var basinSize = 0
    if (lowest) {
        currentItem.basinPart = true
        basinSize += 1
        basinSize += setBasin(heightMatrix, columnIndex - 1, lineIndex)
        basinSize += setBasin(heightMatrix, columnIndex + 1, lineIndex)
        basinSize += setBasin(heightMatrix, columnIndex, lineIndex - 1)
        basinSize += setBasin(heightMatrix, columnIndex, lineIndex + 1)
    }
    return basinSize
}

private fun part1(): Int {
    val lowestPoints = mutableListOf<Height>()

    for ((listIndex, line) in input.withIndex()) {
        for ((index, height) in line.withIndex()) {
            val lowest = isLowest(index, input, listIndex, height)
            if (lowest) {
                lowestPoints.add(height)
            }
        }
    }

    return lowestPoints.sumOf { it.height + 1 }
}

private fun isLowest(
    index: Int,
    convertedInput: List<List<Height>>,
    lineIndex: Int,
    currentElement: Height
): Boolean {
    val line = convertedInput[lineIndex]
    val prev = line.getOrNull(index - 1)
    val next = line.getOrNull(index + 1)
    val down = convertedInput.getOrNull(lineIndex - 1)
        ?.get(index)
    val up = convertedInput.getOrNull(lineIndex + 1)
        ?.get(index)
    val lowest = arrayOf(prev, next, down, up).filterNotNull().all { currentElement.height < it.height }
    if (lowest) {
        currentElement.lowest = true
        return true
    }
    return false
}

private data class Height(
    val height: Int,
    var lowest: Boolean = false,
    val valid: Boolean,
    var basinPart: Boolean = false
)

private data class HeightMatrix(val heightMatrix: List<List<Height>>) {
    fun get(lineIndex: Int, columnIndex: Int): Height? {
        return heightMatrix.getOrNull(lineIndex)?.getOrNull(columnIndex)
    }
}
