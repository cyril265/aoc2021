package day17

private val target = Area(60..94, -171..-136)


fun main() {
    var globalMax = 0
    var velocitiesCount = 0

    for (x in -1000..1000) {
        for (y in -1000..1000) {
            val maxY = findMaxY(Velocity(Point(x, y)))
            if (maxY != null) {
                if (maxY > globalMax) globalMax = maxY
                velocitiesCount++
            }
        }
    }

    println("p1 $globalMax")
    println("p2 $velocitiesCount")
}


private fun findMaxY(velocity: Velocity): Int? {
    var position = Point(0, 0)
    var maxY = 0
    for (step in 1..500) {
        position = position.move(velocity)

        if (position.y > maxY) maxY = position.y

        if (target.contains(position)) {
            return maxY
        }
        velocity.step()
    }
    return null
}

private data class Area(val x: IntRange, val y: IntRange) {

    fun contains(point: Point) = x.contains(point.x) && y.contains(point.y)
}


private data class Point(var x: Int, var y: Int) {

    fun move(velocity: Velocity) = Point(x + velocity.point.x, y + velocity.point.y)
}

private data class Velocity(val point: Point) {

    fun step() {
        val (x, y) = point
        this.point.x = when {
            x > 0 -> x - 1
            x < 0 -> x + 1
            else -> 0
        }
        this.point.y = y - 1
    }
}
