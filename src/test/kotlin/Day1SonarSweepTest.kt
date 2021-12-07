import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.nio.file.Paths

val puzzleLines = Paths.get("src", "test", "kotlin", "day1.txt").toFile().readLines()
fun List<String>.toInts() = map { it.toInt() }
fun List<Int>.countGrowing(): Int = windowed(2).count { it[0] < it[1]}

class Day1SonarSweepTest {
    @Test
    fun testCountGrowing() {
        assertThat(listOf(1,2).countGrowing()).isEqualTo(1)
        assertThat(listOf(1,2,4).countGrowing()).isEqualTo(2)
        assertThat(listOf(3,2).countGrowing()).isEqualTo(0)
        assertThat(listOf(3,3).countGrowing()).isEqualTo(0)
    }

    @Test
    fun testSonarSweep() {
        assertThat(puzzleLines.toInts().countGrowing()).isEqualTo(7)
    }
}

