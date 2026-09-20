package com.beust.chip8

import kotlinx.coroutines.*

/**
 * For clients that want to be notified when something happens on the computer.
 */
interface ComputerListener {
    fun onPause() {}
    fun onStart() {}
    fun onSoundChanged(isBeeping: Boolean) {}
}


data class AssemblyLine(val counter: Int, val byte0: Byte, val byte1: Byte, val name: String)


internal class Computer(val display: Display,
        val keyboard: Keyboard = Keyboard(),
        val frameBuffer: FrameBuffer = FrameBuffer(),
        var cpu: Cpu = Cpu(),
        val sound: Boolean = true)
{
    var isRunning: Boolean = false
        private set

    /**
     * How many instructions the CPU executes per second. Games were written for a wide
     * range of real-world speeds, so this is adjustable from the UI.
     */
    var speedHz: Int = DEFAULT_SPEED_HZ
        set(value) {
            field = value.coerceIn(MIN_SPEED_HZ, MAX_SPEED_HZ)
            if (isRunning) {
                // Restart the loops so the new speed takes effect immediately.
                launchTimers()
            }
        }

    /** True while the Chip-8 sound timer is counting down, i.e. the machine is beeping. */
    var isBeeping: Boolean = false
        private set

    private var cpuTickJob: Job? = null
    private var timerFutureJob: Job? = null
    private var romData: ByteArray? = null

    private var listener: ComputerListener? = null

    private fun unsigned(b: Byte): Int = if (b < 0) b + 0x10 else b.toInt()

    private val scope = MainScope()

    fun setListener(listener: ComputerListener?) {
        this.listener = listener
    }

    fun loadRom(romData: ByteArray, launchTimers: Boolean = true) {
        this.romData = romData
        resetCpu()
        if (launchTimers) {
            start()
        }
    }

    private fun resetCpu() {
        cpu = Cpu()
        keyboard.releaseAll()
        romData?.let {
            cpu.loadRom(it)
        }
    }

    fun stop() {
        pause()
        frameBuffer.frameBuffer.fill(0)
        display.clear(frameBuffer.frameBuffer)
        resetCpu()
    }

    /** Stop, clear the screen and start the currently loaded rom again from the beginning. */
    fun reset() {
        stop()
        if (romData != null) {
            start()
        }
    }

    fun pause() {
        if (!isRunning) return
        isRunning = false
        cancelTimers()
        keyboard.releaseAll()
        setBeeping(false)
        listener?.onPause()
    }

    fun start() {
        if (isRunning) return
        isRunning = true
        launchTimers()
        listener?.onStart()
    }

    private fun cancelTimers() {
        cpuTickJob?.cancel()
        cpuTickJob = null
        timerFutureJob?.cancel()
        timerFutureJob = null
    }

    private fun setBeeping(beeping: Boolean) {
        if (isBeeping != beeping) {
            isBeeping = beeping
            listener?.onSoundChanged(beeping)
        }
    }

    private fun nextInstruction(pc: Int = cpu.PC) : Instruction {
        fun extract(pc: Int): Pair<Int, Int> {
            val b = cpu.memory[pc]
            val b0 = unsigned(b.toInt().shr(4).toByte())
            val b1 = unsigned(b.toInt().and(0xf).toByte())
            return Pair(b0, b1)
        }
        val (b0, b1) = extract(pc)
        val (b2, b3) = extract(pc + 1)

        return Instruction(this@Computer, b0, b1, b2, b3)
    }

    private fun launchTimers() {
        cancelTimers()

        // Run a batch of instructions per frame rather than sleeping between each one:
        // timer granularity (4ms+ in browsers) makes per-instruction delays wildly
        // inaccurate, which is what made the emulator feel sluggish on some platforms.
        val cyclesPerFrame = ((speedHz * FRAME_MILLIS) / 1000).coerceAtLeast(1).toInt()
        cpuTickJob = startCoroutineTimer(repeatMillis = FRAME_MILLIS) {
            repeat(cyclesPerFrame) {
                nextInstruction().run()
            }
        }

        // The Chip-8 delay and sound timers always tick at 60Hz, independent of CPU speed.
        timerFutureJob = startCoroutineTimer(repeatMillis = FRAME_MILLIS) {
            if (cpu.DT > 0) {
                cpu.DT--
            }
            if (cpu.ST > 0) {
                cpu.ST--
            }
            setBeeping(sound && cpu.ST > 0)
        }
    }


    /**
     * Disassemble the loaded rom. Defaults to the whole rom from its load address rather
     * than a fixed instruction count, which previously ran on past the end into garbage.
     */
    fun disassemble(from: Int = PROGRAM_START): List<AssemblyLine> {
        val instructionCount = ((romData?.size ?: 0) / 2).coerceAtMost(MAX_DISASSEMBLY_LINES)
        var pc = from
        val result = ArrayList<AssemblyLine>(instructionCount)
        repeat(instructionCount) {
            if (pc + 1 >= cpu.memory.size) return result
            val inst = nextInstruction(pc)
            result.add(AssemblyLine(pc, cpu.memory[pc], cpu.memory[pc + 1], inst.toString()))
            pc += 2
        }
        return result
    }


    private fun startCoroutineTimer(delayMillis: Long = 0, repeatMillis: Long = 0, action: () -> Unit)  = scope.launch {
            delay(delayMillis)
            if (repeatMillis > 0) {
                while (true) {
                    action()
                    delay(repeatMillis)
                }
            } else {
                action()
            }
        }

    companion object {
        /** 60Hz, the rate the Chip-8 delay and sound timers run at. */
        private const val FRAME_MILLIS = 16L

        /** Roms are loaded at 0x200; the bytes below that hold the font sprites. */
        const val PROGRAM_START = 0x200
        private const val MAX_DISASSEMBLY_LINES = 2048

        const val MIN_SPEED_HZ = 100
        const val DEFAULT_SPEED_HZ = 600
        const val MAX_SPEED_HZ = 2000
    }
}

