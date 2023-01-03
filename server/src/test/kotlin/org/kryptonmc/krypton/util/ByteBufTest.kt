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
package org.kryptonmc.krypton.util

import io.netty.buffer.Unpooled
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ByteBufTest {

    @Test
    fun `ensure writing string over maximum length fails`() {
        val buffer = Unpooled.buffer()
        assertThrows<EncoderException> { buffer.writeString("Hello World!", 11) }
    }

    @Test
    fun `ensure writing string of maximum length succeeds`() {
        val buffer = Unpooled.buffer()
        assertDoesNotThrow { buffer.writeString("Hello World!", 12) }
    }

    @Test
    fun `ensure writing string writes correct length`() {
        val buffer = Unpooled.buffer()
        buffer.writeString("Hello World!", 12)
        assertEquals(12, buffer.readVarInt())
    }

    @Test
    fun `ensure writing string writes correct bytes`() {
        val buffer = Unpooled.buffer()
        buffer.writeString("Hello World!", 12)
        buffer.readVarInt() // Skip length
        val output = ByteArray(12)
        buffer.readBytes(output)
        assertContentEquals("Hello World!".encodeToByteArray(), output)
    }

    @Test
    fun `ensure reading string over maximum length fails`() {
        val buffer = Unpooled.buffer()
        buffer.writeByte(100)
        assertThrows<DecoderException> { buffer.readString(10) }
    }

    @Test
    fun `ensure reading string of correct length succeeds`() {
        val buffer = Unpooled.buffer()
        buffer.writeString("Hello World!")
        assertEquals("Hello World!", buffer.readString())
    }

    @Test
    fun `ensure random uuid writes correct least and most bits`() {
        val buffer = Unpooled.buffer()
        val uuid = UUID.randomUUID()
        buffer.writeUUID(uuid)
        assertEquals(uuid.mostSignificantBits, buffer.readLong())
        assertEquals(uuid.leastSignificantBits, buffer.readLong())
    }
}
