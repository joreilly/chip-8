package dev.johnoreilly.chip_8_kmm.androidApp

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.beust.chip8.Display
import dev.johnoreilly.chip8.Emulator
import kotlinx.collections.immutable.ImmutableList


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val romFile = resources.assets.open("Space Invaders [David Winter].ch8")
        val romData = romFile.readBytes()
        setContent {
            MaterialTheme {
                MainLayout(romData)
            }
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

    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
        EmulatorView(emulator)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            GameButton(emulator, 4, Icons.AutoMirrored.Filled.ArrowBack)
            GameButton(emulator, 5, Icons.Filled.ArrowUpward)
            GameButton(emulator, 6, Icons.AutoMirrored.Filled.ArrowForward)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameButton(emulator: Emulator, number: Int, icon: ImageVector) {
    Button(modifier = Modifier.pointerInteropFilter {
        when (it.action) {
            MotionEvent.ACTION_DOWN -> {
                emulator.keyPressed(number)
            }
            MotionEvent.ACTION_UP -> {
                emulator.keyReleased()
            }
            else ->  false
        }
        true
    }, onClick = {}) {
        Icon(imageVector = icon, contentDescription = "$number")
    }
}

@Composable
fun EmulatorView(emulator: Emulator) {
    val screenData = produceState<ImmutableList<Boolean>?>(null, emulator) {
        emulator.observeScreenUpdates {
            value = it
        }
    }

    val displayWidth = Display.WIDTH
    val displayHeight = Display.HEIGHT

    screenData.value?.let { screenData ->
        Canvas(modifier = Modifier.fillMaxWidth()) {
            val blockSize = size.width / displayWidth

            repeat(displayWidth) { x ->
                repeat(displayHeight) { y ->
                    val index = x + displayWidth * y
                    if (screenData[index]) {
                        val xx = blockSize * x.toFloat()
                        val yy = blockSize * y.toFloat()

                        drawRect(Color.Black, topLeft = Offset(xx, yy),
                            size = Size(blockSize, blockSize))
                    }
                }
            }
        }
    }
}


