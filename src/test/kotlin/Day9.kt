import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import java.util.LinkedList

fun to2DArray(input: String): Array<IntArray> =
    input.lines().map { line -> line.toCharArray().map { c -> c.digitToInt() }.toIntArray() }.toTypedArray()


fun isMinimum(inputArray: Array<IntArray>, i: Int, j: Int): Boolean {
    return inputArray[i][j] < neighborhood(inputArray, i, j).minOf { inputArray[it.first][it.second] }
}

fun sumOfRisks(inputArray: Array<IntArray>): Int =
    minLocations(inputArray)
        .sumOf { (i, j) -> inputArray[i][j] + 1 }


fun minLocations(inputArray: Array<IntArray>): Sequence<Pair<Int, Int>> {
    return sequence {
        inputArray.forEachIndexed { i: Int, line: IntArray ->
            line.forEachIndexed { j, v ->
                run {
                    if (isMinimum(inputArray, i, j)) {
                        yield(i to j)
                    }
                }
            }
        }
    }
}

fun neighborhood(inputArray: Array<IntArray>, i: Int, j: Int): Set<Pair<Int, Int>> {
    val neighborhood = mutableSetOf<Pair<Int, Int>>()
    // North
    if (i > 0) {
        neighborhood.add(i - 1 to j)
    }
    // South
    if (i < inputArray.size - 1) {
        neighborhood.add(i + 1 to j)
    }
    // East
    if (j > 0) {
        neighborhood.add(i to j - 1)
    }
    // West
    if (j < inputArray[0].size - 1) {
        neighborhood.add(i to j + 1)
    }
    return neighborhood
}

fun determineBasin(inputArray: Array<IntArray>, i: Int, j: Int): Set<Pair<Int, Int>> {
    val toExplore = LinkedList<Pair<Int, Int>>()
    val visited = mutableSetOf<Pair<Int, Int>>()
    val result = mutableSetOf<Pair<Int, Int>>()

    toExplore.push(i to j)

    fun valueAt(i: Int, j: Int) = inputArray[i][j]

    while (toExplore.isNotEmpty()) {
        val currentLocation = toExplore.remove()
        val valueCurrentLocation = valueAt(currentLocation.first, currentLocation.second)
        if (currentLocation !in visited) {
            result.add(currentLocation)
            toExplore.addAll(
                neighborhood(inputArray, currentLocation.first, currentLocation.second)
                    .filter { it !in visited }
                    // only take neighbors with value bigger than current value
                    .filter { (ni, nj) -> valueAt(ni, nj) in ((valueCurrentLocation + 1)..8) })

        }
    }
    return result
}


fun part2(inputArray: Array<IntArray>) =
    minLocations(inputArray)
        .map { (i, j) -> determineBasin(inputArray, i, j) }
        .map { it.size }
        .sortedDescending()
        .take(3)
        .fold(1) { product, value -> product * value }

class Day9 {
    val testInput = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent()

    @Test
    fun testToIntArrray() {
        assertThat(
            to2DArray(
                """
            12
            34
        """.trimIndent()
            )
        ).isEqualTo(
            arrayOf(
                intArrayOf(1, 2),
                intArrayOf(3, 4)
            )
        )
    }

    @Test
    fun testIsMinimum() {
        val input = to2DArray(testInput)

        // Corners
        assertThat(isMinimum(input, 0, 0)).isFalse
        assertThat(isMinimum(input, 4, 9)).isFalse

        // Minimum
        assertThat(isMinimum(input, 0, 1)).isTrue
        assertThat(isMinimum(input, 0, 9)).isTrue
        assertThat(isMinimum(input, 2, 2)).isTrue
        assertThat(isMinimum(input, 4, 6)).isTrue
    }

    @Test
    fun testSumOfRisks() {
        val input = to2DArray(testInput)
        assertThat(sumOfRisks(input)).isEqualTo(15)
    }

    @Test
    fun printPart1() {
        val input = to2DArray(Paths.get("src", "test", "kotlin", "day9.txt").toFile().readText())
        println("Part1: ${sumOfRisks(input)}")
    }

    @Test
    fun testPart2() {
        val input = to2DArray(testInput)
        assertThat(part2(input)).isEqualTo(1134)
    }

    @Test
    fun printPart2() {
        val input = to2DArray(Paths.get("src", "test", "kotlin", "day9.txt").toFile().readText())
        print("Part 2: ${part2(input)}")
    }
}