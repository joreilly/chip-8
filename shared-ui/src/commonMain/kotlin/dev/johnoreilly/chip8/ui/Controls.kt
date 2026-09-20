package dev.johnoreilly.chip8.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * A key on the virtual Chip-8 keypad. Unlike a normal Button this reports press and
 * release separately — games need to know how long a key is held, and several keys
 * can be held at once.
 */
@Composable
fun KeypadButton(
    key: Int,
    isPressed: Boolean,
    onPress: (Int) -> Unit,
    onRelease: (Int) -> Unit,
    modifier: Modifier = Modifier,
    description: String = "Key ${key.toHexLabel()}",
    content: @Composable () -> Unit,
) {
    val background by animateColorAsState(
        if (isPressed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        label = "keyBackground",
    )
    val foreground = if (isPressed) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .sizeIn(minWidth = 56.dp, minHeight = 56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .pointerInput(key) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    onPress(key)
                    // Fires for a lift or a cancelled gesture, so a key can never stick down.
                    waitForUpOrCancellation()
                    onRelease(key)
                }
            }
            .semantics {
                this.role = Role.Button
                this.contentDescription = description
            },
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides foreground,
            content = content,
        )
    }
}

private fun ControlRole.icon(): ImageVector? = when (this) {
    ControlRole.Left -> Icons.AutoMirrored.Filled.ArrowBack
    ControlRole.Right -> Icons.AutoMirrored.Filled.ArrowForward
    ControlRole.Up -> Icons.Filled.ArrowUpward
    ControlRole.Down -> Icons.Filled.ArrowDownward
    ControlRole.Action, ControlRole.Other -> null
}

/** The controls that actually matter for the selected game, in a natural left-to-right order. */
@Composable
fun GameControls(
    game: Game,
    pressedKeys: Set<Int>,
    onPress: (Int) -> Unit,
    onRelease: (Int) -> Unit,
    showKeyHints: Boolean,
    modifier: Modifier = Modifier,
) {
    if (game.controls.isEmpty()) {
        Text(
            text = "${game.title} is a demo — nothing to control, just watch.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.padding(vertical = 12.dp),
        )
        return
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        game.controls.sortedBy { it.role.ordinal }.forEach { control ->
            KeypadButton(
                key = control.key,
                isPressed = control.key in pressedKeys,
                onPress = onPress,
                onRelease = onRelease,
                description = control.label,
                modifier = Modifier.widthIn(min = 72.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val icon = control.role.icon()
                    if (icon != null) {
                        Icon(icon, contentDescription = null, modifier = Modifier.size(22.dp))
                    } else {
                        Text(control.label, style = MaterialTheme.typography.labelLarge)
                    }
                    if (showKeyHints) {
                        Text(
                            text = control.key.toHexLabel(),
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                }
            }
        }
    }
}

/** The full 4x4 hex keypad, as laid out on the original COSMAC VIP. */
@Composable
fun HexKeypad(
    pressedKeys: Set<Int>,
    onPress: (Int) -> Unit,
    onRelease: (Int) -> Unit,
    highlighted: Set<Int>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.widthIn(max = 300.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HexKeypadRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { key ->
                    KeypadButton(
                        key = key,
                        isPressed = key in pressedKeys,
                        onPress = onPress,
                        onRelease = onRelease,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = key.toHexLabel(),
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = FontFamily.Monospace,
                            // Keys this game actually uses stand out from the rest.
                            fontWeight = if (key in highlighted) FontWeight.Bold else FontWeight.Normal,
                            color = if (key in highlighted && key !in pressedKeys) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                LocalContentColor.current
                            },
                        )
                    }
                }
            }
        }
    }
}
