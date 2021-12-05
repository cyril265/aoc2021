package day4

import read
import readToList

val x = readToList("day4_input.txt")


fun main() {
    println(part1())
}

private fun part1(): Int {
    val input = x.first().split(',')
    val boardLines = read("day4_board.txt").split("\r\n\r\n")

    val boards = boardLines
        .map { line ->
            line.split("\r\n")
                .filter { it.isNotBlank() }
                .map { it.split(' ').filter { it.isNotBlank() }.map(::Cell) }
        }
        .map(::Board)

    input.forEach { number ->
        boards.forEach { board ->
            board.board.forEach { line ->
                line.forEach { cell ->
                    if (number == cell.value) {
                        cell.matched = true
                    }
                }
            }
            if (board.matches()) {
                val sum = board.board.sumOf { x -> x.filter { !it.matched }.sumOf { it.value.toInt() } }
                return@part1 sum * number.toInt()
            }
        }
    }
    return 0
}

data class Cell(val value: String, var matched: Boolean = false)
data class Board(val board: List<List<Cell>>) {

    fun matches(): Boolean {
        return board.any { line -> line.all { it.matched } } ||
            transpose(board).any { line -> line.all { it.matched } }
    }

}

private fun transpose(source: List<List<Cell>>) =
    (source.first().indices).asSequence().map { i ->
        (source.indices).map { j ->
            source[j][i]
        }
    }
