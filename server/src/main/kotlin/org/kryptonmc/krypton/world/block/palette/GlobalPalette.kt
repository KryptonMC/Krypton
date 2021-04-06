package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.kyori.adventure.nbt.ListBinaryTag
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.extension.varIntSize
import org.kryptonmc.krypton.registry.Registry
import java.io.IOException

class GlobalPalette<T>(
    private val registry: Registry<T>,
    private val default: T
) : Palette<T>, Registry<T> by registry {

    override val serializedSize = 0.varIntSize()

    override fun contains(predicate: (T?) -> Boolean) = true

    override fun get(id: Int) = registry[id] ?: default

    override fun write(buf: ByteBuf) = Unit
    override fun write(tag: ListBinaryTag) = Unit
    override fun read(tag: ListBinaryTag) = Unit
}