package gay.lilyy.aoc2024.days.day4

import gay.lilyy.aoc2024.Day

@Suppress("unused")
class Day4 : Day() {
    override val day = 4

    val debug = false
    override suspend fun part1(): Any {
        // get lines as List<List<Char>> where Char is lowercase
        val lines = getInput().readLines().map { it.toList().map { it.lowercaseChar() } }

        var xmasCount = 0
        for (lineNum in lines.indices) {
            val line = lines[lineNum]
            for (charNum in line.indices) {
                val char = line[charNum]
                if (char == 'x') {
                    // Left to Right
                    if (charNum + 3 <= line.count() - 1) {
                        if (line[charNum + 1] == 'm' && line[charNum + 2] == 'a' && line[charNum + 3] == 's') {
                            xmasCount++
                            if (debug) println("Found XMAS at $charNum, $lineNum (L to R)")
                        }
                    }
                    // Right to Left
                    if (charNum - 3 >= 0) {
                        if (line[charNum - 1] == 'm' && line[charNum - 2] == 'a' && line[charNum - 3] == 's') {
                            xmasCount++
                            if (debug) println("Found XMAS at $charNum, $lineNum (R to L)")
                        }
                    }
                    // Top to Bottom
                    if (lineNum + 3 <= lines.count() - 1) {
                        if (lines[lineNum + 1][charNum] == 'm' && lines[lineNum + 2][charNum] == 'a' && lines[lineNum + 3][charNum] == 's') {
                            xmasCount++
                            if (debug) println("Found XMAS at $charNum, $lineNum (T to B)")
                        }
                        // Top Left to Bottom Right
                        if (charNum + 3 <= line.count() - 1) {
                            if (lines[lineNum + 1][charNum + 1] == 'm' && lines[lineNum + 2][charNum + 2] == 'a' && lines[lineNum + 3][charNum + 3] == 's') {
                                xmasCount++
                                if (debug) println("Found XMAS at $charNum, $lineNum (TL to BR)")
                            }
                        }
                        // Top Right to Bottom Left
                        if (charNum - 3 >= 0) {
                            if (lines[lineNum + 1][charNum - 1] == 'm' && lines[lineNum + 2][charNum - 2] == 'a' && lines[lineNum + 3][charNum - 3] == 's') {
                                xmasCount++
                                if (debug) println("Found XMAS at $charNum, $lineNum (TR to BL)")
                            }
                        }
                    }
                    // Bottom to Top
                    if (lineNum - 3 >= 0) {
                        if (lines[lineNum - 1][charNum] == 'm' && lines[lineNum - 2][charNum] == 'a' && lines[lineNum - 3][charNum] == 's') {
                            xmasCount++
                            if (debug) println("Found XMAS at $charNum, $lineNum (B to T)")
                        }
                        // Bottom Left to Top Right
                        if (charNum + 3 <= line.count() - 1) {
                            if (lines[lineNum - 1][charNum + 1] == 'm' && lines[lineNum - 2][charNum + 2] == 'a' && lines[lineNum - 3][charNum + 3] == 's') {
                                xmasCount++
                                if (debug) println("Found XMAS at $charNum, $lineNum (BL to TR)")
                            }
                        }
                        // Bottom Right to Top Left
                        if (charNum - 3 >= 0) {
                            if (lines[lineNum - 1][charNum - 1] == 'm' && lines[lineNum - 2][charNum - 2] == 'a' && lines[lineNum - 3][charNum - 3] == 's') {
                                xmasCount++
                                if (debug) println("Found XMAS at $charNum, $lineNum (BR to TL)")
                            }
                        }
                    }
                }
            }
        }
        return xmasCount
    }

    override suspend fun part2(): Any {
        val lines = getInput().readLines().map {
            it.toList().map { it.lowercaseChar() }.mapNotNull {
                // i hate bom with a passion
                if (it == 0xFEFF.toChar()) {
                    return@mapNotNull null
                }
                it
            }
        }

        var xMasCount = 0
        for (lineNum in lines.indices) {
            if (lineNum == 0 || lineNum == lines.count() - 1) continue // Skip first and last line
            val line = lines[lineNum]
            for (charNum in line.indices) {
                if (charNum == 0 || charNum == line.count() - 1) continue // Skip first and last char
                val char = line[charNum]
                if (char == 'a') {
                    if ((lines[lineNum - 1][charNum - 1] == 'm' && lines[lineNum + 1][charNum + 1] == 's') ||
                        (lines[lineNum - 1][charNum - 1] == 's' && lines[lineNum + 1][charNum + 1] == 'm')
                    ) {
                        if ((lines[lineNum - 1][charNum + 1] == 'm' && lines[lineNum + 1][charNum - 1] == 's') ||
                            (lines[lineNum - 1][charNum + 1] == 's' && lines[lineNum + 1][charNum - 1] == 'm')
                        ) {
                            xMasCount++
                            if (debug) println("Found X-MAS at $charNum, $lineNum")
                        }
                    }
                }
            }
        }
        return xMasCount
    }
}