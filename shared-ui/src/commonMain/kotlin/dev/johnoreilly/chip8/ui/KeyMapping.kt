package dev.johnoreilly.chip8.ui

import androidx.compose.ui.input.key.Key

/**
 * Maps a physical key to a Chip-8 hex key.
 *
 * Two mappings are layered. The hex keys 0-9 and A-F map straight through, which keeps
 * the keys the roms were written for (4/5/6 for Space Invaders, for example) working.
 * On top of that, the arrow keys and space are resolved against the selected game's
 * control scheme, so they do the obvious thing in whatever is loaded.
 */
fun Key.toChip8Key(game: Game?): Int? {
    directHexKey()?.let { return it }

    val role = when (this) {
        Key.DirectionLeft -> ControlRole.Left
        Key.DirectionRight -> ControlRole.Right
        Key.DirectionUp -> ControlRole.Up
        Key.DirectionDown -> ControlRole.Down
        Key.Spacebar, Key.Enter -> ControlRole.Action
        else -> return null
    }
    // Up doubles as the action key in games that fire upwards but have no separate button.
    return game?.keyFor(role) ?: if (role == ControlRole.Up) game?.keyFor(ControlRole.Action) else null
}

private fun Key.directHexKey(): Int? = when (this) {
    Key.Zero, Key.NumPad0 -> 0x0
    Key.One, Key.NumPad1 -> 0x1
    Key.Two, Key.NumPad2 -> 0x2
    Key.Three, Key.NumPad3 -> 0x3
    Key.Four, Key.NumPad4 -> 0x4
    Key.Five, Key.NumPad5 -> 0x5
    Key.Six, Key.NumPad6 -> 0x6
    Key.Seven, Key.NumPad7 -> 0x7
    Key.Eight, Key.NumPad8 -> 0x8
    Key.Nine, Key.NumPad9 -> 0x9
    Key.A -> 0xA
    Key.B -> 0xB
    Key.C -> 0xC
    Key.D -> 0xD
    Key.E -> 0xE
    Key.F -> 0xF
    else -> null
}

/** Human readable summary of how to drive [game] from a physical keyboard. */
fun keyboardHint(game: Game): String {
    if (game.controls.isEmpty()) return "No controls — this rom just plays."
    // Spelled out rather than drawn with arrow glyphs, which the browser's default
    // font does not always have.
    val arrows = buildList {
        if (game.keyFor(ControlRole.Left) != null) add("left")
        if (game.keyFor(ControlRole.Right) != null) add("right")
        if (game.keyFor(ControlRole.Up) != null) add("up")
        if (game.keyFor(ControlRole.Down) != null) add("down")
    }
    val action = game.controls.firstOrNull { it.role == ControlRole.Action }
    val hexKeys = game.controls.joinToString(" ") { it.key.toHexLabel() }

    return buildString {
        if (arrows.isNotEmpty()) append("Arrow keys: ${arrows.joinToString("/")}")
        if (action != null) {
            if (isNotEmpty()) append("   |   ")
            append("Space = ${action.label}")
        }
        if (isNotEmpty()) append("   |   ")
        append("or keys $hexKeys")
    }
}
