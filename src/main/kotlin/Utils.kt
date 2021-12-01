import java.math.BigInteger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
private val client = HttpClient.newHttpClient();

fun readInput(day: String): List<String> {
    val sessionId = System.getenv("session")
    val request = HttpRequest.newBuilder(URI.create("https://adventofcode.com/2021/day/$day/input"))
        .GET()
        .header(
            "cookie",
            "session=$sessionId"
        )
        .build();
    return client.send(request, HttpResponse.BodyHandlers.ofLines())
        .body()
        .toList();

}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
