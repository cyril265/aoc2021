package day5

import readToList

val input = readToList("day5.txt")

fun main() {
    val lines = input.map { line ->
        val (first, second) = line.split(" -> ")
        Line(toPoint(first), toPoint(second))
    }

    val resultPoints = lines.flatMap { line -> line.producePoints() }
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

    fun isHorizontal() = a.x == b.x
    fun isVertical() = a.y == b.y

    fun producePoints(): List<Point> {
        return if (isHorizontal()) {
            val range = if (a.y < b.x) a.y..b.y else b.y..a.y
            range.map { Point(a.x, it) }
        } else if (isVertical()) {
            val range = if (a.x < b.x) a.x..b.x else b.x..a.x
            range.map { Point(it, a.y) }
        } else {
            emptyList()
        }
    }

}

data class Point(val x: Int, val y: Int)
