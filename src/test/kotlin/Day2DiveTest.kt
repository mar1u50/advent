import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Paths

// https://adventofcode.com/2021/day/2

val day2Input = Paths.get("src", "test", "kotlin", "day2.txt").toFile().readLines()

data class Position(val horizontalPosition: Int, val depth: Int) {
    fun multiplication() = horizontalPosition * depth
}

interface Command {
    fun apply(position: Position): Position
}

data class ForwardCommand(private val step: Int) : Command {
    override fun apply(position: Position) = Position(position.horizontalPosition + step, position.depth)
}

data class DownCommand(private val step: Int) : Command {
    override fun apply(position: Position) = Position(position.horizontalPosition, position.depth + step)
}

data class UpCommand(private val step: Int) : Command {
    override fun apply(position: Position) = Position(position.horizontalPosition, position.depth - step)
}

private fun navigate(commands: List<Command>): Position {
    var position = Position(0, 0)
    commands.forEach {
        position = it.apply(position)
    }
    return position
}

fun parseCommands(commandLines: List<String>): List<Command> = commandLines
    .map { it.split(" ") }
    .map { createCommand(it[0], it[1].toInt()) }

fun createCommand(command: String, step: Int) = when (command) {
    "forward" -> ForwardCommand(step)
    "down" -> DownCommand(step)
    "up" -> UpCommand(step)
    else -> throw IllegalArgumentException("Invalid command: '$command'")
}

class Day2DiveTest {
    @Test
    fun testParseCommands() {
        assertThat(parseCommands(listOf("forward 1", "down 2", "up 3"))).isEqualTo(
            listOf(
                ForwardCommand(1),
                DownCommand(2),
                UpCommand(3)
            )
        )
    }

    @Test
    fun testDay2Navigate() {
        val position = navigate(parseCommands(day2Input))
        assertThat(position.depth).isEqualTo(10)
        assertThat(position.horizontalPosition).isEqualTo(15)
        assertThat(position.multiplication()).isEqualTo(150)
    }
}

