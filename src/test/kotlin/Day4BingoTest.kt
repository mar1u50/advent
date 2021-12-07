import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.nio.file.Paths
import java.util.LinkedList

class BingoCard(cardContent: List<String>) {
    val lines = cardContent.map { parseLine(it) }
    val marked = HashSet<Int>()
    var lastDrawn = -1
    var lastScore = 0
    var newWin = false

    fun mark(number: Int) {
        lastDrawn = number
        marked.add(number)
        val newScore = if (isBingo()) lastDrawn * sumUnmarked() else 0
        newWin = lastScore == 0 && newScore != 0
        lastScore = newScore
    }

    fun getColumn(col: Int) = IntArray(lines.size) { lines[it][col] }

    fun isBingo(): Boolean {
        val numberOfColums = lines[0].size
        val numberOfLines = lines.size

        return lines.map { line -> line.count { marked.contains(it) } }
            .count { it == numberOfColums } > 0 ||
                IntRange(0, numberOfColums - 1)
                    .map { ndx -> getColumn(ndx).count { marked.contains(it) } }
                    .count { it == numberOfLines } > 0
    }

    fun sumUnmarked() = lines.flatten().sumOf { if (!marked.contains(it)) it else 0 }

    fun getScore() = lastScore

    companion object {
        fun parseLine(line: String): List<Int> {
            return line.split(" ").filter { it.isNotEmpty() }.toInts()
        }
    }
}

enum class Style {
    PART1,
    PART2
}

class BingoGame(
    val cards: List<BingoCard>,
    randomNumbers: List<Int>,
    val style: Style = Style.PART1
) {
    val randomNumbersQueue = LinkedList(randomNumbers)

    var finalScore = 0

    fun drawNumber(): Int = randomNumbersQueue.remove()

    fun play() {
        while (finalScore == 0 && randomNumbersQueue.isNotEmpty()) {
            val newDrawnNumber = drawNumber()
            cards.forEach { bingoCard -> bingoCard.mark(newDrawnNumber) }

            finalScore = when (style) {
                Style.PART1 -> cards.maxOf { bingoCard -> bingoCard.getScore() }
                else -> if (cards.none { !it.isBingo() }) cards.find { it.newWin }?.getScore() ?: 0 else 0
            }
        }
    }
}

class Day4BingoTest {
    val TEST_RANDMON_NUMBERS = listOf(
        7,
        4,
        9,
        5,
        11,
        17,
        23,
        2,
        0,
        14,
        21,
        24,
        10,
        16,
        13,
        6,
        15,
        25,
        12,
        22,
        18,
        20,
        8,
        19,
        3,
        26,
        1
    )
    val TEST_CARDS = listOf(
        BingoCard(
            listOf(
                "22 13 17 11  0",
                " 8  2 23  4 24",
                "21  9 14 16  7",
                " 6 10  3 18  5",
                " 1 12 20 15 19"
            )
        ),
        BingoCard(
            listOf(
                " 3 15  0  2 22",
                " 9 18 13 17  5",
                "19  8  7 25 23",
                "20 11 10 24  4",
                "14 21 16 12  6"
            )
        ),
        BingoCard(
            listOf(
                "14 21 17 24  4",
                "10 16 15  9 19",
                "18  8 23 26 20",
                "22 11 13  6  5",
                " 2  0 12  3  7"
            )
        )
    )

    @Test
    fun testBingoCardHorizontal() {
        val card = BingoCard(
            listOf(
                "1  2 0",
                " 3  4 5"
            )
        )

        assertThat(card.sumUnmarked()).isEqualTo(15)

        card.mark(128)
        card.mark(1)
        assertThat(card.getScore()).isEqualTo(0)
        card.mark(2)
        assertThat(card.getScore()).isEqualTo(0)
        card.mark(0)
        card.mark(5)
        assertThat(card.getScore()).isEqualTo(7 * 5)
    }

    @Test
    fun testBingoCardVertical() {
        val card = BingoCard(
            listOf(
                "1  2 0",
                " 3  4 5"
            )
        )

        card.mark(2)
        card.mark(4)
        card.mark(5)

        assertThat(card.getScore()).isEqualTo(4 * 5)
    }

    @Test
    fun testParseLine() {
        assertThat(BingoCard.parseLine(" 8  2 23  4 24")).isEqualTo(listOf(8, 2, 23, 4, 24))
    }

    @Test
    fun testGamePart1() {
        val game = BingoGame(
            randomNumbers = TEST_RANDMON_NUMBERS,
            cards = TEST_CARDS
        )
        game.play()

        assertThat(game.finalScore).isEqualTo(4512)
    }

    @Test
    fun testGamePart2() {
        val game = BingoGame(
            randomNumbers = TEST_RANDMON_NUMBERS,
            cards = TEST_CARDS,
            style = Style.PART2
        )
        game.play()

        assertThat(game.finalScore).isEqualTo(1924)
    }

    @Test
    fun testGameFullPart1() {
        val day4Input = Paths.get("src", "test", "kotlin", "day4.txt").toFile().readLines()
        val randomNumbers = day4Input[0].split(",").toInts()

        val game = BingoGame(
            day4Input.windowed(5).filter { it.count { s -> s.isBlank() } == 0 }.map { BingoCard(it) },
            randomNumbers,
            style = Style.PART1
        )
        game.play()
        assertThat(game.finalScore).isEqualTo(23177)
    }

    @Test
    fun testGameFullPart2() {
        val day4Input = Paths.get("src", "test", "kotlin", "day4.txt").toFile().readLines()
        val randomNumbers = day4Input[0].split(",").toInts()

        val game = BingoGame(
            day4Input.windowed(5).filter { it.count { s -> s.isBlank() } == 0 }.map { BingoCard(it) },
            randomNumbers,
            style = Style.PART2
        )
        game.play()
        assertThat(game.finalScore).isEqualTo(6804)
    }
}
