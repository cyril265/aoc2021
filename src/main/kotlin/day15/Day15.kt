package day15

import readToList

private val input = readToList("day15.txt")

fun main() {
    val weightMatrix = input.map { line ->
        line.chars().map { char ->
            Character.getNumericValue(char)
        }.toArray()
    }.toTypedArray()

    val graph = Graph(weightMatrix)
    println(graph)

}


data class Graph(val matrix: Array<IntArray>) {

    val visitedMatrix: Array<BooleanArray> = Array(matrix.size) { _ -> BooleanArray(matrix[0].size) }

    init {
        visitedMatrix[0][0] = true
    }

    fun adjacent(rowIndex: Int, columnIndex: Int): List<Int> {
        return listOfNotNull(
            get(rowIndex + 1, columnIndex),
            get(rowIndex - 1, columnIndex),
            get(rowIndex, columnIndex + 1),
            get(rowIndex, columnIndex - 1),
        )
    }

    fun get(rowIndex: Int, columnIndex: Int): Int? {
        if (rowIndex < matrix.size) {
            val row = matrix[rowIndex]
            if (columnIndex < row.size) {
                return row[columnIndex]
            }
        }
        return null
    }

    private fun doIt() {
        var shortestDistance = Int.MAX_VALUE
        var shortIndex = -1
        for ((rowIndex, row) in matrix.withIndex()) {
            for ((columnIndex, weight) in row.withIndex()) {
                shortest()
            }
        }
    }

    private fun shortest() {

    }


}

class Vertex(
    val weight: Int,
    val adjacent: Array<Vertex>,
    val visited: Boolean = false
)
