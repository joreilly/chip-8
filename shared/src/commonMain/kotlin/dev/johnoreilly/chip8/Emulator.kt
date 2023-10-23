package dev.johnoreilly.chip8

import com.beust.chip8.Computer
import com.beust.chip8.Display
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class Emulator {
    private val display = ComposeDisplay()
    private val computer = Computer(display)

    fun loadRom(romData: ByteArray) {
        computer.stop()
        computer.loadRom(romData)
    }

    fun stop() {
        computer.stop()
    }

    fun disassemble(): List<Computer.AssemblyLine> {
        return computer.disassemble()
    }

    fun observeScreenUpdates(success: (ImmutableList<Boolean>) -> Unit) {
        display.setScreenCallback {
            success(it)
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
    private var screenCallback: ((ImmutableList<Boolean>) -> Unit)? = null

    override fun draw(frameBuffer: IntArray) {
        val screen = ArrayList<Boolean>(Display.WIDTH * Display.HEIGHT)
        frameBuffer.forEach { value ->
            screen.add(value == 1)
        }
        screenCallback?.invoke(screen.toImmutableList())
    }

    override fun clear(frameBuffer: IntArray) {
        frameBuffer.fill(0)
    }

    fun setScreenCallback(screenCallback: (ImmutableList<Boolean>) -> Unit) {
        this.screenCallback = screenCallback
    }
}
