package day17

private val target = Area(60..94, -171..-136)


fun main() {
    var globalMax = 0
    var count = 0

    for (x in -1000..1000) {
        for (y in -1000..1000) {
            val velocity = Velocity(Point(x, y))
            val maxY = findMax(velocity)
            if (maxY != null) {
                if (maxY > globalMax) globalMax = maxY
                count++
            }
        }
    }

    println("p1 $globalMax")
    println("p2 $count")

}


private fun findMax(velocity: Velocity): Int? {
    var position = Point(0, 0)
    var maxY1 = 0
    for (step in 1..500) {
        position = Point(position.x + velocity.point.x, position.y + velocity.point.y)

        if (position.y > maxY1) maxY1 = position.y

        if (target.contains(position)) {
            return maxY1
        } else {
            velocity.step()
        }
    }
    return null
}

private data class Area(val x: IntRange, val y: IntRange) {

    fun contains(point: Point) = x.contains(point.x) && y.contains(point.y)
}


private data class Point(var x: Int, var y: Int)

private data class Velocity(val point: Point) {

    fun step() {
        val (x, y) = point
        val newX = when {
            x > 0 -> x - 1
            x < 0 -> x + 1
            else -> 0
        }
        val newY = y - 1
        this.point.x = newX
        this.point.y = newY
    }

}
