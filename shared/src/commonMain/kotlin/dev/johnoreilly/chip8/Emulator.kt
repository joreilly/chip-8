package dev.johnoreilly.chip8

import com.beust.chip8.AssemblyLine
import com.beust.chip8.Computer
import com.beust.chip8.ComputerListener
import com.beust.chip8.Display
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Screen(val screenData: List<Boolean>)

class Emulator {
    private val display = ComposeDisplay()
    private val computer = Computer(display)

    private val _screen = MutableStateFlow(Screen(emptyList()))
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    /** True while the CPU is executing, false when paused or stopped. */
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _isBeeping = MutableStateFlow(false)
    /** True while the rom is driving the sound timer, for clients that show or play a beep. */
    val isBeeping: StateFlow<Boolean> = _isBeeping.asStateFlow()

    private val _speedHz = MutableStateFlow(Computer.DEFAULT_SPEED_HZ)
    /** Instructions executed per second. */
    val speedHz: StateFlow<Int> = _speedHz.asStateFlow()

    init {
        display.setScreenCallback { _screen.value = Screen(it) }
        computer.setListener(object : ComputerListener {
            override fun onStart() { _isRunning.value = true }
            override fun onPause() { _isRunning.value = false }
            override fun onSoundChanged(isBeeping: Boolean) { _isBeeping.value = isBeeping }
        })
    }

    fun loadRom(romData: ByteArray) {
        computer.stop()
        computer.loadRom(romData)
    }

    fun stop() {
        computer.stop()
    }

    /** Pause execution, leaving the current screen contents visible. */
    fun pause() {
        computer.pause()
    }

    /** Resume execution after [pause]. */
    fun resume() {
        computer.start()
    }

    fun togglePause() {
        if (computer.isRunning) computer.pause() else computer.start()
    }

    /** Restart the current rom from the beginning. */
    fun reset() {
        computer.reset()
    }

    fun setSpeedHz(speedHz: Int) {
        computer.speedHz = speedHz
        _speedHz.value = computer.speedHz
    }

    /** Disassembly of the loaded rom, from its load address. */
    fun disassemble(): List<AssemblyLine> {
        return computer.disassemble()
    }

    fun keyPressed(key: Int) {
        computer.keyboard.press(key)
    }

    fun keyReleased(key: Int) {
        computer.keyboard.release(key)
    }

    /** Release every held key. */
    fun keyReleased() {
        computer.keyboard.releaseAll()
    }

    companion object {
        val MIN_SPEED_HZ = Computer.MIN_SPEED_HZ
        val DEFAULT_SPEED_HZ = Computer.DEFAULT_SPEED_HZ
        val MAX_SPEED_HZ = Computer.MAX_SPEED_HZ
    }
}


class ComposeDisplay : Display {
    private var screenCallback: ((List<Boolean>) -> Unit)? = null

    override fun draw(frameBuffer: IntArray) {
        screenCallback?.invoke(frameBuffer.map { it == 1 })
    }

    override fun clear(frameBuffer: IntArray) {
        frameBuffer.fill(0)
        // Push the cleared frame out too, otherwise a stop/reset leaves the last
        // rendered image on screen.
        screenCallback?.invoke(List(Display.WIDTH * Display.HEIGHT) { false })
    }

    fun setScreenCallback(screenCallback: (List<Boolean>) -> Unit) {
        this.screenCallback = screenCallback
    }
}
