package gay.lilyy.aoc2024.days.day0

import gay.lilyy.aoc2024.Day
import gay.lilyy.aoc2024.days.day0.Day6.GridToken
import gay.lilyy.aoc2024.days.day0.Day6.GuardFacing
typealias Grid = List<MutableList<Pair<GridToken, GuardFacing?>>>
@Suppress("unused")
class Day6 : Day() {
    override val day = 6

    private val debug = true

    enum class GuardFacing {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    enum class GridToken {
        EMPTY,
        OBSTACLE,
        PLACEDOBSTACLE,
        VISITED,
        GUARD
    }



    private suspend fun getGrid(): Grid = getInput().readLines().map { line ->
        line.toCharArray().map {
            when (it) {
                '.' -> GridToken.EMPTY to null
                '#' -> GridToken.OBSTACLE to null
                '^' -> {
                    GridToken.GUARD to GuardFacing.UP
                }

                'X' -> {
                    GridToken.GUARD to GuardFacing.DOWN
                }

                '<' -> {
                    GridToken.GUARD to GuardFacing.LEFT
                }

                '>' -> {
                    GridToken.GUARD to GuardFacing.RIGHT
                }

                else -> GridToken.EMPTY to null
            }
        }.toMutableList()
    }.toList()

    private fun printGrid(grid: Grid) = if (debug) {
        println("Grid:")
        println(grid.joinToString("\n") { row ->
            row.map {
                when (it.first) {
                    GridToken.EMPTY -> '.'
                    GridToken.OBSTACLE -> '#'
                    GridToken.PLACEDOBSTACLE -> 'O'
                    GridToken.VISITED -> 'X'
                    GridToken.GUARD -> when (it.second) {
                        GuardFacing.UP -> '^'
                        GuardFacing.DOWN -> 'X'
                        GuardFacing.LEFT -> '<'
                        GuardFacing.RIGHT -> '>'
                        else -> throw IllegalStateException("Guard has no facing")
                    }
                }
            }.joinToString("")
        })
    } else {}

    private fun simulateMove(grid: Grid): Grid? {
        while (grid.flatten().find { it.first == GridToken.GUARD } != null) {
            val guardPos = grid.mapIndexed { y, row -> row.mapIndexed { x, grid -> Pair(x, y) to grid } }.flatten()
                .first { it.second.first == GridToken.GUARD }.first
            val guardFacing = grid[guardPos.second][guardPos.first].second ?: throw IllegalStateException("Guard has no facing")
            val moveDirection = when (guardFacing) {
                GuardFacing.UP -> Pair(0, -1)
                GuardFacing.DOWN -> Pair(0, 1)
                GuardFacing.LEFT -> Pair(-1, 0)
                GuardFacing.RIGHT -> Pair(1, 0)
            }
            // Move the guard
//            when (guardFacing) {
//                GuardFacing.UP -> {
//                    // Get tile in front of guard
//                    val tile = grid.getOrNull(guardPos.second - 1)?.getOrNull(guardPos.first)
//                    if (tile == null) {
//                        grid[guardPos.second][guardPos.first] = GridToken.VISITED to GuardFacing.UP
//                        break
//                    }
//                    if (tile.first == GridToken.EMPTY || tile.first == GridToken.VISITED) {
//                        grid[guardPos.second][guardPos.first] = GridToken.VISITED to GuardFacing.UP
//                        if (tile.first == GridToken.VISITED && tile.second == GuardFacing.UP) {
//                            printGrid(grid)
//                            return null
//                        }
//                        grid[guardPos.second - 1][guardPos.first] = GridToken.GUARD to GuardFacing.UP
//                    } else {
//                        grid[guardPos.second][guardPos.first] = GridToken.GUARD to GuardFacing.RIGHT
//                    }
//                }

            val tileInFront = grid.getOrNull(guardPos.second + moveDirection.second)?.getOrNull(guardPos.first + moveDirection.first)
            if (tileInFront == null) {
                grid[guardPos.second][guardPos.first] = GridToken.VISITED to guardFacing
                break
            }
            if (tileInFront.first == GridToken.EMPTY || tileInFront.first == GridToken.VISITED) {
                grid[guardPos.second][guardPos.first] = GridToken.VISITED to guardFacing
                if (tileInFront.first == GridToken.VISITED && tileInFront.second == guardFacing) {
                    printGrid(grid)
                    return null
                }
                grid[guardPos.second + moveDirection.second][guardPos.first + moveDirection.first] = GridToken.GUARD to guardFacing
            } else {
                grid[guardPos.second][guardPos.first] = GridToken.GUARD to when (guardFacing) {
                    GuardFacing.UP -> GuardFacing.RIGHT
                    GuardFacing.DOWN -> GuardFacing.LEFT
                    GuardFacing.LEFT -> GuardFacing.UP
                    GuardFacing.RIGHT -> GuardFacing.DOWN
                }
            }

        }
        return grid
    }

    override suspend fun part1(): Any {
        val grid = simulateMove(getGrid()) ?: throw IllegalStateException("Guard got stuck")

        printGrid(grid)
        return grid.flatten().count { it.first == GridToken.VISITED }
    }

    // Bruteforce but it could be worse
    override suspend fun part2(): Any {
        //Simulate the grid, then put the guard back in the starting position
        val origGrid = getGrid()
        val grid = simulateMove(getGrid()) ?: throw IllegalStateException("Guard got stuck")
        val guardPos = origGrid.mapIndexed { y, row -> row.mapIndexed { x, grid -> Pair(x, y) to grid } }.flatten()
            .first { it.second.first == GridToken.GUARD }.first
        grid[guardPos.second][guardPos.first] = GridToken.GUARD to origGrid[guardPos.second][guardPos.first].second
        var timesStuck = 0

        var times = 0
        val timesOutOf = grid.flatten().count { it.first == GridToken.VISITED }

        for (y in grid.indices) {
            bleh@ for (x in grid[y].indices) {
                if (grid[y][x].first == GridToken.VISITED) {
                    val gridInfrontCoords = when (grid[y][x].second) {
                        GuardFacing.UP -> Pair(x, y - 1)
                        GuardFacing.DOWN -> Pair(x, y + 1)
                        GuardFacing.LEFT -> Pair(x - 1, y)
                        GuardFacing.RIGHT -> Pair(x + 1, y)
                        else -> throw IllegalStateException("Guard has no facing")
                    }
                    val tempGrid = origGrid.map { it.toMutableList() }.toMutableList()
                    val inFront =
                        tempGrid.getOrNull(gridInfrontCoords.second)?.getOrNull(gridInfrontCoords.first)
                    if (inFront == null) {
                        times++
                        println("$times / $timesOutOf")
                        printGrid(tempGrid)
                        continue
                    }
                    if (inFront.first == GridToken.OBSTACLE) {
                        times++
                        println("$times / $timesOutOf")
                        printGrid(tempGrid)
                        continue
                    }
                    val turnDirection = when (grid[y][x].second) {
                        GuardFacing.UP -> GuardFacing.RIGHT
                        GuardFacing.DOWN -> GuardFacing.LEFT
                        GuardFacing.LEFT -> GuardFacing.UP
                        GuardFacing.RIGHT -> GuardFacing.DOWN
                        else -> throw IllegalStateException("Guard has no facing")
                    }
                    val turnCoords = when (turnDirection) {
                        GuardFacing.UP -> Pair(x, y - 1)
                        GuardFacing.DOWN -> Pair(x, y + 1)
                        GuardFacing.LEFT -> Pair(x - 1, y)
                        GuardFacing.RIGHT -> Pair(x + 1, y)
                    }
                    val turn = tempGrid.getOrNull(turnCoords.second)?.getOrNull(turnCoords.first)
                    if (turn == null){
                        times++
                        println("$times / $timesOutOf")
                        printGrid(tempGrid)
                        continue@bleh
                    }
                    var tempCords = turnCoords
                    while2@ while (true) {
                        val turnInFront = tempGrid.getOrNull(tempCords.second)?.getOrNull(tempCords.first)
                        if (turnInFront == null) {
                            times++
                            println("$times / $timesOutOf")
                            printGrid(tempGrid)
                            continue@bleh
                        }
                        if (turnInFront.first == GridToken.OBSTACLE) {
                            break@while2
                        }
                        tempCords = when (turnDirection) {
                            GuardFacing.UP -> Pair(tempCords.first, tempCords.second - 1)
                            GuardFacing.DOWN -> Pair(tempCords.first, tempCords.second + 1)
                            GuardFacing.LEFT -> Pair(tempCords.first - 1, tempCords.second)
                            GuardFacing.RIGHT -> Pair(tempCords.first + 1, tempCords.second)
                        }
                    }
                    tempGrid[gridInfrontCoords.second][gridInfrontCoords.first] = GridToken.PLACEDOBSTACLE to null
                    val result = simulateMove(tempGrid)
                    if (result == null) {
                        println("Guard got stuck at $gridInfrontCoords")
                        timesStuck++
                    }

                    times++
                    println("$times / $timesOutOf")
                }
            }
        }

        return timesStuck
    }
}