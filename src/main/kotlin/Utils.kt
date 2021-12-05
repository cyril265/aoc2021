import java.math.BigInteger
import java.net.URI
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
private val cookie = readResource("cookie")


fun readToList(name: String): List<String> {
    return read(name).lines()
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
