package org.kryptonmc.krypton.command.argument.serializer.brigadier

import com.mojang.brigadier.arguments.LongArgumentType
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.command.argument.serializer.ArgumentSerializer

class LongArgumentSerializer : ArgumentSerializer<LongArgumentType> {

    override fun write(argument: LongArgumentType, buf: ByteBuf) {
        val minimum = argument.minimum != Long.MIN_VALUE
        val maximum = argument.maximum != Long.MAX_VALUE
        buf.writeByte(createFlags(minimum, maximum))
        if (minimum) buf.writeLong(argument.minimum)
        if (maximum) buf.writeLong(argument.maximum)
    }
}