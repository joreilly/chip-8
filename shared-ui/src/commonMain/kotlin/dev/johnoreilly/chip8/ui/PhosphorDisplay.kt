package dev.johnoreilly.chip8.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.beust.chip8.Display
import dev.johnoreilly.chip8.ui.theme.Bezel
import dev.johnoreilly.chip8.ui.theme.BezelEdge
import dev.johnoreilly.chip8.ui.theme.Phosphor

private const val DisplayAspectRatio = Display.WIDTH.toFloat() / Display.HEIGHT.toFloat()

/**
 * The 64x32 Chip-8 framebuffer, drawn as a monochrome CRT panel: lit pixels get a soft
 * halo over a near-black screen, with scanlines across the whole thing.
 *
 * Drawing on an explicitly dark screen matters beyond looks — the old renderer painted
 * black pixels straight onto the window background, so the picture vanished in dark mode.
 */
@Composable
fun PhosphorDisplay(
    screenData: List<Boolean>,
    phosphor: Phosphor,
    modifier: Modifier = Modifier,
    dimmed: Boolean = false,
) {
    val brightness by animateFloatAsState(if (dimmed) 0.3f else 1f, label = "brightness")

    Box(
        modifier = modifier
            .aspectRatio(DisplayAspectRatio)
            .clip(RoundedCornerShape(14.dp))
            .background(Bezel)
            .border(1.dp, BezelEdge, RoundedCornerShape(14.dp))
            .padding(10.dp),
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(4.dp))
                .background(phosphor.screen),
        ) {
            val pixel = size.width / Display.WIDTH
            drawPixels(screenData, phosphor, pixel, brightness)
            drawScanlines(pixel)
        }
    }
}

private fun DrawScope.drawPixels(
    screenData: List<Boolean>,
    phosphor: Phosphor,
    pixel: Float,
    brightness: Float,
) {
    if (screenData.size < Display.WIDTH * Display.HEIGHT) return

    // The halo is drawn first, inflated around the pixel, so neighbouring lit pixels
    // bleed into each other the way a real phosphor does.
    val halo = pixel * 0.55f
    val haloColor = phosphor.glow.copy(alpha = 0.25f * brightness)
    val litColor = phosphor.lit.copy(alpha = brightness)

    repeat(Display.HEIGHT) { y ->
        val rowOffset = Display.WIDTH * y
        repeat(Display.WIDTH) { x ->
            if (screenData[rowOffset + x]) {
                val left = x * pixel
                val top = y * pixel
                drawRect(
                    color = haloColor,
                    topLeft = Offset(left - halo, top - halo),
                    size = Size(pixel + halo * 2, pixel + halo * 2),
                )
                drawRect(
                    color = litColor,
                    topLeft = Offset(left, top),
                    size = Size(pixel, pixel),
                )
            }
        }
    }
}

/** One dark line per Chip-8 row, which reads as CRT scanlines at any display size. */
private fun DrawScope.drawScanlines(pixel: Float) {
    if (pixel < 3f) return
    val thickness = pixel * 0.2f
    val color = Color.Black.copy(alpha = 0.22f)
    repeat(Display.HEIGHT) { y ->
        drawRect(
            color = color,
            topLeft = Offset(0f, (y + 1) * pixel - thickness),
            size = Size(size.width, thickness),
        )
    }
}
