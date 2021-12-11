import java.nio.file.Paths

fun main() {
    val input = Paths.get("src", "test", "kotlin", "day11.txt").toFile().readText()

    day11Part1(to2DArray(input))
    day11Part2(to2DArray(input))
}

fun day11Part1(energyLevels: Array<IntArray>) {
    val numberOfFlashes = (0 until 100)
        .map { step(energyLevels) }
        .sumOf { it.flashesCount }

    println("Part1: ${numberOfFlashes}")
}

fun day11Part2(energyLevels: Array<IntArray>) {
    var index = 0
    var allFlashed = false
    val matrixSize = energyLevels.size * energyLevels[0].size
    while (index < 1000 && !allFlashed) {
        if (step(energyLevels).flashesCount == matrixSize) {
            allFlashed = true
        }
        index++
    }

    println("Part2: $index")
}

fun step(energyLevels: Array<IntArray>): StepEffects {
    val flashedThisStep = mutableSetOf<OctopusPos>()

    // increment capped
    for ((row, col) in allPositions(energyLevels)) {
            energyLevels[row][col] = minOf(energyLevels[row][col] + 1, 10)
    }

    val newFlashes =  ArrayDeque<OctopusPos>()
    newFlashes.addAll(allPositions(energyLevels).filter {  energyLevels[it.first][it.second] == 10 })

    flashedThisStep.addAll(newFlashes)

    while (newFlashes.size > 0) {
        val flash = newFlashes.removeFirst()
        adjacency(flash.first, flash.second, energyLevels).forEach { pos -> run {
            val (row, col) = pos
            energyLevels[row][col] = energyLevels[row][col] + 1
            if (energyLevels[row][col] == 10) {
                newFlashes.addLast(pos)
                flashedThisStep.add(pos)
            }
        } }
    }

    allPositions(energyLevels)
        .filter { (row, col) -> energyLevels[row][col] > 9 }
        .forEach { (row, col) -> run { energyLevels[row][col] = 0 } }

    return StepEffects(flashedThisStep.size)
}

fun allPositions(energyLevels: Array<IntArray>): List<OctopusPos> =
    energyLevels.indices.flatMap { row ->
        (0 until energyLevels[0].size).map { col -> OctopusPos(row, col) }
    }

fun adjacency(i: Int, j: Int, inputArray: Array<IntArray>): List<OctopusPos> {
    val adjacency = mutableListOf<OctopusPos>()
    // North
    if (i > 0) {
        adjacency.add(i - 1 to j)
    }
    // South
    if (i < inputArray.size - 1) {
        adjacency.add(i + 1 to j)
    }
    // East
    if (j > 0) {
        adjacency.add(i to j - 1)
    }
    // West
    if (j < inputArray[0].size - 1) {
        adjacency.add(i to j + 1)
    }
    // NE
    if (i > 0 && j > 0) {
        adjacency.add(i - 1 to j - 1)
    }
    // NW
    if (i > 0 && j < inputArray[0].size - 1) {
        adjacency.add(i - 1 to j + 1)
    }
    // SE
    if (i < inputArray.size - 1 && j > 0) {
        adjacency.add(i + 1 to j - 1)
    }
    // SW
    if (i < inputArray.size - 1 && j < inputArray[0].size - 1) {
        adjacency.add(i + 1 to j + 1)
    }
    return adjacency
}

data class StepEffects(val flashesCount: Int)
typealias OctopusPos = Pair<Int, Int>