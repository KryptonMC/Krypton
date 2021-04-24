package org.kryptonmc.krypton.command.argument.serializer

import com.mojang.brigadier.arguments.ArgumentType
import io.netty.buffer.ByteBuf

/**
 * A serialiser for argument types of [T]
 *
 * @param T the argument type this is a serialiser for
 */
interface ArgumentSerializer<T : ArgumentType<*>> {

    /**
     * Write this argument serialiser to the given [buf]
     *
     * @param argument the argument type to write
     * @param buf the buffer to write to
     */
    fun write(argument: T, buf: ByteBuf)

    fun createFlags(minimum: Boolean, maximum: Boolean): Int {
        var byte = 0
        if (minimum) byte = byte or 1
        if (maximum) byte = byte or 2
        return byte
    }
}
