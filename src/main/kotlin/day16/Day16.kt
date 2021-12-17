package day16

import readToList
import java.util.Stack

private val input = readToList("day16.txt").first()

fun main() {
    val map = input.map { char ->
        toBits(char)
    }.flatMap { it.toList() }.toMutableList()


    val packets = getPacket(map, null)
    println(packets)
//    println(packets.sumOf { it.version })
//
//    val stack = Stack<Node>()
//    packets.reversed().forEach { packet ->
//        if (packet.operation != null) {
//            val leaves = stack.popWhile { it.current.depth - 1 == packet.depth }
//            stack.push(Node(packet, leaves))
//        } else {
//            stack.push(Node(packet))
//        }
//    }
//    val message = evaluate(stack.first())
//    println(message)
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
            return "operation=${current.operation}, children=$children, children=${
                current.children.filterNotNull().map { it.operation ?: it.numericValue }
            }"
        } else {
            return "numeric=${current.numericValue}, children=$children, children=${
                current.children.filterNotNull().map { it.operation ?: it.numericValue }
            }"
        }
    }

}


private fun getPacket(bits: MutableList<Char>, limit: Long? = null): List<Packet> {
    if (bits.all { it == '0' }) return emptyList()

    val (version, typeId) = getVersionType(bits)
    val opi = Operation.create(typeId)

    val result = mutableListOf<Packet>()

    while (bits.isNotEmpty()) {
        val packetSimple = getPacketSimple(bits) ?: break
        result.add(packetSimple)
        if (packetSimple.numberOfPackets != null) {
            packetSimple.children += (1..packetSimple.numberOfPackets).map { getPacketSimple(bits) }
        }
    }
    return result
//    if (typeId == 4L) {
//        return if (limit != null) {
//            (0 until limit).map { Packet(version, typeId, mapNumber(bits)) }
//        } else {
//            listOf(Packet(version, typeId, mapNumber(bits))) + getPacket(bits, null)
//        }
//    } else {
//        val (lengthType) = bits.consume(1)
//
//        if (lengthType == '0') {
//            val totalLengthBits = bits.consume(15)
//            val totalLength = toLong(totalLengthBits)
//
//            val newParent = Packet(version, typeId, totalLengthBits)
//            println(newParent.operation)
//
//            if (limit != null) {
//                newParent.children += getPacket(bits.consume(totalLength))
//                val newLimit = limit - 1
//                newParent.children += (0 until newLimit).flatMap { getPacket(bits) }
//            } else {
//                newParent.children += getPacket(bits.consume(totalLength))
//                newParent.children += getPacket(bits)
//            }
//
//            return listOf(newParent)
//        } else {
//            val content = bits.consume(11)
//            val numberOfPackets = toLong(content)
//            val newParent = Packet(version, typeId, content)
//            println(newParent.operation)
//            newParent.children += newParent
//            newParent.children += getPacket(bits, numberOfPackets)
//            return listOf(newParent)
//        }
//    }

}

private fun getPacketSimple(bits: MutableList<Char>): Packet? {
    if (bits.all { it == '0' }) return null

    val (version, typeId) = getVersionType(bits)
    val opi = Operation.create(typeId)

    if (typeId == 4L) {
        Packet(version, typeId, mapNumber(bits))
    } else {
        val (lengthType) = bits.consume(1)

        if (lengthType == '0') {
            val totalLengthBits = bits.consume(15)
            val totalLength = toLong(totalLengthBits)

            return Packet(version, typeId, totalLengthBits)
        } else {
            val content = bits.consume(11)
            val numberOfPackets = toLong(content)
            return Packet(version, typeId, content, numberOfPackets)
        }
    }
    return null

}

private fun getVersionType(bits: MutableList<Char>): Pair<Long, Long> {
    val version = toLong(bits.consume(3))
    val typeId = toLong(bits.consume(3))
    return Pair(version, typeId)
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

private class Packet(
    val version: Long,
    typeId: Long,
    content: MutableList<Char>,
    val numberOfPackets: Long? = null
) {

    var children: MutableList<Packet?> = mutableListOf()
    val operation = Operation.create(typeId)
    val numericValue = toLong(content)
    override fun toString(): String {
        return "Packet(operation=$operation, numericValue=$numericValue, children=${
            children.filterNotNull().map { it.operation ?: it.numericValue }
        })"
    }


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

private fun <T> Stack<T>.drain(): List<T> {
    val result = mutableListOf<T>()
    while (this.isNotEmpty()) {
        result.add(this.pop())
    }
    return result
}

private fun <T> Stack<T>.popWhile(predicate: (T) -> Boolean): List<T> {
    val result = mutableListOf<T>()
    while (this.isNotEmpty() && predicate(this.peek())) {
        result.add(this.pop())
    }
    return result
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
