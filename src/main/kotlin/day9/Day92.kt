package day9

import readToList

fun main() {
    println(part1())
    println(part2())
}

private fun part1(): Int {
    val heightMatrix = HeightMatrix(readInput())
    heightMatrix.setLowPoints()
    val filter = heightMatrix.matrix
        .flatten()
        .filter { it.lowest }
    return filter
        .sumOf { it.height + 1 }

}

private fun part2(): Int {
    val heightMatrix = HeightMatrix(readInput())
    heightMatrix.setLowPoints()

    val basins = heightMatrix.matrix
        .flatten()
        .filter { it.lowest }
        .map { lowPoint ->
            heightMatrix.search(lowPoint)
        }

    val (a, b, c) = basins.sortedDescending()
    return a * b * c
}

private data class HeightMatrix(val matrix: List<List<Height>>) {
    fun get(rowIndex: Int, columnIndex: Int): Height? {
        return matrix.getOrNull(rowIndex)?.getOrNull(columnIndex)
    }

    fun setLowPoints() {
        for (row in matrix) {
            for (height in row) {
                if (getAdjacent(height).all { height.height < it.height }) {
                    height.lowest = true
                }
            }
        }
    }

    fun search(lowPoint: Height): Int {
        val adjacent = getAdjacent(lowPoint).filter { !it.basinPart && it.valid }
        var basinSize = 0

        if (!lowPoint.basinPart) {
            lowPoint.basinPart = true
            basinSize++
        }

        basinSize += adjacent
            .filter { lowPoint.height <= it.height }
            .sumOf { search(it) }
        return basinSize
    }

    private fun getAdjacent(height: Height): List<Height> {
        return listOfNotNull(
            get(height.rowIndex - 1, height.columnIndex),
            get(height.rowIndex + 1, height.columnIndex),
            get(height.rowIndex, height.columnIndex - 1),
            get(height.rowIndex, height.columnIndex + 1),
        )
    }

}

private data class Height(
    val height: Int,
    var lowest: Boolean = false,
    val valid: Boolean,
    var basinPart: Boolean = false,
    val columnIndex: Int,
    val rowIndex: Int
)

private fun readInput() = readToList("day9.txt")
    .mapIndexed { rowIndex, line ->
        line.mapIndexed { columnIndex, char ->
            val numericValue = Character.getNumericValue(char)
            Height(height = numericValue, valid = numericValue != 9, columnIndex = columnIndex, rowIndex = rowIndex)
        }
    }
