package com.beust.chip8

/**
 * The Chip-8 hex keypad. Tracks every key currently held down so that games which
 * need simultaneous presses (move while firing, for example) behave correctly.
 */
internal class Keyboard {
    private val pressed = LinkedHashSet<Int>()

    /** The most recently pressed key that is still held, or null if none are. */
    val key: Int?
        get() = pressed.lastOrNull()

    fun isPressed(key: Int): Boolean = key in pressed

    fun press(key: Int) {
        if (key in VALID_KEYS) {
            // Re-add so this becomes the most recent key.
            pressed.remove(key)
            pressed.add(key)
        }
    }

    fun release(key: Int) {
        pressed.remove(key)
    }

    fun releaseAll() {
        pressed.clear()
    }

    companion object {
        private val VALID_KEYS = 0..0xf
    }
}
