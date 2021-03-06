import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nield.kotlinstatistics.median
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.math.round

fun parsePositions(positionsString: String): List<Int> = positionsString.split(",").toInts()

fun computeOptimumFuelPart1(positions: List<Int>) =
    round(positions.median()).toInt().let { median -> positions.sumOf { abs(it - median) } }

private fun part1() {
    val day7Input = Paths.get("src", "test", "kotlin", "day7.txt").toFile().readLines()[0]
    println("Part 1 - ${computeOptimumFuelPart1(parsePositions(day7Input))}")
}

fun gaussSum(n: Int) = n * (n + 1) / 2

fun sumOfGaussSum(positions: List<Int>, refPos: Int) =
    positions.sumOf { gaussSum(abs(it - refPos)) }

fun computeOptimumFuelPart2(positions: List<Int>): Int =
    (positions.minOrNull()!!..positions.maxOrNull()!!).minOf { p -> sumOfGaussSum(positions, p) }

private fun part2() {
    val day7Input = Paths.get("src", "test", "kotlin", "day7.txt").toFile().readLines()[0]
    println("Part 2 - ${computeOptimumFuelPart2(parsePositions(day7Input))}")
}

fun main() {
    part1()
    part2()
}

class Day7Test {
    private val CRAB_HORRIZ_POSITIONS = "16,1,2,0,4,2,7,1,2,14"

    @Test
    fun testParsePositions() {
        assertThat(parsePositions(CRAB_HORRIZ_POSITIONS)).isEqualTo(listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14))
    }

    @Test
    fun testComputeOptimumFuel() {
        assertThat(computeOptimumFuelPart1(listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14))).isEqualTo(37)
    }

    @Test
    fun testGaussSum() {
        assertThat(gaussSum(0)).isEqualTo(0)
        assertThat(gaussSum(1)).isEqualTo(0 + 1)
        assertThat(gaussSum(2)).isEqualTo(0 + 1 + 2)
        assertThat(gaussSum(3)).isEqualTo(0 + 1 + 2 + 3)
    }

    @Test
    fun testSumOfGaussSum() {
        assertThat(sumOfGaussSum(listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14), 5)).isEqualTo(168)
    }

    @Test
    fun testComputeOptimumFuelPart2() {
        assertThat(computeOptimumFuelPart2(listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14))).isEqualTo(168)
    }
}
