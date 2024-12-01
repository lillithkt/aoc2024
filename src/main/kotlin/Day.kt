package gay.lilyy.aoc2024
import java.io.File
abstract class Day {
    abstract val day: Int
    abstract suspend fun part1(): Any
    abstract suspend fun part2(): Any
    fun getInput(part: Int? = null): File {
        var exampleFile: File? = null
        var inputFile: File? = null
        if (part != null) {
            exampleFile = File("inputs/$day/$part/example.txt")
            inputFile = File("inputs/$day/$part/input.txt")
        } else {
            exampleFile = File("inputs/$day/example.txt")
            inputFile = File("inputs/$day/input.txt")
        }
        return if (inputFile.exists()) {
            println("Using Real Input")
            inputFile
        } else if (exampleFile.exists()) {
            println("Using Example Input")
            exampleFile
        } else {
            throw Exception("No input file found")
        }
    }
}