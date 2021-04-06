package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.ListBinaryTag
import org.kryptonmc.krypton.registry.Registry

interface Palette<T> : Registry<T> {

    val serializedSize: Int

    override operator fun get(id: Int): T?

    operator fun contains(predicate: (T?) -> Boolean): Boolean

    fun write(buf: ByteBuf)

    fun write(tag: ListBinaryTag)

    fun read(tag: ListBinaryTag)
}