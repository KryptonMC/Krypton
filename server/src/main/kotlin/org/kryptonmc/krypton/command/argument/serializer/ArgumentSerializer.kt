package org.kryptonmc.krypton.command.argument.serializer

import com.mojang.brigadier.arguments.ArgumentType
import io.netty.buffer.ByteBuf

interface ArgumentSerializer<T : ArgumentType<*>> {

    fun write(argument: T, buf: ByteBuf)

    fun createFlags(minimum: Boolean, maximum: Boolean): Int {
        var byte = 0
        if (minimum) byte = byte or 1
        if (maximum) byte = byte or 2
        return byte
    }
}