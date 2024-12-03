package gay.lilyy.aoc2024

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import java.io.File

fun getSessionCookie(): String? {
    val cookieFile = File("data/sessioncookie.txt")
    return if (cookieFile.exists()) {
        var cookie = cookieFile.readText().replace("\uFEFF", "")
        if (!cookie.startsWith("session=")) {
            cookie = "session=$cookie"
        }
        cookie
    } else {
        null
    }
}

fun hasSessionCookie(): Boolean {
    val cookieFile = File("data/sessioncookie.txt")
    return cookieFile.exists()
}

suspend fun downloadInput(day: Int): File? {
    val client = HttpClient(CIO)
    val cookie = getSessionCookie()
    if (cookie == null) {
        println("No session cookie found, please add it to data/sessioncookie.txt")
        return null
    }
    println(cookie)
    val res = client.get("https://adventofcode.com/2024/day/$day/input") {
        header("Cookie", cookie)
    }
    val input = res.bodyAsText()
    val inputFile = File("data/inputs/$day/input.txt")
    inputFile.writeText(input)
    return inputFile
}