package dev.johnoreilly.chip_8_kmm.androidApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import dev.johnoreilly.chip8.Emulator
import dev.johnoreilly.chip8.ui.Chip8App
import dev.johnoreilly.chip8.ui.InputMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val emulator = remember { Emulator() }
            Chip8App(emulator = emulator, inputMode = InputMode.Touch)
        }
    }
}
