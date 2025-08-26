@file:OptIn(ExperimentalComposeUiApi::class)

package dev.johnoreilly.chip8.presentation

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.OutlinedButton
import com.beust.chip8.Display
import dev.johnoreilly.chip8.Emulator
import kotlinx.collections.immutable.ImmutableList


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val romFile = resources.assets.open("Space Invaders [David Winter].ch8")
        val romData = romFile.readBytes()

        setContent {
            MainLayout(romData)
        }
    }
}


@Composable
fun MainLayout(romData: ByteArray) {
    val emulator = remember {
        Emulator().also {
            it.loadRom(romData)
        }
    }

    Column(modifier = Modifier.fillMaxSize()
        .padding(top = 16.dp), verticalArrangement = Arrangement.SpaceBetween)
    {
        EmulatorView(emulator)

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.Center) {
            GameButton(emulator, 4, Icons.Filled.ArrowBack)
            GameButton(emulator, 5, Icons.Filled.ArrowUpward)
            GameButton(emulator, 6, Icons.Filled.ArrowForward)
        }
    }
}



@Composable
fun EmulatorView(emulator: Emulator) {
    val focusRequester = remember { FocusRequester() }

    val screenData = produceState<List<Boolean>?>(null, emulator) {
        emulator.observeScreenUpdates {
            value = it
        }
    }

    val displayWidth = Display.WIDTH
    val displayHeight = Display.HEIGHT

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val blockSize = size.width / displayWidth

        screenData.value?.let { screenData ->
            repeat(displayWidth) { x ->
                repeat(displayHeight) { y ->
                    val index = x + displayWidth * y
                    if (screenData[index]) {
                        val xx = blockSize * x.toFloat()
                        val yy = blockSize * y.toFloat()

                        drawRect(
                            Color.White, topLeft = Offset(xx, yy),
                            size = Size(blockSize, blockSize)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameButton(emulator: Emulator, number: Int, icon: ImageVector) {
    OutlinedButton(modifier = Modifier.padding(8.dp).size(32.dp).pointerInteropFilter {
        when (it.action) {
            MotionEvent.ACTION_DOWN ->emulator.keyPressed(number)
            MotionEvent.ACTION_UP -> emulator.keyReleased()
        }
        true
    }, onClick = {}) {
        Icon(imageVector = icon, contentDescription = "$number")
    }
}

