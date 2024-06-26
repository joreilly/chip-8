package com.beust.chip8

import kotlinx.coroutines.delay

class Keyboard {
    var key: Int? = null

    suspend fun waitForKeyPress(): Int {
        while (key == null) {
            delay(100)
        }
        return key!!
    }
}

