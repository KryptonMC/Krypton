/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.command.argument.ArgumentTypes
import org.kryptonmc.krypton.command.argument.ArgumentTypes.writeArgumentType
import org.kryptonmc.krypton.command.argument.serializer.ArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.EmptyArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.DoubleArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.FloatArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.IntegerArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.LongArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.StringArgumentSerializer
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class ArgumentTests {

    @Test
    fun `argument types get call returns not null for registered types`() {
        assertNotNull(ArgumentTypes["brigadier:bool".toNamespacedKey()])
        assertNotNull(ArgumentTypes["brigadier:float".toNamespacedKey()])
        assertNotNull(ArgumentTypes["brigadier:double".toNamespacedKey()])
        assertNotNull(ArgumentTypes["brigadier:integer".toNamespacedKey()])
        assertNotNull(ArgumentTypes["brigadier:long".toNamespacedKey()])
        assertNotNull(ArgumentTypes["brigadier:string".toNamespacedKey()])
    }

    @Test
    fun `argument types write calls error when entry is null`() {
        val buffer = Unpooled.buffer().apply { writeArgumentType(BogusArgumentType()) }
        assertEquals("minecraft:", buffer.readString())
        assertFalse(buffer.isReadable)
    }

    @Test
    fun `valid argument type writing for empty serializer`() {
        val buffer = Unpooled.buffer().apply { writeArgumentType(BoolArgumentType.bool()) }
        assertEquals("brigadier:bool", buffer.readString())
        assertFalse(buffer.isReadable)
    }

    @Test
    fun `valid argument type writing for integer serializer`() {
        val buffer = Unpooled.buffer().apply { writeArgumentType(IntegerArgumentType.integer(10, 20)) }
        assertEquals("brigadier:integer", buffer.readString())
        assertEquals(0b00000011, buffer.readByte())
        assertEquals(10, buffer.readInt())
        assertEquals(20, buffer.readInt())
    }

    @Test
    fun `register calls for existing types fail and new types succeed`() {
        val method = ArgumentTypes::class.java.getDeclaredMethod(
            "register",
            String::class.java,
            KClass::class.java,
            ArgumentSerializer::class.java
        ).apply { isAccessible = true }

        assertThrows<IllegalArgumentException> {
            throwCause { method(ArgumentTypes, "brigadier:bool", BoolArgumentType::class, EmptyArgumentSerializer<BoolArgumentType>()) }
        }
        assertThrows<IllegalArgumentException> {
            throwCause { method(ArgumentTypes, "brigadier:bool", BogusArgumentType::class, EmptyArgumentSerializer<BogusArgumentType>()) }
        }
        assertDoesNotThrow {
            throwCause { method(ArgumentTypes, "krypton:test", BogusArgumentType::class, EmptyArgumentSerializer<BogusArgumentType>()) }
        }

        callRemove("BY_CLASS", BogusArgumentType::class)
        callRemove("BY_NAME", NamespacedKey("krypton", "test"))
    }

    @Test
    fun `serializer flag creating`() {
        EmptyArgumentSerializer<BoolArgumentType>().checkFlags()
        DoubleArgumentSerializer().checkFlags()
        FloatArgumentSerializer().checkFlags()
        IntegerArgumentSerializer().checkFlags()
        LongArgumentSerializer().checkFlags()
        StringArgumentSerializer().checkFlags()
    }

    @Test
    fun `number argument serializers min and max`() {
        val buf = Unpooled.buffer()

        DoubleArgumentSerializer().write(DoubleArgumentType.doubleArg(10.0, 20.0), buf)
        buf.readByte()
        assertEquals(10.0, buf.readDouble())
        assertEquals(20.0, buf.readDouble())

        FloatArgumentSerializer().write(FloatArgumentType.floatArg(10F, 20F), buf)
        buf.readByte()
        assertEquals(10F, buf.readFloat())
        assertEquals(20F, buf.readFloat())

        IntegerArgumentSerializer().write(IntegerArgumentType.integer(10, 20), buf)
        buf.readByte()
        assertEquals(10, buf.readInt())
        assertEquals(20, buf.readInt())

        LongArgumentSerializer().write(LongArgumentType.longArg(10L, 20L), buf)
        buf.readByte()
        assertEquals(10L, buf.readLong())
        assertEquals(20L, buf.readLong())
    }

    @Test
    fun `string argument serializer`() {
        val buf = Unpooled.buffer()
        StringArgumentSerializer().write(StringArgumentType.greedyString(), buf)
        assertEquals(2, buf.readVarInt())
    }

    companion object {

        @BeforeAll
        @JvmStatic
        fun `preload needed classes`() {
            Class.forName("org.kryptonmc.krypton.command.argument.ArgumentTypes")
        }

        private fun callRemove(mapName: String, removal: Any) = ArgumentTypes::class.java.getDeclaredField(mapName).apply {
            isAccessible = true
            (get(ArgumentTypes) as? MutableMap<*, *>)?.remove(removal)
        }

        private fun ArgumentSerializer<*>.checkFlags() {
            assertEquals(0b00000011, createFlags(minimum = true, maximum = true))
            assertEquals(0b00000000, createFlags(minimum = false, maximum = false))
            assertEquals(0b00000010, createFlags(minimum = false, maximum = true))
            assertEquals(0b00000001, createFlags(minimum = true, maximum = false))
        }
    }
}

private class BogusArgumentType : ArgumentType<Nothing?> {

    override fun parse(reader: StringReader?): Nothing? = null
}

private fun throwCause(function: () -> Unit) = try {
    function()
} catch (exception: Exception) {
    throw exception.cause ?: exception
}
