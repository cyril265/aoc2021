package day3

import readLocalInput

private val input = readLocalInput("day3.txt")
    .map { it.toCharArray() }.toTypedArray()

fun main() {
    println(part1())
}

fun part1(): Int {
    val transposed = transpose(input)

    val gammaBits = transposed.map { column ->
        if (column.count { it == '0' } > column.size / 2) '0' else '1'
    }.toCharArray()
    val epsilonBits = gammaBits.map { if (it == '0') '1' else '0' }.toCharArray()

    val gamma = toInt(gammaBits)
    val epsilon = toInt(epsilonBits)

    return gamma * epsilon
}

private fun toInt(gammaBits: CharArray): Int {
    val toByteArray = String(gammaBits)
    return Integer.parseInt(toByteArray, 2)
}

private fun transpose(source: Array<CharArray>) =
    (source.first().indices).map { i ->
        (source.indices).map { j ->
            source[j][i]
        }
    }
