package org.kryptonmc.krypton.command.argument.serializer

import com.mojang.brigadier.arguments.ArgumentType
import io.netty.buffer.ByteBuf

/**
 * An empty argument serialiser. Used when the argument type doesn't have anything to write,
 * such as a boolean
 *
 * @author Callum Seabrook
 */
class EmptyArgumentSerializer<T : ArgumentType<*>> : ArgumentSerializer<T> {

    override fun write(argument: T, buf: ByteBuf) {}
}