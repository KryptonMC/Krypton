package org.kryptonmc.krypton.command.argument.serializer

import com.mojang.brigadier.arguments.ArgumentType
import io.netty.buffer.ByteBuf

class EmptyArgumentSerializer<T : ArgumentType<*>> : ArgumentSerializer<T> {

    override fun write(argument: T, buf: ByteBuf) {}
}