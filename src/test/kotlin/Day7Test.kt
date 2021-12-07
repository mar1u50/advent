import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nield.kotlinstatistics.median
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.math.round

fun parsePositions(positionsString: String): List<Int> = positionsString.split(",").toInts()

fun computeOptimumFuel(positions: List<Int>) =
    round(positions.median()).toInt().let { median -> positions.sumOf { abs(it - median) } }

fun main() {
    part1()
}

private fun part1() {
    val day7Input = Paths.get("src", "test", "kotlin", "day7.txt").toFile().readLines()[0]
    print(computeOptimumFuel(parsePositions(day7Input)))
}

class Day7Test {
    val CRAB_HORRIZ_POSITIONS = "16,1,2,0,4,2,7,1,2,14"

    @Test
    fun testParsePositions() {
        assertThat(parsePositions(CRAB_HORRIZ_POSITIONS)).isEqualTo(listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14))
    }

    @Test
    fun testComputeOptimumFuel() {
        assertThat(computeOptimumFuel(listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14))).isEqualTo(37)
    }
}
