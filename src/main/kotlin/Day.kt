package gay.lilyy.aoc2024
import java.io.File
import java.io.FileNotFoundException

abstract class Day {
    abstract val day: Int
    abstract suspend fun part1(): Any
    abstract suspend fun part2(): Any
    suspend fun getInput(part: Int? = null): File {
        var exampleFile: File? = null
        var inputFile: File? = null
        if (part != null) {
            exampleFile = File("data/inputs/$day/example$part.txt")
            inputFile = File("data/inputs/$day/input.txt")
        } else {
            exampleFile = File("data/inputs/$day/example.txt")
            inputFile = File("data/inputs/$day/input.txt")
        }
        if (RuntimeStorage.exampleInput) {
            if (exampleFile.exists()) {
                return exampleFile
            } else {
                throw FileNotFoundException("Example file not found!")
            }
        } else {
            if (inputFile.exists()) {
                return inputFile
            } else {
                if (!hasSessionCookie()) {
                    println("Input file not found, defaulting to example")
                    return exampleFile
                } else {
                    println("Input file not found, downloading input")
                    val input = downloadInput(day)
                    if (input != null) {
                        return input
                    } else {
                        println("Failed to download input, defaulting to example")
                        return exampleFile
                    }
                }
            }
        }
    }
}