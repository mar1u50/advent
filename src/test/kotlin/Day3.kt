import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Paths

// https://adventofcode.com/2021/day/3

val day3Input = Paths.get("src", "test", "kotlin", "day3.txt").toFile().readLines()

fun oneCounters(input: List<String>): List<Int> {
    val noDigits = input.first().length
    val oneCounters = ArrayList<Int>()
    for (i in 1..noDigits) {
        oneCounters.add(0)
    }

    input.forEach { diagString ->
        run {
            diagString.forEachIndexed { index, char ->
                run {
                    oneCounters[index] += if (char == '1') 1 else 0
                }
            }
        }
    }
    return oneCounters
}

fun computeStats(oneCounters: List<Int>, inputSize: Int): Pair<Int, Int> {
    val gamma =
        oneCounters.map { oneCount -> if ((oneCount * 2) > inputSize) '1' else '0' }.toCharArray().concatToString()
            .toInt(2)
    val epsilon =
        oneCounters.map { oneCount -> if ((oneCount * 2) < inputSize) '1' else '0' }.toCharArray().concatToString()
            .toInt(2)
    return gamma to epsilon
}

fun oxigenGeneratorRating(input: List<String>, index: Int): Int {
    if (input.size == 1) return input.first().toCharArray().concatToString().toInt(2)

    val (listWithOnes, listWithZeros) = input.partition { it[index] == '1' }
    return if (listWithOnes.size >= listWithZeros.size) {
        oxigenGeneratorRating(listWithOnes, index + 1)
    } else {
        oxigenGeneratorRating(listWithZeros, index + 1)
    }
}

fun CO2ScrubberRating(input: List<String>, index: Int): Int {
    if (input.size == 1) return input.first().toCharArray().concatToString().toInt(2)

    val (listWithOnes, listWithZeros) = input.partition { it[index] == '1' }
    return if (listWithOnes.size < listWithZeros.size) {
        CO2ScrubberRating(listWithOnes, index + 1)
    } else {
        CO2ScrubberRating(listWithZeros, index + 1)
    }
}

fun computeLifeSupport(input: List<String>): Int =
    oxigenGeneratorRating(input, 0) * CO2ScrubberRating(input, 0)

class Day3 {
    @Test
    fun testOneCounters() {
        assertThat(oneCounters(listOf("100", "110", "110"))).isEqualTo(listOf(3, 2, 0))
    }

    @Test
    fun testComputeStats() {
        assertThat(computeStats(listOf(3, 2, 0), 3)).isEqualTo(6 to 1)
    }

    @Test
    fun testDay3Part1() {
        val (gamma, epsilon) = computeStats(oneCounters(day3Input), day3Input.size)
        assertThat(gamma).isEqualTo(3259)
        assertThat(epsilon).isEqualTo(836)
        assertThat(epsilon * gamma).isEqualTo(2724524)
    }

    @Test
    fun testOxigenGeneratorRating() {
        assertThat(oxigenGeneratorRating(day3Input, 0)).isEqualTo(4023)
    }
    @Test
    fun testCO2ScrubberRating() {
        assertThat(CO2ScrubberRating(day3Input, 0)).isEqualTo(690)
    }

    @Test
    fun testDay3Part2() {
        assertThat(computeLifeSupport(day3Input)).isEqualTo(2775870)
    }
}