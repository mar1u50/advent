import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Paths

fun to2DArray(input: String): Array<IntArray> =
    input.lines().map { line -> line.toCharArray().map { c -> c.digitToInt() }.toIntArray() }.toTypedArray()


fun isMinimum(inputArray: Array<IntArray>, i: Int, j: Int): Boolean {
    val northValue = if (i > 0) inputArray[i - 1][j] else Int.MAX_VALUE
    val southValue = if (i < inputArray.size - 1) inputArray[i + 1][j] else Int.MAX_VALUE
    val eastValue = if (j > 0) inputArray[i][j - 1] else Int.MAX_VALUE
    val westValue = if (j < inputArray[0].size - 1) inputArray[i][j + 1] else Int.MAX_VALUE
    val cellValue = inputArray[i][j]
    return cellValue < listOf(northValue, southValue, eastValue, westValue).minOrNull()!!
}

fun sumOfRisks(inputArray: Array<IntArray>): Int {
    var sum = 0;
    inputArray.forEachIndexed { i: Int, line: IntArray ->
        line.forEachIndexed { j, v ->
            run {
                sum += if (isMinimum(inputArray, i, j)) v + 1 else 0
            }
        }
    }
    return sum
}

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
    fun testPart1() {
        val input = to2DArray(Paths.get("src", "test", "kotlin", "day9.txt").toFile().readText())
        println("Part1: ${sumOfRisks(input)}")
    }
}