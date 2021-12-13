package day13

import readToList

private val input = readToList("day13.txt")

fun main() {
    println(part1())
    println(part2())
}

private fun part1(): Int {
    val (foldInstructions, dots) = mapInput()
    val foldInstruction = foldInstructions.first()
    dots.forEach { dot ->
        dot.fold(foldInstruction)
    }

    return dots.toSet().size
}

private fun part2(): String {
    val (foldInstructions, dots) = mapInput()
    foldInstructions.forEach { foldInstruction ->
        dots.forEach { dot ->
            dot.fold(foldInstruction)
        }
    }
    val yMax = dots.maxOf { it.y }
    val xMax = dots.maxOf { it.x }
    val resultMatrix = Array(yMax + 1) { Array(xMax + 1) { "." } }

    resultMatrix.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, _ ->
            if (dots.any { it.x == columnIndex && it.y == rowIndex }) resultMatrix[rowIndex][columnIndex] = "#"
        }
    }

    return resultMatrix.joinToString(separator = "\n") { it.contentToString() }
}

private fun mapInput(): Pair<List<FoldInstruction>, List<Dot>> {
    val foldIndex = input.indexOfFirst { it.startsWith("fold") }
    val dotsPlain = input.subList(0, foldIndex - 1)
    val foldInstructionsPlain = input.subList(foldIndex, input.size)

    val foldInstructions = foldInstructionsPlain.map {
        val (coordinate, value) = it.substringAfter("fold along ").split("=")
        val coordinateType = if (coordinate == "x") CoordinateType.X else CoordinateType.Y
        FoldInstruction(value.toInt(), coordinateType)
    }


    val dots = dotsPlain.map {
        val (x, y) = it.split(",")
        Dot(x.toInt(), y.toInt())
    }
    return Pair(foldInstructions, dots)
}


private data class Dot(var x: Int, var y: Int) {

    fun fold(foldInstruction: FoldInstruction) {
        if (foldInstruction.type == CoordinateType.Y) {
            if (y > foldInstruction.coordinate) {
                y = foldInstruction.coordinate - (y - foldInstruction.coordinate)
            }
        } else {
            if (x > foldInstruction.coordinate) {
                x = foldInstruction.coordinate - (x - foldInstruction.coordinate)
            }
        }
    }
}

private data class FoldInstruction(val coordinate: Int, val type: CoordinateType)

enum class CoordinateType {
    X, Y
}
