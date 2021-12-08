import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Paths

fun getObservedLines(fullContent: String) = fullContent.split("\n")

fun parseLine(line: String): Pair<List<String>, List<String>> {
    val lineParts = line.split(" | ")
    val left = lineParts[0].split(" ")
    val right = lineParts[1].split(" ")

    return left to right
}

val len_digits_unique_number_segments = setOf(2, 3, 4, 7)

fun countUniqueNumberInOutput(output: List<String>) =
    output.filter { len_digits_unique_number_segments.contains(it.length) }.count()

fun countUniqueNumberInLine(line: String) =
    countUniqueNumberInOutput(parseLine(line).second)

fun countUniqueNumber(lines: List<String>): Int =
    lines.sumOf { countUniqueNumberInLine(it) }

fun day8part1() {
    val day8Input = Paths.get("src", "test", "kotlin", "day8.txt").toFile().readLines()
    println(countUniqueNumber(day8Input))
}

// Part2
fun part2Line(trainData: List<String>, evalData: List<String>): Int {
    val one = trainData.first { it.length == 2 }.toSortedSet()
    val four = trainData.first { it.length == 4 }.toSortedSet()
    val seven = trainData.first { it.length == 3 }.toSortedSet()
    val eight = trainData.first { it.length == 7 }.toSortedSet()
    val three = trainData.filter { it.length == 5 }.first { it.toSortedSet().containsAll(one) }.toSortedSet()
    val six = trainData.filter { it.length == 6 }.first { !it.toSortedSet().containsAll(one) }.toSortedSet()
    val nine = trainData.filter { it.length == 6 }.first { it.toSortedSet().containsAll(three) }.toSortedSet()
    val zero = trainData.filter { it.length == 6 }.filter { !it.toSortedSet().containsAll(three) }
        .first { it.toSortedSet().containsAll(one) }
        .toSortedSet()
    val five = trainData.filter { it.length == 5 }.first { six.containsAll(it.toSortedSet()) }.toSortedSet()
    val two = trainData.filter { it.length == 5 }.first { it.toSortedSet() != five && it.toSortedSet() != three }
        .toSortedSet()

    val theMap = mapOf(
        one to 1,
        two to 2,
        three to 3,
        four to 4,
        five to 5,
        six to 6,
        seven to 7,
        eight to 8,
        nine to 9,
        zero to 0
    )

    assertThat(theMap.values.size).isEqualTo(10)

    println("testing $trainData")
    return evalData.map { theMap[it.toSortedSet()].toString() }.joinToString("").toInt()
}

fun day8part2() {
    val day8Input = Paths.get("src", "test", "kotlin", "day8.txt").toFile().readLines()
    println(day8Input.sumOf { line -> parseLine(line).let { part2Line(it.first, it.second) } })
}

class Day8 {
    val puzzleTestInput = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"""

    @Test
    fun testGetObservedLines() {
        assertThat(getObservedLines(puzzleTestInput)).hasSize(10)
    }

    @Test
    fun testParseLine() {
        assertThat(parseLine("asdf asas | sas d").first).isEqualTo(listOf("asdf", "asas"))
        assertThat(parseLine("asdf asas | sas d").second).isEqualTo(listOf("sas", "d"))
    }

    @Test
    fun testCountUniqueNumberInLine() {
        assertThat(
            countUniqueNumberInLine("be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe")
        )
            .isEqualTo(2)
    }

    @Test
    fun testCountUniqueNumber() {
        assertThat(countUniqueNumber(getObservedLines(puzzleTestInput))).isEqualTo(26)
    }

    @Test
    fun testPart2Line() {
        assertThat(
            parseLine("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")
                .let { part2Line(it.first, it.second) }
        ).isEqualTo(5353)

        assertThat(
            parseLine("edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc")
                .let { part2Line(it.first, it.second) }
        ).isEqualTo(9781)

        assertThat(
            parseLine("bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef")
                .let { part2Line(it.first, it.second) }
        ).isEqualTo(1625)
    }

    @Test
    fun testPart2() {
        day8part2();
    }
}