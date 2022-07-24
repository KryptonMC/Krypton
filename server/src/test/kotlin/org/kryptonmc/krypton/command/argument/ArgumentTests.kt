package org.kryptonmc.krypton.command.argument

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import io.netty.buffer.Unpooled
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.kryptonmc.krypton.api.InitializerExtension
import org.kryptonmc.krypton.api.Initializers
import org.kryptonmc.krypton.command.argument.serializer.DoubleArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.FlaggedArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.FloatArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.IntegerArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.LongArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.StringArgumentSerializer
import org.kryptonmc.krypton.api.ArgumentSerializerInitializer
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeArgumentType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@ExtendWith(InitializerExtension::class)
@Initializers(ArgumentSerializerInitializer::class)
class ArgumentTests {

    @Test
    fun `argument types write calls error when entry is null`() {
        val buffer = Unpooled.buffer()
        assertThrows<IllegalStateException> { buffer.writeArgumentType(BogusArgumentType) }
        assertFalse(buffer.isReadable)

    }

    @Test
    fun `valid argument type writing for empty serializer`() {
        val buffer = Unpooled.buffer().apply { writeArgumentType(BoolArgumentType.bool()) }
        assertEquals(0, buffer.readVarInt())
        assertFalse(buffer.isReadable)
    }

    @Test
    fun `valid argument type writing for integer serializer`() {
        val buffer = Unpooled.buffer().apply { writeArgumentType(IntegerArgumentType.integer(10, 20)) }
        assertEquals(3, buffer.readVarInt())
        assertEquals(0b00000011, buffer.readByte())
        assertEquals(10, buffer.readInt())
        assertEquals(20, buffer.readInt())
    }

    @Test
    fun `serializer flag creating`() {
        DoubleArgumentSerializer.checkFlags()
        FloatArgumentSerializer.checkFlags()
        IntegerArgumentSerializer.checkFlags()
        LongArgumentSerializer.checkFlags()
    }

    @Test
    fun `number argument serializers min and max`() {
        val buf = Unpooled.buffer()

        DoubleArgumentSerializer.write(buf, DoubleArgumentType.doubleArg(10.0, 20.0))
        buf.readByte()
        assertEquals(10.0, buf.readDouble())
        assertEquals(20.0, buf.readDouble())

        FloatArgumentSerializer.write(buf, FloatArgumentType.floatArg(10F, 20F))
        buf.readByte()
        assertEquals(10F, buf.readFloat())
        assertEquals(20F, buf.readFloat())

        IntegerArgumentSerializer.write(buf, IntegerArgumentType.integer(10, 20))
        buf.readByte()
        assertEquals(10, buf.readInt())
        assertEquals(20, buf.readInt())

        LongArgumentSerializer.write(buf, LongArgumentType.longArg(10L, 20L))
        buf.readByte()
        assertEquals(10L, buf.readLong())
        assertEquals(20L, buf.readLong())
    }

    @Test
    fun `string argument serializer`() {
        val buf = Unpooled.buffer()
        StringArgumentSerializer.write(buf, StringArgumentType.greedyString())
        assertEquals(2, buf.readVarInt())
    }

    private object BogusArgumentType : ArgumentType<Nothing?> {

        override fun parse(reader: StringReader?): Nothing? = null
    }
}

private fun FlaggedArgumentSerializer<*>.checkFlags() {
    assertEquals(0b00000011, createFlags(minimum = true, maximum = true))
    assertEquals(0b00000000, createFlags(minimum = false, maximum = false))
    assertEquals(0b00000010, createFlags(minimum = false, maximum = true))
    assertEquals(0b00000001, createFlags(minimum = true, maximum = false))
}
