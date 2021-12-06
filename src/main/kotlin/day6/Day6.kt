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
    val fishArray = LongArray(7)
    input.forEach {
        fishArray[it]++
    }
    val newFishArray = LongArray(9)

    (1..days).forEach { _ ->
        val first = tick(fishArray)
        val second = tick(newFishArray)

        fishArray[6] = first.resetFishCount + second.resetFishCount
        newFishArray[8] = first.resetNewFishCount + second.resetNewFishCount
    }

    return fishArray.sum() + newFishArray.sum()
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