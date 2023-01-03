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
package org.kryptonmc.krypton.command.argument

import com.mojang.brigadier.arguments.ArgumentType
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.junit.jupiter.api.BeforeAll
import org.kryptonmc.krypton.util.readVarInt
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.assertEquals
import kotlin.test.assertFalse

// Named "Abstract" to indicate it's not a test on its own on first glance without having to open the class file
@Suppress("UnnecessaryAbstractClass") // This class is designed to be extended only.
abstract class AbstractArgumentSerializerTest {

    protected fun writeArgumentAndSkipId(argumentType: ArgumentType<*>): ByteBuf {
        val buffer = Unpooled.buffer()
        ArgumentSerializers.write(buffer, argumentType)
        buffer.readVarInt() // Skip the ID, we don't want to test this for the tests that use this
        return buffer
    }

    protected fun skipFlags(buffer: ByteBuf) {
        buffer.skipBytes(1) // Flags are always 1 byte
    }

    protected fun assertFlags(buf: ByteBuf, minFlag: Boolean, maxFlag: Boolean) {
        var flags = 0
        if (minFlag) flags = flags or 0x01
        if (maxFlag) flags = flags or 0x02
        assertEquals(flags.toByte(), buf.readByte())
    }

    protected fun assertNotReadable(buf: ByteBuf) {
        assertFalse(buf.isReadable)
    }

    companion object {

        private val serializersBootstrapped = AtomicBoolean()

        @JvmStatic
        @BeforeAll
        fun `bootstrap argument serializers`() {
            if (!serializersBootstrapped.compareAndSet(false, true)) return
            ArgumentSerializers.bootstrap()
        }
    }
}
