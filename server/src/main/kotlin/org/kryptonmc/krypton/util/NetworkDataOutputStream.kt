package org.kryptonmc.krypton.util

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

/**
 * An output stream that wraps a [ByteArrayOutputStream] and allows us to write things like null-separated
 * string lists with ease.
 *
 * Used in the GS4 query handler.
 *
 * @author Callum Seabrook
 */
class NetworkDataOutputStream(size: Int) {

    private val outputStream = ByteArrayOutputStream(size)
    private val dataOutputStream = DataOutputStream(outputStream)

    fun write(bytes: ByteArray) = dataOutputStream.write(bytes, 0, bytes.size)

    fun write(string: String) {
        dataOutputStream.writeBytes(string)
        dataOutputStream.write(0)
    }

    fun write(value: Int) = dataOutputStream.write(value)

    fun write(value: Short) = dataOutputStream.writeShort(java.lang.Short.reverseBytes(value).toInt())

    fun reset() = outputStream.reset()

    val byteArray: ByteArray
        get() = outputStream.toByteArray()
}