package gay.lilyy.aoc2024.days.day2

import gay.lilyy.aoc2024.Day
import gay.lilyy.aoc2024.mapNotException

@Suppress("unused")
class Day2 : Day() {
    override val day = 2

    private fun getReports(): List<List<Int>> = getInput().readLines().map {
        it.split(" ").map {
            String(it.toByteArray(Charsets.ISO_8859_1)).split("").mapNotException { it.toInt() }.joinToString("").toInt()
        }
    }
    override suspend fun part1(): Any {
        val reports = getReports()
        return reports.filter { isReportSafe(it) }.count()
    }

    override suspend fun part2(): Any {
        val reports = getReports()
        var safeReports = 0
        forReport@ for (report in reports.map {it.toMutableList()}) {
            var reportSafe = false


            forVal@ for (i in 0 until report.size - 1) {

                if (!isReportSafe(report)) {
                    val test1 = report.toMutableList()
                    test1.removeAt(i)
                    if (!isReportSafe(test1)) {
                        val test2 = report.toMutableList()
                        test2.removeAt(i + 1)
                        if (isReportSafe(test2)) {
                            reportSafe = true
                            break@forVal
                        }
                    } else {
                        reportSafe = true
                        break@forVal
                    }
                } else {
                    reportSafe = true
                    break@forVal
                }
            }
            if (reportSafe) {
                safeReports++
            }
        }
        return safeReports
    }

    private fun isReportSafe(report: List<Int>): Boolean {
        val increasing = report[0] < report[1]
        var reportSafe = true
        for (i in 0 until report.size - 1) {
            if (increasing == report[i] < report[i + 1]) {
                // Two numbers match increasing
                val difference = (report[i + 1] - report[i]) * (if (increasing) 1 else -1)
                if (difference < 1 || difference > 3) {
                    reportSafe = false
                    break
                }
            } else {
                // Two numbers do not match increasing
                reportSafe = false
                break
            }
        }
        return reportSafe
    }
}