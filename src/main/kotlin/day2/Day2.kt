package day2

import readToList


private val input = readToList("day2")

fun main() {
    println(part1())
    println(part2())
}


private fun part1(): Int {
    val (horizontal, vertical) = splitInput()
        .partition { (direction, _) ->
            direction == "forward"
        }

    return horizontal.sumOf { (_, amount) -> amount } *
            vertical.sumOf { (direction, amount) -> if (direction == "down") amount else -amount }
}


private fun part2(): Int {
    var aim = 0
    var position = 0
    var depth = 0

    splitInput().forEach { (direction, x) ->
        when (direction) {
            "forward" -> {
                position += x
                depth += aim * x
            }
            "down" -> {
                aim += x
            }
            else -> {
                aim -= x
            }
        }
    }

    return depth * position
}

private fun splitInput() = input.map {
    val (direction, amount) = it.split(" ")
    direction to amount.toInt()
}
