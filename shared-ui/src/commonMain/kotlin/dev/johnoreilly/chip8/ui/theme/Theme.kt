package dev.johnoreilly.chip8.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * The colour of the emulator's "phosphor". Real Chip-8 machines were monochrome, so
 * the display is drawn in a single tint over a near-black screen.
 */
enum class Phosphor(val label: String, val lit: Color, val glow: Color, val screen: Color) {
    Green("Green", Color(0xFF8CFFC1), Color(0xFF1FE87A), Color(0xFF05100A)),
    Amber("Amber", Color(0xFFFFD79A), Color(0xFFFFA023), Color(0xFF140C02)),
    Ice("Ice", Color(0xFFE6F1FF), Color(0xFF6FA8FF), Color(0xFF05080F));

    fun next(): Phosphor = entries[(ordinal + 1) % entries.size]
}

internal val Bezel = Color(0xFF101318)
internal val BezelEdge = Color(0xFF2A303A)

/** Dark, console-like theme shared by every client. */
@Composable
fun Chip8Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF6FE8A6),
            onPrimary = Color(0xFF00301A),
            primaryContainer = Color(0xFF1B4B33),
            onPrimaryContainer = Color(0xFFA6FFCD),
            secondary = Color(0xFF9FCAFF),
            onSecondary = Color(0xFF00325A),
            background = Color(0xFF0A0C10),
            onBackground = Color(0xFFE3E6EC),
            surface = Color(0xFF0F1218),
            onSurface = Color(0xFFE3E6EC),
            surfaceVariant = Color(0xFF1A1F27),
            onSurfaceVariant = Color(0xFFA9B2C0),
            outline = Color(0xFF39414D),
            outlineVariant = Color(0xFF262C35),
            error = Color(0xFFFFB4AB),
        ),
        content = content,
    )
}
