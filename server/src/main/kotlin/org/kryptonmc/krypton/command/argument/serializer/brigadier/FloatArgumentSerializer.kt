package org.kryptonmc.krypton.command.argument.serializer.brigadier

import com.mojang.brigadier.arguments.FloatArgumentType
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.command.argument.serializer.ArgumentSerializer

/**
 * A serialiser for Brigadier's [FloatArgumentType]
 */
class FloatArgumentSerializer : ArgumentSerializer<FloatArgumentType> {

    override fun write(argument: FloatArgumentType, buf: ByteBuf) {
        val minimum = argument.minimum != -Float.MAX_VALUE
        val maximum = argument.maximum != Float.MAX_VALUE
        buf.writeByte(createFlags(minimum, maximum))
        if (minimum) buf.writeFloat(argument.minimum)
        if (maximum) buf.writeFloat(argument.maximum)
    }
}
