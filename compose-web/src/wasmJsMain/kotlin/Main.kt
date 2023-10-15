import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("Chip8", canvasElementId = "Chip8Canvas") {
        EmulatorApp()
    }
}

