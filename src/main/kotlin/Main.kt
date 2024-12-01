package gay.lilyy.aoc2024

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import org.reflections.Reflections
import kotlin.system.measureNanoTime

suspend fun main(args: Array<String>) {
    val days = Reflections("gay.lilyy.aoc2024.days").getSubTypesOf(Day::class.java).map { it.getDeclaredConstructor().newInstance()}
    val daysSorted = days.sortedBy { it.day.toInt() }

    if (days.isEmpty()) {
        println("No days found!")
        return
    }
    var dayNum: Int? = null
    var partNum: Int? = null
    if (args.isEmpty()) {
        println("No day specified, defaulting to latest day")
        dayNum = daysSorted.last().day
        partNum = 0
    }

    if (dayNum == null) {
        dayNum = args[0].toInt()
    }
    if (args.size > 1) {
        partNum = args[1].toInt()
    }

    if (partNum == null) {
        println("No part specified, defaulting to both parts")
        partNum = 0
    }

    if (partNum < 0 || partNum > 2) {
        println("Invalid part number!")
        return
    }

    var daysToRun: List<Day> = listOf()
    if (dayNum == 0) {
        println("Running All Days")
        daysToRun = daysSorted
    } else {
        daysToRun = daysSorted.filter { it.day.toInt() == dayNum.toInt() }
    }


    var totalTime = 0L
    for (day in daysToRun) {
        println("========= Day ${day.day} =========")

        var dayTime = 0L
        if (partNum == 0 || partNum == 1) {
            println("===== Part 1 =====")
            var result: Any? = null
            val part1Time = measureNanoTime {
                result = day.part1()
            }
            println("Result: $result")
            println("Time: $part1Time ns")
            dayTime += part1Time
        }
        if (partNum == 0 || partNum == 2) {
            println("===== Part 2 =====")
            var result: Any? = null
            val part2Time = measureNanoTime {
                result = day.part2()
            }
            println("Result: $result")
            println("Time: $part2Time ns")
            dayTime += part2Time
        }
        println("Total Time: $dayTime ns")
        totalTime += dayTime
    }

    println("========= Total Time: $totalTime ns =========")
}