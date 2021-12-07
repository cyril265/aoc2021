package day5

import readToList

val input = readToList("day5.txt")

fun main() {
    val validLines = input
        .map { line ->
            val (first, second) = line.split(" -> ")
            Line(toPoint(first), toPoint(second))
        }
        .filter { it.notDiagonal() }

    val resultPoints = validLines.flatMap { line -> line.producePoints() }
    val groupedByPoints = resultPoints
        .groupingBy { it }
        .eachCount()

    println(groupedByPoints.values.count { it >= 2 })
}

private fun toPoint(first: String): Point {
    val (x, y) = first.split(",").map { it.toInt() }
    return Point(x, y)
}

data class Line(val a: Point, val b: Point) {

    fun notDiagonal() = isHorizontal() || isVertical()
    fun isHorizontal() = a.x == b.x
    fun isVertical() = a.y == b.y

    fun producePoints(): List<Point> {
        return if (isHorizontal()) {
            val range = if (a.y < b.y) a.y..b.y else b.y..a.y
            range.map { Point(a.x, it) }
        } else {
            val range = if (a.x < b.x) a.x..b.x else b.x..a.x
            range.map { Point(it, a.y) }
        }
    }
}

data class Point(val x: Int, val y: Int)
