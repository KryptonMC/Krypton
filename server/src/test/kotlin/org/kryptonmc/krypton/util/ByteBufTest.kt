/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
