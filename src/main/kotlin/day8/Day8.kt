package day8

import readToList

private val input = readToList("day8.txt")

fun main() {
    val validDigits = input.map { line ->
        val (inputStr, digitStr) = line.split(" | ")
        val inputs = inputStr.split(" ").map { Digit(it) }.filter { validLengths.contains(it.sorted.size) }.toSet()
        val digits = digitStr.split(" ").map { Digit(it) }

        digits.filter { inputs.contains(it) }
    }

    println(validDigits.sumOf { it.size })
}

private class Digit(val signal: String) {

    val sorted = signal.toCharArray().sortedArray()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Digit

        if (!sorted.contentEquals(other.sorted)) return false

        return true
    }

    override fun hashCode(): Int {
        return sorted.contentHashCode()
    }

    override fun toString(): String {
        return "Digit(signal='$signal')"
    }


}

private val validLengths = arrayOf(2, 3, 4, 7)
