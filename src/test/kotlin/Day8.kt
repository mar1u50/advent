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
}