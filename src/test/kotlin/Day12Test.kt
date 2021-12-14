import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day12Test {
    val smallTest = """
        |start-A
        |start-b
        |A-c
        |A-b
        |b-d
        |A-end
        |b-end
    """.trimMargin()

    @Test
    fun testParseEdge() {
        assertThat(parseEdge("start-A")).isEqualTo(Pair("start", "A"))
    }

    @Test
    fun testToGraph() {
        val lines = smallTest.split("\n")

        assertThat(lines).hasSize(7)
        val graph = toGraph(lines)

        assertThat(graph).hasSize(5)

        assertThat(graph["start"]).isEqualTo(listOf("A", "b"))
        assertThat(graph["A"]).isEqualTo(listOf("c", "b", "end"))
        assertThat(graph["c"]).isEqualTo(listOf("A"))
        assertThat(graph["b"]).isEqualTo(listOf("A", "d", "end"))
        assertThat(graph["d"]).isEqualTo(listOf("b"))
    }

    @Test
    fun testVisit() {
        val graph = toGraph(smallTest.split("\n"))

        assertThat(visit(graph, "start")).hasSize(10)
    }
}