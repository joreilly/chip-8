package dev.johnoreilly.chip8.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.beust.chip8.AssemblyLine
import dev.johnoreilly.chip8.Emulator
import dev.johnoreilly.chip8.ui.resources.Res
import dev.johnoreilly.chip8.ui.theme.Phosphor
import org.jetbrains.compose.resources.ExperimentalResourceApi

/** Speeds offered in the UI, covering the range roms were actually written for. */
enum class SpeedPreset(val label: String, val hz: Int) {
    Slow("Slow", 300),
    Normal("Normal", 600),
    Fast("Fast", 1000),
    Turbo("Turbo", 1600),
}

/**
 * Everything the Chip-8 UI needs to render, holding the presentation state that sits
 * around the emulator itself.
 */
@Stable
class Chip8UiState(val emulator: Emulator) {
    var selectedGame by mutableStateOf(GameCatalog.first())
        private set

    var phosphor by mutableStateOf(Phosphor.Green)
        private set

    /** An explicit speed choice, or null to use the selected game's recommended speed. */
    var speedOverride by mutableStateOf<SpeedPreset?>(null)
        private set

    var showKeypad by mutableStateOf(false)
        private set

    /** Whether the rom library sidebar is showing (wide windows only). */
    var showLibrary by mutableStateOf(true)
        private set

    var isLoadingRom by mutableStateOf(true)
        private set

    var showDisassembly by mutableStateOf(false)
        private set

    var disassembly by mutableStateOf(emptyList<AssemblyLine>())
        private set

    /** Keys currently held, whether by touch or by physical keyboard. */
    var pressedKeys by mutableStateOf(emptySet<Int>())
        private set

    fun selectGame(game: Game) {
        if (game == selectedGame) return
        releaseAllKeys()
        speedOverride = null
        selectedGame = game
    }

    fun cyclePhosphor() {
        phosphor = phosphor.next()
    }

    fun selectSpeed(preset: SpeedPreset) {
        speedOverride = preset
        emulator.setSpeedHz(preset.hz)
    }

    /** The speed in force: an explicit choice, or the game's own recommended default. */
    fun effectiveSpeedHz(): Int = speedOverride?.hz ?: selectedGame.speedHz

    fun toggleKeypad() {
        showKeypad = !showKeypad
    }

    fun toggleLibrary() {
        showLibrary = !showLibrary
    }

    fun toggleDisassembly() {
        showDisassembly = !showDisassembly
    }

    fun pressKey(key: Int) {
        if (key in pressedKeys) return
        pressedKeys = pressedKeys + key
        emulator.keyPressed(key)
    }

    fun releaseKey(key: Int) {
        if (key !in pressedKeys) return
        pressedKeys = pressedKeys - key
        emulator.keyReleased(key)
    }

    fun releaseAllKeys() {
        pressedKeys = emptySet()
        emulator.keyReleased()
    }

    fun reset() {
        releaseAllKeys()
        emulator.reset()
    }

    fun togglePause() {
        releaseAllKeys()
        emulator.togglePause()
    }

    @OptIn(ExperimentalResourceApi::class)
    internal suspend fun loadSelectedRom() {
        isLoadingRom = true
        val romData = Res.readBytes(selectedGame.romPath)
        emulator.setSpeedHz(effectiveSpeedHz())
        emulator.loadRom(romData)
        disassembly = emulator.disassemble()
        isLoadingRom = false
    }
}

@Composable
fun rememberChip8UiState(emulator: Emulator): Chip8UiState {
    val state = remember(emulator) { Chip8UiState(emulator) }

    LaunchedEffect(state, state.selectedGame) {
        state.loadSelectedRom()
    }
    return state
}
