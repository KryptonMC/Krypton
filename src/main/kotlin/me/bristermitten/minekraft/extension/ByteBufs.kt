package me.bristermitten.minekraft.extension

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import kotlin.experimental.and


fun ByteBuf.readVarInt(): Int
{
    var numRead = 0
    var result = 0
    var read: Byte
    do {
        read = readByte()
        val value = (read and 127).toInt()
        result = result or (value shl 7 * numRead)
        numRead++
        if (numRead > 5) {
            throw RuntimeException("VarInt is too big")
        }
    } while (read and 128.toByte() != 0.toByte())

    return result
}

fun ByteBuf.writeVarInt(varInt: Int)
{
    var i = varInt
    while (i and -128 != 0) {
        writeByte(i and 127 or 128)
        i = i ushr 7
    }

    writeByte(i)
}

fun ByteBuf.readString(maxLength: Short = Short.MAX_VALUE): String
{
    val length = this.readVarInt()

    return when {
        length > maxLength * 4 -> {
            throw IOException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")")
        }
        length < 0 -> {
            throw IOException("The received encoded string buffer length is less than zero! Weird string!")
        }
        else -> {
            val array = ByteArray(length)
            this.readBytes(array)
            val s = String(array, UTF_8)
            if (s.length > maxLength) {
                throw IOException("The received string length is longer than maximum allowed ($length > $maxLength)")
            } else {
                s
            }
        }
    }
}

fun ByteBuf.writeString(s: String, maxLength: Short = Short.MAX_VALUE) {
    val bytes = s.toByteArray(Charsets.UTF_8)
    if (bytes.size > maxLength) {
        throw EncoderException("String too big (was " + bytes.size + " bytes encoded, max " + maxLength + ")")
    } else {
        writeVarInt(bytes.size)
        writeBytes(bytes)
    }
}

fun ByteBuf.readVarIntByteArray(): ByteArray {
    val length = readVarInt()
    val readable = readableBytes()
    if (length > readable) {
        throw DecoderException("Not enough bytes to read. Expected Length: $length, actual length: $readable")
    }
    val bytes = ByteArray(length)
    readBytes(bytes)
    return bytes
}

fun ByteBuf.readAllAvailableBytes(): ByteArray {
    val length = readableBytes()
    return readBytes(length).array()
}


fun ByteBuf.writeUUID(uuid: UUID) {
    writeLong(uuid.mostSignificantBits)
    writeLong(uuid.leastSignificantBits)
//    val ints = uuid.toIntArray()
//
//    for (element in ints) {
//        writeInt(element)
//    }
}

//fun ByteBuf.writeNBTCompound(tag: TagCompound) {
//    val byteBuffer = ByteBuffer.allocate(tag.sizeInBytes + 10)
//    tag.writeRoot(byteBuffer)
//    writeBytes(byteBuffer)
//}

fun ByteBuf.writeNBTCompound(tag: CompoundBinaryTag) {
    val outputStream = ByteArrayOutputStream()
    BinaryTagIO.writer().write(tag, outputStream)
    writeBytes(outputStream.toByteArray())
}

fun ByteBuf.writeLongs(array: LongArray) {
    for (element in array) writeLong(element)
}

private fun UUID.toIntArray(): IntArray {
    val mostSig = mostSignificantBits
    val leastSig = leastSignificantBits
    return intArrayOf((mostSig shr 32).toInt(), mostSig.toInt(), (leastSig shr 32).toInt(), leastSig.toInt())
}
