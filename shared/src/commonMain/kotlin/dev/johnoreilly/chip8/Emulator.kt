package dev.johnoreilly.chip8

import com.beust.chip8.AssemblyLine
import com.beust.chip8.Computer
import com.beust.chip8.Display
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Screen(val screenData: List<Boolean>)
class Emulator {
    private val display = ComposeDisplay()
    private val computer = Computer(display)

    private val _screen = MutableStateFlow<Screen?>(null)
    val screen: Flow<Screen?> = _screen.asStateFlow()

    fun loadRom(romData: ByteArray) {
        computer.stop()
        computer.loadRom(romData)
        observeScreenUpdates()
    }

    fun stop() {
        computer.stop()
    }

    fun disassemble(): List<AssemblyLine> {
        return computer.disassemble()
    }

    private fun observeScreenUpdates() {
        display.setScreenCallback {
            _screen.value = Screen(it)
        }
    }

    fun keyPressed(key: Int) {
        computer.keyboard.key = key
    }

    fun keyReleased() {
        computer.keyboard.key = null
    }
}


class ComposeDisplay : Display {
    private var screenCallback: ((List<Boolean>) -> Unit)? = null

    override fun draw(frameBuffer: IntArray) {
        val screen = ArrayList<Boolean>(Display.WIDTH * Display.HEIGHT)
        frameBuffer.forEach { value ->
            screen.add(value == 1)
        }
        screenCallback?.invoke(screen)
    }

    override fun clear(frameBuffer: IntArray) {
        frameBuffer.fill(0)
    }

    fun setScreenCallback(screenCallback: (List<Boolean>) -> Unit) {
        this.screenCallback = screenCallback
    }
}
