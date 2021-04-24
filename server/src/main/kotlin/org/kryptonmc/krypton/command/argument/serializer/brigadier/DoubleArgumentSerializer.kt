package org.kryptonmc.krypton.command.argument.serializer.brigadier

import com.mojang.brigadier.arguments.DoubleArgumentType
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.command.argument.serializer.ArgumentSerializer

/**
 * A serialiser for Brigadier's [DoubleArgumentType]
 */
class DoubleArgumentSerializer : ArgumentSerializer<DoubleArgumentType> {

    override fun write(argument: DoubleArgumentType, buf: ByteBuf) {
        val minimum = argument.minimum != -Double.MAX_VALUE
        val maximum = argument.maximum != Double.MAX_VALUE
        buf.writeByte(createFlags(minimum, maximum))
        if (minimum) buf.writeDouble(argument.minimum)
        if (maximum) buf.writeDouble(argument.maximum)
    }
}
