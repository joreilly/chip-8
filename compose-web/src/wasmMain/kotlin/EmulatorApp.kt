import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.beust.chip8.Display
import dev.johnoreilly.chip8.Emulator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
//import theme.SlackColors
//import theme.divider


@Composable
fun EmulatorApp() {
    val emulator = remember { Emulator() }
    var selectedGame by remember { mutableStateOf("Space Invaders [David Winter]") }

    LaunchedEffect(selectedGame) {
        val romData = getRomData(selectedGame)
        emulator.loadRom(romData)
    }

    Row(
        modifier = Modifier.background(color = MaterialTheme.colors.surface).fillMaxSize()
    ) {
        GameWindow(emulator, selectedGame)
    }
}

@OptIn(ExperimentalResourceApi::class)
private suspend fun getRomData(gameName: String): ByteArray {
    return resource("chip-8/Space Invaders [David Winter].ch8").readBytes()
}

@Composable
fun GameListSidebar(gameNames: List<String>, selectedGame: String, onGameSelected: (game: String) -> Unit) {


    LazyColumn(
        modifier = Modifier
            .width(350.dp)
            .background(
                color = MaterialTheme.colors.surface,
                shape = RectangleShape
            )
            .border(
                border = BorderStroke(1.dp, color = Color(0xff35383D)),
                shape = RectangleShape
            )
            .fillMaxHeight()
            .fillMaxWidth()
    ) {

        items(gameNames) { game ->
            println(game)
            val backgroundColor = if (selectedGame == game) Color(0xff5B7AA2) else Color.Transparent
            val color = if (selectedGame == game) Color.White else Color.Black
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        color = backgroundColor
                    )
                    .clickable() {
                        onGameSelected.invoke(game)
                    }.padding(horizontal = 15.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = game,
                    color = color,
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }

}

@Composable
fun GameWindow(emulator: Emulator, gameName: String) {
    val focusRequester = remember(::FocusRequester)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier
        .onKeyEvent {
            if (it.type == KeyEventType.KeyDown) {
                emulator.keyPressed(it.key.keyCode.toInt() - 48)
            } else if (it.type == KeyEventType.KeyUp) {
                emulator.keyReleased()
            }
            true
        }
        .focusRequester(focusRequester)
        .focusTarget()
        .clickable() { focusRequester.requestFocus() }
        .padding(16.dp))
    {
        EmulatorView(emulator, gameName)
    }
}

@Composable
fun EmulatorView(emulator: Emulator, gameName: String) {
    val screenData = produceState<IntArray?>(null, gameName) {
        emulator.observeScreenUpdates {
            value = it
        }
    }

    screenData.value?.let { screenData ->
        BoxWithConstraints {
            val blockWidth = constraints.maxWidth / Display.WIDTH
            val blockHeight = blockWidth

            Canvas(modifier = Modifier.fillMaxSize().background(Color.White)) {

                repeat(Display.WIDTH) { x ->
                    repeat(Display.HEIGHT) { y ->
                        val index = x + Display.WIDTH * y
                        if (screenData[index] == 1) {
                            val xx = blockWidth * x.toFloat()
                            val yy = blockHeight * y.toFloat()

                            drawRect(
                                Color.Black,
                                topLeft = Offset(xx, yy),
                                size = Size(blockHeight.toFloat(), blockHeight.toFloat())
                            )
                        }
                    }
                }
            }
        }
    }

}


