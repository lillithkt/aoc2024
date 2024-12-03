package gay.lilyy.aoc2024

import org.reflections.Reflections
import kotlin.system.measureTimeMillis

object RuntimeStorage {
    var exampleInput = false
}
suspend fun main(argsArray: Array<String>) {
    val args = argsArray.toMutableList()
    val days = Reflections("gay.lilyy.aoc2024.days").getSubTypesOf(Day::class.java).map { it.getDeclaredConstructor().newInstance()}
    val daysSorted = days.sortedBy { it.day.toInt() }

    if (days.isEmpty()) {
        println("No days found!")
        return
    }
    var dayNum: Int? = null
    var partNum: Int? = null

    if (args.contains("--example") || args.contains("-e")) {
        RuntimeStorage.exampleInput = true
        args.remove("--example")
        args.remove("-e")
    }

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
            val part1Time = measureTimeMillis {
                result = day.part1()
            }
            println("Result: $result")
            println("Time: $part1Time ms")
            dayTime += part1Time
        }
        if (partNum == 0 || partNum == 2) {
            println("===== Part 2 =====")
            var result: Any? = null
            val part2Time = measureTimeMillis {
                result = day.part2()
            }
            println("Result: $result")
            println("Time: $part2Time ms")
            dayTime += part2Time
        }
        println("Total Time for Day ${day.day}: $dayTime ms")
        totalTime += dayTime
    }

    println("========= Total Time: $totalTime ms =========")
}