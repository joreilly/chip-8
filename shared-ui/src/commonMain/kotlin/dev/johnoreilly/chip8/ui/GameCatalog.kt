package dev.johnoreilly.chip8.ui

import dev.johnoreilly.chip8.Emulator

/**
 * What a key does in a particular game. Chip-8 roms have no metadata, so the mapping
 * of hex key to action is curated here — it's what lets the UI show a left arrow
 * rather than an anonymous "4".
 */
enum class ControlRole { Left, Right, Up, Down, Action, Other }

/** A single hex key (0x0..0xF) bound to a role within a game. */
data class GameControl(
    val key: Int,
    val role: ControlRole,
    val label: String,
)

data class Game(
    val id: String,
    val title: String,
    val credit: String,
    val description: String,
    val controls: List<GameControl> = emptyList(),
    val speedHz: Int = Emulator.DEFAULT_SPEED_HZ,
) {
    val romPath: String get() = "files/roms/$id.ch8"

    /** True for roms that just draw something — no input, nothing to lose. */
    val isDemo: Boolean get() = controls.isEmpty()

    fun keyFor(role: ControlRole): Int? = controls.firstOrNull { it.role == role }?.key
}

private fun left(key: Int) = GameControl(key, ControlRole.Left, "Left")
private fun right(key: Int) = GameControl(key, ControlRole.Right, "Right")
private fun up(key: Int) = GameControl(key, ControlRole.Up, "Up")
private fun down(key: Int) = GameControl(key, ControlRole.Down, "Down")
private fun action(key: Int, label: String) = GameControl(key, ControlRole.Action, label)
private fun other(key: Int, label: String) = GameControl(key, ControlRole.Other, label)

/** The bundled rom library, games first then the non-interactive demos. */
val GameCatalog: List<Game> = listOf(
    Game(
        id = "space_invaders",
        title = "Space Invaders",
        credit = "David Winter",
        description = "Shoot the descending aliens before they reach you.",
        controls = listOf(left(4), action(5, "Fire"), right(6)),
        speedHz = 900,
    ),
    Game(
        id = "brix",
        title = "Brix",
        credit = "Andreas Gustafsson, 1990",
        description = "Bounce the ball to clear every brick.",
        controls = listOf(left(4), right(6)),
        speedHz = 600,
    ),
    Game(
        id = "breakout",
        title = "Breakout",
        credit = "Carmelo Cortez, 1979",
        description = "The original brick-breaker.",
        controls = listOf(left(4), right(6)),
        speedHz = 600,
    ),
    Game(
        id = "tetris",
        title = "Tetris",
        credit = "Fran Dachille, 1991",
        description = "Stack the falling pieces into complete rows.",
        controls = listOf(left(4), action(5, "Rotate"), right(6), down(7)),
        speedHz = 500,
    ),
    Game(
        id = "blinky",
        title = "Blinky",
        credit = "Hans Christian Egeberg, 1991",
        description = "A Pac-Man style maze chase.",
        controls = listOf(up(3), down(6), left(7), right(8)),
        speedHz = 1200,
    ),
    Game(
        id = "pong",
        title = "Pong (2 player)",
        credit = "Paul Vervalin, 1990",
        description = "Two paddles, one ball. Left player uses 1 and 4.",
        controls = listOf(
            up(1), down(4),
            other(0xC, "P2 up"), other(0xD, "P2 down"),
        ),
        speedHz = 600,
    ),
    Game(
        id = "maze",
        title = "Maze",
        credit = "David Winter, 199x",
        description = "Draws a random maze. No input.",
    ),
    Game(
        id = "particle_demo",
        title = "Particle Demo",
        credit = "zeroZshadow, 2008",
        description = "A cascade of particles.",
        speedHz = 1000,
    ),
    Game(
        id = "stars",
        title = "Stars",
        credit = "Sergey Naydenov, 2010",
        description = "A scrolling starfield.",
        speedHz = 1000,
    ),
    Game(
        id = "trip8_demo",
        title = "Trip8 Demo",
        credit = "Revival Studios, 2008",
        description = "Rotating wireframe graphics demo.",
        speedHz = 1200,
    ),
    Game(
        id = "zero_demo",
        title = "Zero Demo",
        credit = "zeroZshadow, 2007",
        description = "A short graphics demo.",
    ),
    Game(
        id = "sierpinski",
        title = "Sierpinski",
        credit = "Sergey Naydenov, 2010",
        description = "Draws the Sierpinski triangle.",
        speedHz = 1000,
    ),
    Game(
        id = "chip8_picture",
        title = "Chip-8 Logo",
        credit = "Unknown",
        description = "Renders the Chip-8 logo.",
    ),
    Game(
        id = "clock",
        title = "Clock",
        credit = "Bill Fisher, 1981",
        description = "A running digital clock.",
    ),
)

/** The 4x4 hex keypad layout as it appeared on the original COSMAC VIP. */
val HexKeypadRows: List<List<Int>> = listOf(
    listOf(0x1, 0x2, 0x3, 0xC),
    listOf(0x4, 0x5, 0x6, 0xD),
    listOf(0x7, 0x8, 0x9, 0xE),
    listOf(0xA, 0x0, 0xB, 0xF),
)

fun Int.toHexLabel(): String = toString(16).uppercase()
