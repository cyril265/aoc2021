package day5

import readToList

val input = readToList("day5.txt")

fun main() {
    val lines = input.map { line ->
        val (first, second) = line.split(" -> ")
        Line(toPoint(first), toPoint(second))
    }.filter { line -> line.isHorizontal() || line.isVertical() }

    val result = Array(10) { IntArray(10) }
    val resultPoints = lines.map { line -> line.producePoints() }
    resultPoints.flatten()
        .forEach { point -> result[point.x][point.y] = 1}
    println(resultPoints)

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
            (a.y..b.y).map { Point(a.x, it) }
        } else {
            (a.x..b.x).map { Point(it, a.y) }
        }
    }

}

data class Point(val x: Int, val y: Int)
