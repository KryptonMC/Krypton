package org.kryptonmc.krypton.command.argument.serializer.brigadier

import com.mojang.brigadier.arguments.IntegerArgumentType
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.command.argument.serializer.ArgumentSerializer

/**
 * A serialiser for Brigadier's [IntegerArgumentType]
 *
 * @author Callum Seabrook
 */
class IntegerArgumentSerializer : ArgumentSerializer<IntegerArgumentType> {

    override fun write(argument: IntegerArgumentType, buf: ByteBuf) {
        val minimum = argument.minimum != Int.MIN_VALUE
        val maximum = argument.maximum != Int.MAX_VALUE
        buf.writeByte(createFlags(minimum, maximum))
        if (minimum) buf.writeInt(argument.minimum)
        if (maximum) buf.writeInt(argument.maximum)
    }
}