package day16

import readToList
import java.util.Stack

private val input = readToList("day16.txt").first()

fun main() {
    val map = input.map { char ->
        toBits(char)
    }.flatMap { it.toList() }.toMutableList()


    println(map.toCharArray().concatToString())

    val packets = getPacket(map)
    println(packets.sumOf { it.version })

    val stack = Stack<Node>()
    packets.reversed().forEach { packet ->
        if (packet.operation != null) {
            val leafs = stack.takeWhile { it.current.operation == null }
            stack.push(Node(packet, leafs))
            stack.consume(leafs.count().toLong())
        } else {
            stack.push(Node(packet))
        }
    }
    val message = evaluate(stack.first())
    println(message)
}


private fun evaluate(tree: Node): Long {
    if (tree.hasChildren()) {
        if (tree.current.operation != null) {
            println(tree.current.operation)
            val evaluate = tree.children.map { evaluate(it) }.reversed()
            return tree.current.operation.execute(evaluate)
        }
    }
    return tree.current.numericValue
}

private data class Node(val current: Packet, val children: List<Node> = emptyList()) {
    fun hasChildren(): Boolean {
        return children.isNotEmpty()
    }

    override fun toString(): String {
        if (current.operation != null) {
            return "operation=${current.operation}, children=$children"
        } else {
            return "numeric=${current.numericValue}, children=$children"
        }
    }


}


private fun getPacket(bits: MutableList<Char>): List<Packet> {
    if (bits.all { it == '0' }) return emptyList()

    val version = toLong(bits.consume(3))
    val typeId = toLong(bits.consume(3))
    if (typeId == 4L) {
        val container = mutableListOf(Packet(version, typeId, mapNumber(bits)))
        container += getPacket(bits)
        return container
    } else {
        val (lengthType) = bits.consume(1)
        if (lengthType == '0') {
            val totalLengthBits = bits.consume(15)
            val totalLength = toLong(totalLengthBits)

            val container = mutableListOf<Packet>()
            container += Packet(version, typeId, totalLengthBits)
            container += getPacket(bits.consume(totalLength))
            container += getPacket(bits)
            return container
        } else {
            val content = bits.consume(11)
            val numberOfPackets = toLong(content)
            val resultContainer = mutableListOf<Packet>()
            resultContainer += Packet(version, typeId, content)
            resultContainer += getPacket(bits).toMutableList()
            return resultContainer
        }
    }

}

private fun mapNumber(
    bits: MutableList<Char>
): MutableList<Char> {
    val result = mutableListOf<Char>()
    do {
        val number = bits.consume(5)
        val (prefix) = number.consume(1)
        result.addAll(number)
    } while (prefix != '0')
    return result
}

private class Packet(val version: Long, val typeId: Long, content: MutableList<Char>) {
    val operation = Operation.create(typeId)
    val numericValue = toLong(content)

}


private interface Operation {
    fun execute(numbers: List<Long>): Long

    companion object {
        fun create(typeId: Long): Operation? {
            return when (typeId) {
                0L -> Sum()
                1L -> Product()
                2L -> Min()
                3L -> Max()
                5L -> GreaterThan()
                6L -> LessThan()
                7L -> EqualTo()
                4L -> null
                else -> TODO()
            }
        }
    }
}


class Sum : Operation {
    override fun execute(numbers: List<Long>): Long {
        return numbers.sum()
    }
}

class Product : Operation {
    override fun execute(numbers: List<Long>): Long {
        return numbers.reduce { acc, num -> num * acc }
    }

}

class Min : Operation {
    override fun execute(numbers: List<Long>): Long {
        return numbers.minOf { it }
    }

}

class Max : Operation {
    override fun execute(numbers: List<Long>): Long {
        return numbers.maxOf { it }
    }

}

class GreaterThan : Operation {
    override fun execute(numbers: List<Long>): Long {
        val (a, b) = numbers
        return if (a > b) 1 else 0
    }

}

class LessThan : Operation {
    override fun execute(numbers: List<Long>): Long {
        val (a, b) = numbers
        return if (a < b) 1 else 0
    }

}

class EqualTo : Operation {
    override fun execute(numbers: List<Long>): Long {
        val (a, b) = numbers
        return if (a == b) 1 else 0
    }

}


private fun toLong(bits: List<Char>): Long {
    return String(bits.toCharArray()).toLong(2)
}

private fun <T> MutableList<T>.consume(count: Long): MutableList<T> {
    return (0 until count).map { _ -> this.removeFirst() }.toMutableList()
}

private fun toBits(char: Char) = when (char) {
    '0' -> "0000"
    '1' -> "0001"
    '2' -> "0010"
    '3' -> "0011"
    '4' -> "0100"
    '5' -> "0101"
    '6' -> "0110"
    '7' -> "0111"
    '8' -> "1000"
    '9' -> "1001"
    'A' -> "1010"
    'B' -> "1011"
    'C' -> "1100"
    'D' -> "1101"
    'E' -> "1110"
    'F' -> "1111"
    else -> TODO("yikes")
}
