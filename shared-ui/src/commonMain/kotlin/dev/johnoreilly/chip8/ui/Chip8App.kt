package dev.johnoreilly.chip8.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tonality
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.johnoreilly.chip8.Emulator
import dev.johnoreilly.chip8.ui.theme.Chip8Theme

/** Whether this client is driven primarily by touch or by a physical keyboard. */
enum class InputMode { Touch, Keyboard }

private val TwoPaneBreakpoint = 900.dp

/**
 * The whole Chip-8 client: rom library, display, transport controls and keypad.
 * Shared by the Android, desktop and web apps.
 */
@Composable
fun Chip8App(
    emulator: Emulator,
    inputMode: InputMode,
    modifier: Modifier = Modifier,
) {
    Chip8Theme {
        BoxWithConstraints(modifier) {
            Chip8Screen(
                state = rememberChip8UiState(emulator),
                inputMode = inputMode,
                wideWindow = maxWidth >= TwoPaneBreakpoint,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun Chip8Screen(
    state: Chip8UiState,
    inputMode: InputMode,
    wideWindow: Boolean,
) {
    val screen by state.emulator.screen.collectAsState()
    val isRunning by state.emulator.isRunning.collectAsState()
    val focusRequester = remember { FocusRequester() }

    // Keep keyboard focus on the emulator, including after switching games.
    LaunchedEffect(state.selectedGame, inputMode) {
        if (inputMode == InputMode.Keyboard) {
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        modifier = Modifier
            .onKeyEvent { event ->
                val chip8Key = event.key.toChip8Key(state.selectedGame) ?: return@onKeyEvent false
                when (event.type) {
                    KeyEventType.KeyDown -> state.pressKey(chip8Key)
                    KeyEventType.KeyUp -> state.releaseKey(chip8Key)
                    else -> return@onKeyEvent false
                }
                true
            }
            .focusRequester(focusRequester)
            .focusTarget(),
        topBar = {
            Chip8TopBar(
                state = state,
                isRunning = isRunning,
                wideWindow = wideWindow,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { contentPadding ->
        Box(Modifier.fillMaxSize().padding(contentPadding)) {
            if (wideWindow) {
                Row(Modifier.fillMaxSize()) {
                    if (state.showLibrary) {
                        GameLibrary(
                            selectedGame = state.selectedGame,
                            onGameSelected = state::selectGame,
                            modifier = Modifier
                                .width(280.dp)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.surface),
                        )
                    }
                    EmulatorPane(
                        state = state,
                        screenData = screen.screenData,
                        isRunning = isRunning,
                        inputMode = inputMode,
                        modifier = Modifier.weight(1f),
                    )
                    if (state.showDisassembly) {
                        DisassemblyPane(
                            disassembly = state.disassembly,
                            modifier = Modifier.width(280.dp).fillMaxHeight(),
                        )
                    }
                }
            } else {
                Column(Modifier.fillMaxSize()) {
                    CompactGamePicker(
                        selectedGame = state.selectedGame,
                        onGameSelected = state::selectGame,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    EmulatorPane(
                        state = state,
                        screenData = screen.screenData,
                        isRunning = isRunning,
                        inputMode = inputMode,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Chip8TopBar(
    state: Chip8UiState,
    isRunning: Boolean,
    wideWindow: Boolean,
) {
    var speedMenuOpen by remember { mutableStateOf(false) }

    TopAppBar(
        navigationIcon = {
            if (wideWindow) {
                IconButton(onClick = state::toggleLibrary) {
                    Icon(
                        imageVector = Icons.Filled.MenuOpen,
                        contentDescription = if (state.showLibrary) "Hide game library" else "Show game library",
                        tint = activeTint(state.showLibrary),
                    )
                }
            }
        },
        title = {
            Column {
                Text(
                    text = "CHIP-8",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Monospace,
                )
                Text(
                    text = state.selectedGame.title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        actions = {
            if (wideWindow) {
                IconButton(onClick = state::toggleDisassembly) {
                    Icon(
                        imageVector = Icons.Filled.Code,
                        contentDescription = "Toggle disassembly",
                        tint = activeTint(state.showDisassembly),
                    )
                }
            }
            IconButton(onClick = state::toggleKeypad) {
                Icon(
                    imageVector = if (state.showKeypad) Icons.Filled.VideogameAsset else Icons.Filled.GridView,
                    contentDescription = if (state.showKeypad) {
                        "Show game controls"
                    } else {
                        "Show full hex keypad"
                    },
                    tint = activeTint(state.showKeypad),
                )
            }
            IconButton(onClick = state::cyclePhosphor) {
                Icon(
                    imageVector = Icons.Filled.Tonality,
                    contentDescription = "Screen colour: ${state.phosphor.label}",
                )
            }
            Box {
                IconButton(onClick = { speedMenuOpen = true }) {
                    Icon(Icons.Filled.Speed, contentDescription = "Emulation speed")
                }
                DropdownMenu(
                    expanded = speedMenuOpen,
                    onDismissRequest = { speedMenuOpen = false },
                ) {
                    SpeedPreset.entries.forEach { preset ->
                        val current = state.effectiveSpeedHz() == preset.hz
                        DropdownMenuItem(
                            text = { Text("${preset.label}  ·  ${preset.hz} Hz") },
                            onClick = {
                                state.selectSpeed(preset)
                                speedMenuOpen = false
                            },
                            trailingIcon = {
                                if (current) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayArrow,
                                        contentDescription = "Current speed",
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            },
                        )
                    }
                }
            }
            IconButton(onClick = state::reset) {
                Icon(Icons.Filled.Refresh, contentDescription = "Restart game")
            }
            IconButton(onClick = state::togglePause) {
                Icon(
                    imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Resume",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

@Composable
private fun activeTint(active: Boolean) = if (active) {
    MaterialTheme.colorScheme.primary
} else {
    MaterialTheme.colorScheme.onSurfaceVariant
}

@Composable
private fun EmulatorPane(
    state: Chip8UiState,
    screenData: List<Boolean>,
    isRunning: Boolean,
    inputMode: InputMode,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // The display takes whatever space the controls leave rather than a fixed
        // width, so the keypad can never be pushed off the bottom of the window.
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            // No fillMax here on purpose: given loose constraints, aspectRatio picks the
            // largest 2:1 box that fits the slot in both directions.
            PhosphorDisplay(
                screenData = screenData,
                phosphor = state.phosphor,
                dimmed = !isRunning || state.isLoadingRom,
            )
            if (!isRunning && !state.isLoadingRom) {
                Text(
                    text = "PAUSED",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily.Monospace,
                    color = state.phosphor.lit,
                )
            }
        }

        Text(
            text = if (inputMode == InputMode.Keyboard) {
                keyboardHint(state.selectedGame)
            } else {
                state.selectedGame.description
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        if (state.showKeypad) {
            HexKeypad(
                pressedKeys = state.pressedKeys,
                onPress = state::pressKey,
                onRelease = state::releaseKey,
                highlighted = state.selectedGame.controls.map { it.key }.toSet(),
            )
        } else {
            GameControls(
                game = state.selectedGame,
                pressedKeys = state.pressedKeys,
                onPress = state::pressKey,
                onRelease = state::releaseKey,
                showKeyHints = true,
            )
        }
    }
}
