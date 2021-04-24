package org.kryptonmc.krypton.command.argument.serializer.brigadier

import com.mojang.brigadier.arguments.StringArgumentType
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.command.argument.serializer.ArgumentSerializer
import org.kryptonmc.krypton.util.writeVarInt

/**
 * A serialiser for Brigadier's [StringArgumentType]
 */
class StringArgumentSerializer : ArgumentSerializer<StringArgumentType> {

    override fun write(argument: StringArgumentType, buf: ByteBuf) = buf.writeVarInt(argument.type.ordinal)
}
