package day11

import readToMatrix


fun main() {
    println(part1(readInput()))
    println(part2(readInput()))
}

private fun readInput() = StateContainer(
    readToMatrix("day11.txt").map { row -> row.map { item -> Energy(item) }.toMutableList() }
)

private fun part1(input: StateContainer): Int {
    (1..100).forEach { _ ->
        input.tick()
    }
    return input.flashedCount
}

private fun part2(input: StateContainer): Int {
    (1..1000).forEach { step ->
        if (input.tick()) {
            return step
        }
    }
    throw RuntimeException("No solution")
}

private data class StateContainer(val matrix: List<MutableList<Energy>>, var flashedCount: Int = 0) {

    fun tick(): Boolean {
        for ((rowIndex, row) in matrix.withIndex()) {
            for ((columnIndex, value) in row.withIndex()) {
                increaseEnergy(rowIndex, columnIndex)
            }
        }
        this.flashedCount += matrix.flatten().count { energy -> energy.flashed }
        val allFlashed = matrix.flatten().all { energy -> energy.flashed }

        resetFlashes()
        return allFlashed
    }

    private fun resetFlashes() {
        matrix.flatten().forEach { energy -> energy.flashed = false }
    }

    fun increaseEnergy(rowIndex: Int, columnIndex: Int) {
        val energy = get(rowIndex, columnIndex) ?: return
        if (energy.flashed) return

        energy.value = (energy.value + 1) % 10
        if (energy.value == 0) {
            energy.flashed = true
            increaseEnergy(rowIndex + 1, columnIndex)
            increaseEnergy(rowIndex - 1, columnIndex)
            increaseEnergy(rowIndex, columnIndex + 1)
            increaseEnergy(rowIndex, columnIndex - 1)

            increaseEnergy(rowIndex + 1, columnIndex + 1)
            increaseEnergy(rowIndex - 1, columnIndex - 1)
            increaseEnergy(rowIndex + 1, columnIndex - 1)
            increaseEnergy(rowIndex - 1, columnIndex + 1)
        }
    }

    private fun get(rowIndex: Int, columnIndex: Int) = matrix.getOrNull(rowIndex)?.getOrNull(columnIndex)

}

private data class Energy(var value: Int, var flashed: Boolean = false)