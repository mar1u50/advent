import java.nio.file.Paths

fun main() {
    val input = Paths.get("src", "main", "kotlin", "day10.txt").toFile().readLines()
    part1(input)
    part2(input)
}

fun part1(lines: List<String>) {
    val part1Score = lines
        .map { checkLine(it) }
        .filter { it.result == CheckLineResults.CURRUPTED }
        .map { it.score }
        .sum()

    println("Part1: $part1Score")
}

fun part2(lines: List<String>) {
    val scores = lines
        .map { checkLine(it) }
        .filter { it.result == CheckLineResults.INCOMPLETE }
        .map { it.score }
        .sorted()

    println("Part2: ${scores[scores.size / 2]}")
}

val parenthesis = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)

fun checkLine(line: String): CheckLineResult {
    val stack = ArrayDeque<Char>()

    for (c in line) {
        if (c in parenthesis.keys) {
            stack.addLast(c)
        } else {
            val lastParen = stack.removeLastOrNull()
            if (lastParen == null || parenthesis[lastParen] != c) {
                return buildCorruptedResult(c)
            }
        }
    }

    return if (stack.isEmpty()) {
        buildOkResult()
    } else {
        buildIncompleteResult(stack)
    }
}

private val incompleteScores = mapOf(
    ')' to 1L,
    ']' to 2L,
    '}' to 3L,
    '>' to 4L
)

fun buildIncompleteResult(stack: ArrayDeque<Char>): CheckLineResult {
    val toClose = stack.reversed().map { parenthesis[it] }
    var score = 0L

    toClose.forEach { c ->
        run {
            score = 5 * score + incompleteScores[c]!!
        }
    }
    return CheckLineResult(CheckLineResults.INCOMPLETE, score)
}

fun buildOkResult(): CheckLineResult {
    return CheckLineResult(CheckLineResults.OK, 0)
}

private val corruptionScores = mapOf(
    ')' to 3L,
    ']' to 57L,
    '}' to 1197L,
    '>' to 25137L
)


fun buildCorruptedResult(ch: Char): CheckLineResult {
    return CheckLineResult(CheckLineResults.CURRUPTED, corruptionScores[ch]!!)
}

enum class CheckLineResults {
    OK,
    CURRUPTED,
    INCOMPLETE
}

data class CheckLineResult(
    val result: CheckLineResults,
    val score: Long
)
