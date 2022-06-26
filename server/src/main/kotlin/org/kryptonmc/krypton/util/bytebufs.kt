/*
 * This file is part of the Krypton project, and parts of it originate from the
 * Velocity project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 * Copyright (C) 2018 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * For the original file that parts of this file are derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/proxy/src/main/java/com/velocitypowered/proxy/protocol/ProtocolUtils.java
 */
package org.kryptonmc.krypton.util

import com.mojang.brigadier.arguments.ArgumentType
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import kotlinx.collections.immutable.persistentMapOf
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toJson
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.command.argument.ArgumentSerializers
import org.kryptonmc.krypton.item.ItemFactory
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.serialization.CompoundEncoder
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import org.spongepowered.math.vector.Vector3i
import java.io.IOException
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.time.Instant
import java.util.UUID
import kotlin.math.ceil
import kotlin.math.min

fun ByteBuf.writeShort(short: Short) {
    writeShort(short.toInt())
}

fun ByteBuf.readVarInt(): Int {
    var i = 0
    val maxRead = min(5, readableBytes())
    for (j in 0 until maxRead) {
        val next = readByte()
        i = i or (next.toInt() and 0x7F shl j * 7)
        if (next.toInt() and 0x80 != 128) return i // If there's no more var int bytes after this, we're done
    }
    return Int.MAX_VALUE
}

// This came from Velocity. See https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
fun ByteBuf.writeVarInt(value: Int) {
    when {
        value.toLong() and (0xFFFFFFFF shl 7) == 0L -> writeByte(value)
        value.toLong() and (0xFFFFFFFF shl 14) == 0L -> writeShort(value and 0x7F or 0x80 shl 8 or (value ushr 7))
        value.toLong() and (0xFFFFFFFF shl 21) == 0L -> writeMedium(value and 0x7F or 0x80 shl 16 or
                (value ushr 7 and 0x7F or 0x80 shl 8) or (value ushr 14))
        value.toLong() and (0xFFFFFFFF shl 28) == 0L -> writeInt(value and 0x7F or 0x80 shl 24 or
                (value ushr 7 and 0x7F or 0x80 shl 16) or (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21))
        else -> {
            writeInt(value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or
                    (value shr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80))
            writeByte(value ushr 28)
        }
    }
}

// This is my own completely crazy extension of the above `writeVarInt` function, created by Andrew Steinborn of Velocity,
// to the 10-bit var long. This is pretty much completely unnecessary, but it functions as expected, and was a challenge in
// the article cited on the above method, so I thought it was worth putting it in here.
fun ByteBuf.writeVarLong(value: Long) {
    when {
        value and (0xFFFFFFFF shl 7) == 0L -> writeByte(value.toInt())
        value and (0xFFFFFFFF shl 14) == 0L -> writeShort((value and 0x7F or 0x80 shl 8 or (value ushr 7)).toInt())
        value and (0xFFFFFFFF shl 21) == 0L -> writeMedium((value and 0x7F or 0x80 shl 16 or
                (value ushr 7 and 0x7F or 0x80 shl 8) or (value ushr 14)).toInt())
        value and (0xFFFFFFFF shl 28) == 0L -> writeInt((value and 0x7F or 0x80 shl 24 or
                (value ushr 7 and 0x7F or 0x80 shl 16) or (value ushr 14 and 0x7F or 0x80 shl 8) or
                (value ushr 21)).toInt())
        value and (0xFFFFFFFF shl 35) == 0L -> {
            writeInt((value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or
                    (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80)).toInt())
            writeByte((value ushr 28).toInt())
        }
        value and (0xFFFFFFFF shl 42) == 0L -> {
            writeInt((value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or
                    (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80)).toInt())
            writeShort((value ushr 28 and 0x7F or 0x80 shl 8 or (value ushr 35)).toInt())
        }
        value and (0xFFFFFFFF shl 49) == 0L -> {
            writeInt((value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or
                    (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80)).toInt())
            writeMedium((value ushr 28 and 0x7F or 0x80 shl 16 or (value ushr 35 and 0x7F or 0x80 shl 8) or
                    (value ushr 42)).toInt())
        }
        value and (0xFFFFFFFF shl 56) == 0L -> writeLong((value and 0x7F or 0x80 shl 56 or
                (value ushr 7 and 0x7F or 0x80 shl 48) or (value ushr 14 and 0x7F or 0x80 shl 40) or
                (value ushr 21 and 0x7F or 0x80 shl 32) or (value ushr 28 and 0x7F or 0x80 shl 24) or
                (value ushr 35 and 0x7F or 0x80 shl 16) or (value ushr 42 and 0x7F or 0x80 shl 8) or
                (value ushr 49)))
        value and (0xFFFFFFFF shl 63) == 0L -> {
            writeLong((value and 0x7F or 0x80 shl 56 or (value ushr 7 and 0x7F or 0x80 shl 48) or
                    (value ushr 14 and 0x7F or 0x80 shl 40) or (value ushr 21 and 0x7F or 0x80 shl 32) or
                    (value ushr 28 and 0x7F or 0x80 shl 24) or (value ushr 35 and 0x7F or 0x80 shl 16) or
                    (value ushr 42 and 0x7F or 0x80 shl 8) or (value ushr 49 and 0x7F or 0x80)))
            writeByte((value ushr 56).toInt())
        }
        value and (0xFFFFFFFF shl 70) == 0L -> {
            writeLong((value and 0x7F or 0x80 shl 56 or (value ushr 7 and 0x7F or 0x80 shl 48) or
                    (value ushr 14 and 0x7F or 0x80 shl 40) or (value ushr 21 and 0x7F or 0x80 shl 32) or
                    (value ushr 28 and 0x7F or 0x80 shl 24) or (value ushr 35 and 0x7F or 0x80 shl 16) or
                    (value ushr 42 and 0x7F or 0x80 shl 8) or (value ushr 49 and 0x7F or 0x80)))
            writeShort((value ushr 56 and 0x7F or 0x80 shl 8 or (value ushr 63)).toInt())
        }
    }
}

fun ByteBuf.readString(max: Int = Short.MAX_VALUE.toInt()): String {
    val length = readVarInt()
    return when {
        length > max * 4 -> throw IOException("String too long! Expected maximum length of $max, got length of $length!")
        length < 0 -> throw IOException("String cannot be less than 0 in length!")
        else -> {
            val string = String(readAvailableBytes(length))
            if (string.length > max) throw IOException("String too long! Expected maximum length of $max, got length of ${string.length}")
            string
        }
    }
}

fun ByteBuf.writeString(value: String, max: Short = Short.MAX_VALUE) {
    val bytes = value.encodeToByteArray()
    if (bytes.size > max) throw EncoderException("String too long! Expected maximum size of $max, got length ${value.length}!")
    writeVarInt(bytes.size)
    writeBytes(bytes)
}

fun ByteBuf.readVarIntByteArray(): ByteArray = readAvailableBytes(readVarInt())

fun ByteBuf.readAvailableBytes(length: Int): ByteArray {
    val bytes = ByteArray(length)
    readBytes(bytes, 0, length)
    return bytes
}

fun ByteBuf.readAllAvailableBytes(): ByteArray = readAvailableBytes(readableBytes())

fun ByteBuf.writeVarIntByteArray(bytes: ByteArray) {
    writeVarInt(bytes.size)
    writeBytes(bytes)
}

fun ByteBuf.writeLongArray(array: LongArray) {
    writeVarInt(array.size)
    for (i in array.indices) {
        writeLong(array[i])
    }
}

fun ByteBuf.writeSingletonLongArray(element: Long) {
    writeVarInt(1)
    writeLong(element)
}

fun ByteBuf.writeUUID(uuid: UUID) {
    writeLong(uuid.mostSignificantBits)
    writeLong(uuid.leastSignificantBits)
}

fun ByteBuf.writeNBT(tag: CompoundTag?) {
    if (tag == null) {
        writeByte(0)
        return
    }
    try {
        TagIO.write(ByteBufOutputStream(this), tag, TagCompression.NONE)
    } catch (exception: IOException) {
        throw EncoderException(exception)
    }
}

fun ByteBuf.readNBT(): CompoundTag {
    val index = readerIndex()
    val type = readByte()
    if (type == 0.toByte()) return CompoundTag.empty()
    readerIndex(index) // reset the head if it's not an end tag

    try {
        return TagIO.read(ByteBufInputStream(this), TagCompression.NONE)
    } catch (exception: IOException) {
        throw DecoderException(exception)
    }
}

fun ByteBuf.writeChat(component: Component) {
    writeString(component.toJson())
}

fun ByteBuf.readItem(): KryptonItemStack {
    if (!readBoolean()) return KryptonItemStack.EMPTY
    val id = readVarInt()
    val count = readByte()
    val nbt = readNBT()
    val type = Registries.ITEM[id]
    return KryptonItemStack(type, count.toInt(), ItemFactory.create(type, nbt))
}

fun ByteBuf.writeItem(item: KryptonItemStack) {
    if (item === KryptonItemStack.EMPTY) {
        writeBoolean(false)
        return
    }
    writeBoolean(true)
    writeVarInt(Registries.ITEM.idOf(item.type))
    writeByte(item.amount)
    writeNBT(item.meta.data)
}

fun ByteBuf.writeVector(x: Int, y: Int, z: Int) {
    writeLong(((x.toLong() and 0x3FFFFFF) shl 38) or ((z.toLong() and 0x3FFFFFF) shl 12) or (y.toLong() and 0xFFF))
}

fun ByteBuf.writeAngle(angle: Float) {
    writeByte(((angle / 360F) * 256F).toInt())
}

fun ByteBuf.writeKey(key: Key) {
    writeString(key.asString())
}

@Suppress("UNCHECKED_CAST")
fun <T : ArgumentType<*>> ByteBuf.writeArgumentType(type: T) {
    val entry = ArgumentSerializers.get(type) ?: return writeKey(Key.key(""))
    writeKey(entry.name)
    entry.serializer.write(this, type)
}

inline fun <reified T : Enum<T>> ByteBuf.readEnum(): T = T::class.java.enumConstants[readVarInt()]

fun ByteBuf.writeEnum(enum: Enum<*>) {
    writeVarInt(enum.ordinal)
}

fun <T> ByteBuf.writeOptional(value: T?, presentAction: (T) -> Unit) {
    writeBoolean(value != null)
    if (value != null) presentAction(value)
}

fun <E> ByteBuf.writeCollection(collection: Collection<E>, action: (E) -> Unit) {
    writeVarInt(collection.size)
    collection.forEach { action(it) }
}

fun <K, V> ByteBuf.writeMap(map: Map<K, V>, keyAction: (ByteBuf, K) -> Unit, valueAction: (ByteBuf, V) -> Unit) {
    writeVarInt(map.size)
    map.forEach { (key, value) ->
        keyAction(this, key)
        valueAction(this, value)
    }
}

fun <K, V> ByteBuf.readMap(keyReader: (ByteBuf) -> K, valueReader: (ByteBuf) -> V): Map<K, V> {
    val map = persistentMapOf<K, V>().builder()
    for (i in 0 until readVarInt()) {
        map[keyReader(this)] = valueReader(this)
    }
    return map.build()
}

fun ByteBuf.writeIntArray(array: IntArray) {
    writeVarInt(array.size)
    for (i in array.indices) {
        writeVarInt(array[i])
    }
}

fun <T> ByteBuf.encode(encoder: CompoundEncoder<T>, value: T) {
    val result = try {
        encoder.encode(value)
    } catch (exception: Exception) {
        throw EncoderException("Failed to encode value $value with encoder $encoder!", exception)
    }
    writeNBT(result)
}

fun ByteBuf.readInstant(): Instant = Instant.ofEpochMilli(readLong())

fun ByteBuf.writeInstant(instant: Instant) {
    writeLong(instant.toEpochMilli())
}

fun ByteBuf.readPublicKey(): PublicKey {
    try {
        val spec = X509EncodedKeySpec(readVarIntByteArray())
        return KeyFactory.getInstance("RSA").generatePublic(spec)
    } catch (exception: Exception) {
        throw DecoderException("Failed to decode public key!", exception)
    }
}

fun ByteBuf.write3EmptyBytes(): Int {
    val index = writerIndex()
    writeMedium(0)
    return index
}

fun ByteBuf.write3ByteVarInt(startIndex: Int, value: Int) {
    val originalIndex = writerIndex()
    writerIndex(startIndex)
    val encoded = (((value and 0x7F) or 0x80) shl 16) or ((((value ushr 7) and 0x7F) or 0x80) shl 8) or (value ushr 14)
    writeMedium(encoded)
    writerIndex(originalIndex)
}

private val VARINT_EXACT_BYTE_LENGTHS = IntArray(33) { ceil((31.0 - (it - 1)) / 7.0).toInt() }.apply { this[32] = 1 }

fun Int.varIntBytes(): Int = VARINT_EXACT_BYTE_LENGTHS[countLeadingZeroBits()]
