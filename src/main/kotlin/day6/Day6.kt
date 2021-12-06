package day6

import readToList

val input = readToList("day6.txt")
    .first()
    .split(",")
    .map { it.toInt() }

fun main() {
    println(fishIt(80))
    println(fishIt(256))
}

private fun fishIt(days: Int): Long {
    val fishArray = LongArray(9)
    input.forEach {
        fishArray[it]++
    }

    (1..days).forEach { _ ->
        val tickResult = tick(fishArray)

        fishArray[6] += tickResult.resetFishCount
        fishArray[8] += tickResult.resetNewFishCount
    }

    return fishArray.sum()
}

fun tick(fishArray: LongArray): TickResult {
    var waitingFishCount = 0L
    var waitingNewFishCount = 0L
    fishArray.forEachIndexed { dayIndex, fishCount ->
        if (dayIndex == 0) {
            fishArray[0] -= fishCount
            waitingFishCount += fishCount
            waitingNewFishCount += fishCount
        } else {
            fishArray[dayIndex] -= fishCount
            fishArray[dayIndex - 1] += fishCount
        }
    }
    return TickResult(waitingFishCount, waitingNewFishCount)
}

data class TickResult(val resetFishCount: Long, val resetNewFishCount: Long)