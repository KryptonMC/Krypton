/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
 */
package org.kryptonmc.krypton

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import net.kyori.adventure.text.Component
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.Bootstrap
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.util.readAllAvailableBytes
import org.kryptonmc.krypton.util.readAvailableBytes
import org.kryptonmc.krypton.util.readById
import org.kryptonmc.krypton.util.readItem
import org.kryptonmc.krypton.util.readNBT
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeId
import org.kryptonmc.krypton.util.writeItem
import org.kryptonmc.krypton.util.writeLongArray
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntByteArray
import org.kryptonmc.krypton.util.writeVarLong
import java.util.UUID
import kotlin.math.min
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ByteBufTests {

    private val random = RandomSource.createThreadLocal()

    @Test
    fun `test var int reading and writing`() {
        val buffer = Unpooled.buffer()
        buffer.writeVarInt(0)
        assertEquals(0, buffer.readVarInt())
        buffer.clear()
        buffer.writeVarInt(127) // 2^7 - 1 (maximum for 1 byte var ints)
        assertEquals(127, buffer.readVarInt())
        buffer.clear()
        buffer.writeVarInt(16383) // 2^14 - 1 (maximum for 2 byte var ints)
        assertEquals(16383, buffer.readVarInt())
        buffer.clear()
        buffer.writeVarInt(2097151) // 2^21 - 1 (maximum for 3 byte var ints)
        assertEquals(2097151, buffer.readVarInt())
        buffer.clear()
        buffer.writeVarInt(268435455) // 2^28 - 1 (maximum for 4 byte var ints)
        assertEquals(268435455, buffer.readVarInt())
        buffer.clear()
        buffer.writeVarInt(-1)
        assertEquals(-1, buffer.readVarInt())
        buffer.clear()
        buffer.writeVarLong(Long.MAX_VALUE)
        assertEquals(Int.MAX_VALUE, buffer.readVarInt()) // We just want to make sure that any more than 5 bytes gives us the int max value
    }

    @Test
    fun `test var long writing`() {
        val buffer = Unpooled.buffer()
        buffer.writeVarLong(0)
        assertEquals(0, buffer.readVarLong())
        buffer.clear()
        buffer.writeVarLong(127) // 2^7 - 1 (maximum for 1 byte var longs)
        assertEquals(127, buffer.readVarLong())
        buffer.clear()
        buffer.writeVarLong(16383) // 2^14 - 1 (maximum for 2 byte var longs)
        assertEquals(16383, buffer.readVarLong())
        buffer.clear()
        buffer.writeVarLong(2097151) // 2^21 - 1 (maximum for 3 byte var longs)
        assertEquals(2097151, buffer.readVarLong())
        buffer.clear()
        buffer.writeVarLong(268435455) // 2^28 - 1 (maximum for 4 byte var longs)
        assertEquals(268435455, buffer.readVarLong())
        buffer.clear()
        buffer.writeVarLong(34359738367) // 2^35 - 1 (maximum for 5 byte var longs)
        assertEquals(34359738367, buffer.readVarLong())
        buffer.clear()
        buffer.writeVarLong(4398046511103) // 2^42 - 1 (maximum for 6 byte var longs)
        assertEquals(4398046511103, buffer.readVarLong())
        buffer.clear()
        buffer.writeVarLong(562949953421311) // 2^49 - 1 (maximum for 7 byte var longs)
        assertEquals(562949953421311, buffer.readVarLong())
        buffer.clear()
        buffer.writeVarLong(72057594037927935) // 2^56 - 1 (maximum for 8 byte var longs)
        assertEquals(72057594037927935, buffer.readVarLong())
        buffer.clear()
        buffer.writeVarLong(Long.MAX_VALUE)
        assertEquals(Long.MAX_VALUE, buffer.readVarLong())
        buffer.clear()
    }

    @Test
    fun `test string writing`() {
        val buffer = Unpooled.buffer()
        assertThrows<EncoderException> { buffer.writeString(TEST_STRING, 10) }
        buffer.writeString(TEST_STRING)
        assertEquals(TEST_STRING_BYTES.size, buffer.readByte().toInt())
        for (i in TEST_STRING_BYTES.indices) {
            assertEquals(TEST_STRING_BYTES[i], buffer.readByte())
        }
    }

    @Test
    fun `test string reading`() {
        val buffer = Unpooled.buffer()
        buffer.writeByte(100)
        assertThrows<DecoderException> { buffer.readString(10) }
        buffer.clear()
        buffer.writeString("Hello World!-")
        assertThrows<DecoderException> { buffer.readString(12) }
        buffer.clear()
        buffer.writeString("Hello World!")
        assertEquals("Hello World!", buffer.readString())
    }

    @Test
    fun `test array writing`() {
        val buffer = Unpooled.buffer()
        // Byte array
        val bytes = ByteArray(32) { random.nextInt(Byte.MAX_VALUE.toInt()).toByte() }
        buffer.writeVarIntByteArray(bytes)
        assertEquals(32, buffer.readVarInt())
        for (i in 0 until 32) {
            assertEquals(bytes[i], buffer.readByte())
        }
        buffer.clear()
        // Long array
        val longs = LongArray(256) { random.nextLong() }
        buffer.writeLongArray(longs)
        assertEquals(256, buffer.readVarInt())
        for (i in 0 until 256) {
            assertEquals(longs[i], buffer.readLong())
        }
    }

    @Test
    fun `test byte array reading`() {
        val buffer = Unpooled.buffer()
        val bytes = ByteArray(32) { random.nextInt(Byte.MAX_VALUE.toInt()).toByte() }
        buffer.writeBytes(bytes)
        assertContentEquals(bytes, buffer.readAllAvailableBytes())
        buffer.clear()
        buffer.writeBytes(bytes.copyOfRange(0, 24))
        assertContentEquals(bytes.copyOfRange(0, 24), buffer.readAvailableBytes(24))
        buffer.clear()
        buffer.writeVarInt(32)
        buffer.writeBytes(bytes)
        assertContentEquals(bytes, buffer.readVarIntByteArray())
    }

    @Test
    fun `test uuid writing`() {
        val buffer = Unpooled.buffer()
        val uuid = UUID.randomUUID()
        buffer.writeUUID(uuid)
        assertEquals(uuid.mostSignificantBits, buffer.readLong())
        assertEquals(uuid.leastSignificantBits, buffer.readLong())
    }

    @Test
    fun `test nbt reading and writing`() {
        val buffer = Unpooled.buffer()
        buffer.writeNBT(null)
        assertEquals(0, buffer.readByte())
        assertEquals(0, buffer.readableBytes())
        buffer.clear()
        buffer.writeByte(0)
        assertTrue(buffer.readNBT().isEmpty)
    }

    @Test
    fun `test item writing`() {
        val buffer = Unpooled.buffer()
        buffer.writeItem(KryptonItemStack.EMPTY)
        assertEquals(false, buffer.readBoolean())
        assertEquals(0, buffer.readableBytes())
        buffer.clear()

        val type = ItemTypes.STONE.downcast()
        val item = KryptonItemStack.Builder().type(type).amount(3).meta { name(Component.text("Hello World!")) }.build()
        buffer.writeItem(item)
        assertEquals(true, buffer.readBoolean())
        assertEquals(type, buffer.readById(KryptonRegistries.ITEM))
        assertEquals(3, buffer.readByte())
        assertEquals(item.meta.data, buffer.readNBT())
    }

    @Test
    fun `test item reading`() {
        val buffer = Unpooled.buffer()
        buffer.writeBoolean(false)
        assertSame(KryptonItemStack.EMPTY, buffer.readItem())
        buffer.clear()

        val item = KryptonItemStack.Builder().type(ItemTypes.STONE).amount(3).meta { name(Component.text("Hello World!")) }.build()
        buffer.writeBoolean(true)
        buffer.writeId(KryptonRegistries.ITEM, item.type)
        buffer.writeByte(3)
        buffer.writeNBT(item.meta.data)
        assertEquals(item, buffer.readItem())
    }

    companion object {

        private const val TEST_STRING = "Hello World!"
        private val TEST_STRING_BYTES = TEST_STRING.encodeToByteArray()

        @JvmStatic
        @BeforeAll
        fun `preload bootstrap`() {
            Bootstrap.preload()
        }
    }
}

private fun ByteBuf.readVarLong(): Long {
    var i = 0L
    val maxRead = min(10, readableBytes())
    for (j in 0 until maxRead) {
        val next = readByte()
        i = i or (next.toLong() and 0x7F shl j * 7)
        if (next.toLong() and 0x80 != 128L) return i // If there's no more var long bytes after this, we're done
    }
    return Long.MAX_VALUE
}
