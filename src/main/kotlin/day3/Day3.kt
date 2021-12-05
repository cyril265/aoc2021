package day3

import readToList
import kotlin.reflect.KFunction2

private val input = readToList("day3.txt")
    .map { it.toCharArray() }.toTypedArray()

fun main() {
    println(part1())
    println(part2())
}

fun part1(): Int {
    val transposed = transpose(input)

    val gammaBits = transposed.map { column ->
        if (column.count { it == '0' } > column.size / 2) '0' else '1'
    }.toCharArray()
    val epsilonBits = inverse(gammaBits)

    val gamma = toInt(gammaBits)
    val epsilon = toInt(epsilonBits)

    return gamma * epsilon
}

private fun inverse(gammaBits: CharArray) = gammaBits.map { if (it == '0') '1' else '0' }.toCharArray()

fun part2(): Int {
    val n1 = filterReport(::mostCommon)
    val n2 = filterReport(::leastCommon)

    return n1 * n2
}

private fun filterReport(decider: KFunction2<List<Char>, Char, Boolean>): Int {
    var result = input
    var index = 0

    while (result.size != 1) {
        val transposed = transpose(result)
        val column = transposed[index]
        result = if (decider(column, '1')) {
            filterNumber(result, index, '1')
        } else {
            filterNumber(result, index, '0')
        }
        index++
    }

    return toInt(result[0])
}

private fun mostCommon(column: List<Char>, bit: Char) = column.count { it == bit } >= column.size / 2.0
private fun leastCommon(column: List<Char>, bit: Char) = !mostCommon(column, bit)


private fun filterNumber(
    currentInput: Array<CharArray>,
    index: Int,
    bit: Char
) = currentInput.filter { row ->
    row[index] == bit
}.toTypedArray()

private fun toInt(bits: CharArray): Int {
    return Integer.parseInt(String(bits), 2)
}

private fun transpose(source: Array<CharArray>) =
    (source.first().indices).map { i ->
        (source.indices).map { j ->
            source[j][i]
        }
    }
