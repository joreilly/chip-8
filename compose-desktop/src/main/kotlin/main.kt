import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.johnoreilly.chip8.Emulator
import dev.johnoreilly.chip8.ui.Chip8App
import dev.johnoreilly.chip8.ui.InputMode

fun main() = application {
    val windowState = rememberWindowState(width = 1100.dp, height = 780.dp)

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Chip-8 Emulator",
    ) {
        val emulator = remember { Emulator() }
        Chip8App(emulator = emulator, inputMode = InputMode.Keyboard)
    }
}
