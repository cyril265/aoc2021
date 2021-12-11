package day8

import readToList

private val input = readToList("day8.txt")

fun main() {
    input.forEach { line ->
        val (inputStr, digitStr) = line.split(" | ")
        val inputs = inputStr.split(" ").map { Digit(it) }.toSet()
        val digits = digitStr.split(" ").map { Digit(it) }

        println(inputs.size)
    }


}

private fun compare(a: String, b: String): Boolean {
    return a.toCharArray().sortedArray().contentEquals(b.toCharArray().sortedArray())
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


}
