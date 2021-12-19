import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.streams.toList

/**
 * Reads lines from the given input txt file.
 */
private val cookie = readResource("cookie")


fun readToList(name: String): List<String> {
    return read(name).lines()
}

fun readToMatrix(name: String): List<MutableList<Int>> {
    return read(name)
        .lines()
        .map { line ->
            line.chars().map { char -> Character.getNumericValue(char) }.toList().toMutableList()
        }.toList()
}

fun read(name: String): String {
    return readResource(name) ?: throw RuntimeException("Could not read $name")
}


private fun readResource(name: String) = object {}.javaClass.getResource("./$name")
    ?.readText()
    ?.trim()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun <T> Stack<T>.popUntil(predicate: (T) -> Boolean): List<T> {
    val result = mutableListOf<T>()
    do {
        if (this.isEmpty()) return result
        val el = this.pop()
        result.add(el)
    } while (predicate(el).not())
    return result
}