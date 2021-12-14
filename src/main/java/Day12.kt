import java.nio.file.Paths

typealias Edge = Pair<String, String>

fun parseEdge(line: String): Edge {
    val list = line.split("-")
    return Edge(list[0], list[1])
}

fun toGraph(lines: List<String>): Map<String, List<String>> {
    val graph = mutableMapOf<String, MutableList<String>>()

    lines.map { parseEdge(it) }.forEach { edge ->
        run {
            val targetNodes = graph[edge.first] ?: mutableListOf()
            targetNodes.add(edge.second)
            graph[edge.first] = targetNodes

            if (edge.first != "start" && edge.second != "end") {
                val sourceNodes = graph[edge.second] ?: mutableListOf()
                sourceNodes.add(edge.first)
                graph[edge.second] = sourceNodes
            }
        }
    }
    return graph
}

class Path(node: String, pathElements: List<String> = listOf(), lowercaseElements: Set<String> = setOf()) {

    val lastElement: String = node
    private val pathElements: List<String>
    private val lowercaseElements: Set<String>

    fun add(target: String): Path {
        return Path(target, pathElements, lowercaseElements)
    }

    fun contains(element: String): Boolean {
        return lowercaseElements.contains(element)
    }

    init {
        this.pathElements = pathElements + node
        this.lowercaseElements = if (node == node.lowercase()) lowercaseElements + node else lowercaseElements
    }
}

fun visit(graph: Map<String, List<String>>, start: String): List<Path> {
    val foundPaths = mutableListOf<Path>()
    visitInternal(graph, Path(start), foundPaths)
    return foundPaths
}

fun visitInternal(graph: Map<String, List<String>>, path: Path, foundPaths: MutableList<Path>) {
    if (path.lastElement == "end") {
        foundPaths.add(path)
    }

    graph[path.lastElement]?.forEach { target -> run {
        if (target == target.lowercase()) {
            if (!path.contains(target)) {
                visitInternal(graph, path.add(target), foundPaths)
            }
        } else {
            visitInternal(graph, path.add(target), foundPaths)
        }
    } }
}

fun main() {
    val input = Paths.get("src", "main", "java", "day12.txt").toFile().readLines()
    val graph = toGraph(input)
    part1(graph)
}

fun part1(graph: Map<String, List<String>>) {
    print("Part1 : ${visit(graph, "start").count()}")
}
