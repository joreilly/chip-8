package dev.johnoreilly.chip8

import com.beust.chip8.Computer
import com.beust.chip8.Display

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

    fun observeScreenUpdates(success: (List<Boolean>) -> Unit) {
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
