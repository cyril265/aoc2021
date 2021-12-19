package day18

import kotlin.math.ceil
import kotlin.math.floor


data class CoolPair(var left: PairComponent, var right: PairComponent) : PairComponent {

    operator fun plus(other: CoolPair): CoolPair {
        val newPair = CoolPair(this, other)
        reduce(newPair)
        return newPair
    }

    fun explode(): Boolean {
        val digitList = digits()
        val pairToExplode = digitList
            .firstOrNull { it.depth >= 4 }
            ?.parent
        if (pairToExplode != null) {
            val leftIndex = digitList.indexOfFirst { it.pair === pairToExplode.left }
            val leftDigit = digitList.getOrNull(leftIndex - 1)?.pair
            val rightDigit = digitList.getOrNull(leftIndex + 2)?.pair

            if (leftDigit != null) {
                leftDigit.number += (pairToExplode.left as Digit).number
            }
            if (rightDigit != null) {
                rightDigit.number += (pairToExplode.right as Digit).number
            }

            val parent = parentOf(this, pairToExplode)
            if (parent != null) {
                val zeroDigit = Digit(0)
                if (parent.left === pairToExplode) parent.left = zeroDigit
                else if (parent.right == pairToExplode) parent.right = zeroDigit
            }
        }
        return pairToExplode != null
    }

    fun split(): Boolean {
        val digitList = digits().map { it.pair }
        val digit = digitList.firstOrNull { it.number >= 10 }
        if (digit != null) {
            val divisionResult = digit.number / 2.0
            val left = floor(divisionResult).toInt()
            val right = ceil(divisionResult).toInt()

            val parent = parentOf(this, digit)
            val newPair = CoolPair(Digit(left), Digit(right))
            checkNotNull(parent) { "Parent shall not be null" }
            if (parent.left === digit) {
                parent.left = newPair
            } else if (parent.right === digit) {
                parent.right = newPair

            }
        }
        return digit != null
    }

    override fun toString(): String {
        return "[$left, $right]"
    }

    private fun digits() = digits(this)
    fun magnitude() = magnitude(this)
    fun reduce() = reduce(this)

}

private fun reduce(pair: CoolPair) {
    while (pair.explode() || pair.split()) {
    }
}

private fun digits(el: PairComponent, depth: Int = -1, parent: PairComponent? = null): List<PairDepth> {
    val result = mutableListOf<PairDepth>()
    val newDepth = depth + 1
    return if (el is CoolPair) {
        result += digits(el.left, newDepth, el)
        result += digits(el.right, newDepth, el)
        result
    } else {
        listOf(PairDepth(el as Digit, depth, parent as CoolPair))
    }
}

private fun magnitude(pair: CoolPair): Long {
    val left = pair.left
    val right = pair.right
    val leftResult = if (left is Digit) {
        left.number * 3L
    } else {
        magnitude(left as CoolPair) * 3L
    }
    val rightResult = if (right is Digit) {
        right.number * 2L
    } else {
        magnitude(right as CoolPair) * 2L
    }
    return leftResult + rightResult
}

private fun parentOf(current: CoolPair, el: PairComponent): CoolPair? {
    if (current.left === el || current.right === el) return current

    if (current.right is CoolPair) {
        val parent = parentOf(current.right as CoolPair, el)
        if (parent != null) {
            return parent
        }
    }
    if (current.left is CoolPair) {
        val parent = parentOf(current.left as CoolPair, el)
        if (parent != null) {
            return parent
        }
    }
    return null
}

private data class PairDepth(val pair: Digit, val depth: Int, val parent: CoolPair)