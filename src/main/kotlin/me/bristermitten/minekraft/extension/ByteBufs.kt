package me.bristermitten.minekraft.extension

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.EncoderException
import java.io.IOException
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.experimental.and


fun ByteBuf.readVarInt(): Int {
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

fun ByteBuf.writeVarInt(value: Int)
{
	var varInt = value
	var part: Int

	while (true) {
		part = varInt and 0x7F
		varInt = varInt ushr 7
		if (varInt != 0) part = part or 0x80
		writeByte(part)
		if (varInt == 0) break
	}
}

fun ByteBuf.readString(maxLength: Short = Short.MAX_VALUE): String
{
	val length = this.readVarInt()

	return when
	{
		length > maxLength * 4 ->
		{
			throw IOException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")")
		}
		length < 0 ->
		{
			throw IOException("The received encoded string buffer length is less than zero! Weird string!")
		}
		else ->
		{
			val array = ByteArray(length)
			this.readBytes(array)
			val s = String(array, UTF_8)
			if (s.length > maxLength)
			{
				throw IOException("The received string length is longer than maximum allowed ($length > $maxLength)")
			} else
			{
				s
			}
		}
	}
}

fun ByteBuf.writeString(s: String)
{
	val bytes = s.toByteArray()
	if (bytes.size > 32767)
	{
		throw EncoderException("String too big (was " + bytes.size + " bytes encoded, max " + 32767 + ")")
	} else
	{
		writeVarInt(bytes.size)
		writeBytes(bytes)
	}
}
