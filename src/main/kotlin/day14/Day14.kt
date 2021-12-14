package day14

import readToList
import java.util.UUID


private val input = readToList("day14.txt")

fun main() {
    val template = input.first()
        .mapIndexed { index, char -> Element(char, index.toString()) }
    val rules = input.drop(2)
        .map { line ->
            val (pair, insertion) = line.split(" -> ")
            Rule(pair.first() to pair[1], insertion[0])
        }

    var templateResult = template

    (0 until 10).forEach { step ->
        println(step)
        templateResult = templateResult.mapIndexed { index, element ->
            if (index == templateResult.size - 1) {
                null
            } else {
                rules.map { rule ->
                    val (first, second) = rule.pair
                    val nextElement = templateResult[index + 1]
                    if (element.char == first && nextElement.char == second) {
                        listOf(element, Element(rule.insertion, UUID.randomUUID().toString()), nextElement)
                    } else {
                        listOf(element)
                    }
                }
                    .flatten()
                    .distinct()
            }
        }.filterNotNull()
            .flatten()
            .distinct()

        templateResult.forEachIndexed { index, el -> el.id = index.toString() }
    }

    println(templateResult.joinToString(separator = ""))
    val message = templateResult.groupingBy { it.char }.eachCount()
    println(
        message.values.maxOf { it } - message.values.minOf { it }
    )

}

private data class Rule(val pair: Pair<Char, Char>, val insertion: Char)

private data class Element(val char: Char, var id: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Element

        if (id != other.id) return false

        return true
    }

    override fun toString(): String {
        return char.toString()
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
