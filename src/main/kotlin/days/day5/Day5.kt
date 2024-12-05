package gay.lilyy.aoc2024.days.day5

import gay.lilyy.aoc2024.Day
import gay.lilyy.aoc2024.killBOM

@Suppress("unused")
class Day5 : Day() {
    override val day = 5

    var order: List<Pair<Int, Int>> = emptyList()
    var updates: List<List<Int>> = emptyList()

    override suspend fun init() {
        val lines = getInput().readLines().map{ killBOM(it) }
        updates = lines.filter { it.contains(",") }.map { it.split(",").map { it.toInt() } }
        order = lines.filter {
            it.contains("|")
        } .map {
            val split = it.split("|")
            Pair(split[0].toInt(), split[1].toInt())
        }
    }

    private fun isUpdateCorrectlyOrdered(update: List<Int>): Boolean {
        val usedOrderNumbers = order.filter {
            update.contains(it.first) && update.contains(it.second)
        }
        for (number in usedOrderNumbers) {
            if (update.indexOf(number.first) > update.indexOf(number.second)) {
                return false
            }
        }
        return true
    }

    override suspend fun part1(): Int = updates.filter {
            isUpdateCorrectlyOrdered(it)
        }.map {
            it[it.count() / 2]
        }.sum()


    override suspend fun part2(): Int = updates.filter {
            !isUpdateCorrectlyOrdered(it)
        }.map {
            it.sortedWith { a, b ->
                if (order.contains(a to b)) -1 else 1
            }
        }.map {
            it[it.count() / 2]
        }.sum()

}