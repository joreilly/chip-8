import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.johnoreilly.chip8.Emulator
import dev.johnoreilly.chip8.ui.Chip8App
import dev.johnoreilly.chip8.ui.InputMode

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        val emulator = remember { Emulator() }
        Chip8App(emulator = emulator, inputMode = InputMode.Keyboard)
    }
}
