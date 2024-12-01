package gay.lilyy.aoc2024.days.day1

import gay.lilyy.aoc2024.Day

@Suppress("unused")
class Day1 : Day() {
    override val day = 1

    private fun getLists(): Pair<MutableList<Int>, MutableList<Int>> {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()
        val input = getInput().readLines()
        for (line in input) {
            val match = Regex("(\\d+)\\s+(\\d+)").find(line)!!
            leftList.add(match.groupValues[1].toInt())
            rightList.add(match.groupValues[2].toInt())
        }
        val leftListSorted = leftList.sorted().toMutableList()
        val rightListSorted = rightList.sorted().toMutableList()
        return Pair(leftListSorted, rightListSorted)
    }
    override suspend fun part1(): Any {
        val (leftList, rightList) = getLists()
        val listLength = leftList.size
        val pairs: MutableList<Pair<Int, Int>> = mutableListOf()
        for (i in 0 until listLength) {
            val smallestLeft = leftList.first()
            leftList.remove(smallestLeft)
            val smallestRight = rightList.first()
            rightList.remove(smallestRight)
            pairs.add(Pair(smallestLeft, smallestRight))
        }
        val apart = mutableListOf<Int>()
        for (pair in pairs) {
            if (pair.first > pair.second) {
                apart.add(pair.first - pair.second)
            } else {
                apart.add(pair.second - pair.first)
            }
        }
        val sum = apart.sum()
        return sum
    }

    override suspend fun part2(): Any {
        val (leftList, rightList) = getLists()
        val listLength = leftList.size
        val similarityScores = mutableListOf<Int>()
        for (i in 0 until listLength) {
            val left = leftList[i]
            val allRight = rightList.filter { it == left }
            similarityScores.add(left * allRight.size)
        }
        return similarityScores.sum()
    }
}