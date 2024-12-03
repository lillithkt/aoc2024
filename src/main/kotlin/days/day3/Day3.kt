package gay.lilyy.aoc2024.days.day3

import gay.lilyy.aoc2024.Day

@Suppress("unused")
class Day3 : Day() {
    override val day = 3


    enum class Function(val regex: Regex, val execute: (List<String>) -> Any) {
        MUL(Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)"), { it[0].toInt() * it[1].toInt() }),
        DO(Regex("do\\(\\)"), { true }),
        DONT(Regex("don't\\(\\)"), { false }),
    }
    private val allFunctionMatches = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)")

    override suspend fun part1(): Any {
        val input = getInput(1).readText()
        val mulMatches = Function.MUL.regex.findAll(input).toList().map {
            it.groupValues.drop(1)
        }
        val mulResults = mulMatches.map { Function.MUL.execute(it) as Int }
        return mulResults.sum()
    }

    override suspend fun part2(): Any {
        val input = getInput(2).readText()
        var doInst = true // We default to true
        var result = 0
        // iter over all Functions
        for (match in allFunctionMatches.findAll(input)) {
            val function = Function.entries.firstOrNull { it.regex.matches(match.value) } ?: continue
            val args = function.regex.matchEntire(match.value)!!.groupValues.drop(1)
            if (function == Function.DO) {
                doInst = true
            } else if (function == Function.DONT) {
                doInst = false
            } else if (function == Function.MUL && doInst) {
                result += Function.MUL.execute(args) as Int
            }
        }
        return result
    }
}