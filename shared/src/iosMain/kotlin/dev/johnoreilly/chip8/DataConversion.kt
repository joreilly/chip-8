package dev.johnoreilly.chip8

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
fun convertNSDataToByteArray(data: NSData): ByteArray {
    val size = data.length.toInt()
    val byteArray = ByteArray(size)

    // Copy from NSData into Kotlin ByteArray efficiently
    byteArray.usePinned { dest ->
        val destPtr = dest.addressOf(0)
        val srcPtr = data.bytes?.reinterpret<ByteVar>()
            ?: error("NSData.bytes is null")
        memcpy(destPtr, srcPtr, data.length)
    }
    return byteArray
}
