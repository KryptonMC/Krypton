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
package org.kryptonmc.krypton.command.argument

import com.mojang.brigadier.arguments.ArgumentType
import org.junit.jupiter.api.BeforeAll
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.util.readVarInt
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.assertEquals
import kotlin.test.assertFalse

// Named "Abstract" to indicate it's not a test on its own on first glance without having to open the class file
@Suppress("UnnecessaryAbstractClass") // This class is designed to be extended only.
abstract class AbstractArgumentSerializerTest {

    protected fun writeArgumentAndSkipId(argumentType: ArgumentType<*>): ByteBuffer {
        val buffer = ByteBuffer.allocate(128)
        val writer = BinaryWriter(buffer)
        ArgumentSerializers.write(writer, argumentType)
        buffer.flip()
        buffer.readVarInt() // Skip the ID, we don't want to test this for the tests that use this
        return buffer
    }

    protected fun skipFlags(buffer: ByteBuffer) {
        buffer.position(buffer.position() + 1) // Flags are always 1 byte
    }

    protected fun assertFlags(buffer: ByteBuffer, minFlag: Boolean, maxFlag: Boolean) {
        var flags = 0
        if (minFlag) flags = flags or 0x01
        if (maxFlag) flags = flags or 0x02
        assertEquals(flags.toByte(), buffer.get())
    }

    protected fun assertExhausted(buffer: ByteBuffer) {
        assertFalse(buffer.hasRemaining())
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
