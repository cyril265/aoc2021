package day4

import read
import readToList


val input = readToList("day4_input.txt").first().split(',')
val boardLines = read("day4_board.txt").split("\r\n\r\n")


fun main() {
    println(play(input, true))
    println(play(input, false))
}

private fun play(input: List<String>, matchFirst: Boolean): Int {
    val boards = toBoards()

    input.forEach { number ->
        boards.forEach { board ->
            board.board.forEach { line ->
                line.forEach { cell ->
                    if (number == cell.value) {
                        cell.matched = true
                    }
                }
            }
            if (matchFirst && board.matches() || boards.all { it.matches() }) {
                return@play board.sumUnmatched() * number.toInt()
            }
        }
    }
    throw RuntimeException("Invalid solution")
}

private fun toBoards(): List<Board> {
    val boards = boardLines
        .map { line ->
            line.split("\r\n")
                .map { it.split(' ').filter { it.isNotBlank() }.map(::Cell) }
        }
        .map(::Board)
    return boards
}


data class Cell(val value: String, var matched: Boolean = false)
data class Board(val board: List<List<Cell>>) {

    fun matches(): Boolean {
        return board.any { line -> line.all { it.matched } } ||
                transpose(board).any { line -> line.all { it.matched } }
    }

    fun sumUnmatched(): Int {
        return board.sumOf { x -> x.filter { !it.matched }.sumOf { it.value.toInt() } }
    }

}

private fun transpose(source: List<List<Cell>>) =
    (source.first().indices).map { i ->
        (source.indices).map { j ->
            source[j][i]
        }
    }
