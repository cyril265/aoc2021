package day6

import readToList

val input = readToList("day6.txt")
    .first()
    .split(",")
    .map { it.toInt() }

fun main() {
    fishIt(256)
}

private fun fishIt(days: Int) {
    val fishArray = LongArray(7)
    input.forEach {
        fishArray[it]++
    }

    val newFishArray = LongArray(9)

    (1..days).forEach { _ ->
        var waitingFishCount = 0L
        var waitingNewFish = 0L

        fishArray.forEachIndexed { dayIndex, fishCount ->
            if (dayIndex == 0) {
                fishArray[0] -= fishCount
                waitingFishCount += fishCount
                waitingNewFish += fishCount
            } else {
                fishArray[dayIndex] -= fishCount
                fishArray[dayIndex - 1] += fishCount
            }
        }

        newFishArray.forEachIndexed { dayIndex, fishCount ->
            if (dayIndex == 0) {
                newFishArray[0] -= fishCount
                waitingFishCount += fishCount
                waitingNewFish += fishCount
            } else {
                newFishArray[dayIndex] -= fishCount
                newFishArray[dayIndex - 1] += fishCount
            }
        }

        fishArray[6] = waitingFishCount
        newFishArray[8] = waitingNewFish
    }

    println(fishArray.sum() + newFishArray.sum())
}
