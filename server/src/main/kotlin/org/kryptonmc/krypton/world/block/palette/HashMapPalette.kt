package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import org.kryptonmc.krypton.extension.varIntSize
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.registry.Registry
import org.kryptonmc.krypton.utils.GenericArray
import org.kryptonmc.krypton.utils.IntIdentityHashBiMap
import org.kryptonmc.krypton.world.block.palette.resize.PaletteResizer
import kotlin.math.max

class HashMapPalette<T>(
    private val registry: Registry<T>,
    private val bits: Int,
    private val resizeHandler: PaletteResizer<T>,
    private val reader: (CompoundBinaryTag) -> T,
    private val writer: (T) -> CompoundBinaryTag
) : Palette<T>, Registry<T> by registry {

    private val values = IntIdentityHashBiMap<T>(1 shl bits)

    override fun idOf(value: T): Int {
        var id = values.idOf(value)
        if (id == -1) {
            id = values + value
            if (id >= 1 shl bits) id = resizeHandler(bits + 1, value)
        }
        return id
    }

    override fun contains(predicate: (T?) -> Boolean): Boolean {
        for (i in 0 until size) {
            if (!predicate(values[i])) continue
            return true
        }
        return false
    }

    override fun get(id: Int) = values[id]

    override fun write(buf: ByteBuf) {
        val size = values.size
        buf.writeVarInt(size)
        for (i in 0 until size) buf.writeVarInt(registry.idOf(values[i]!!))
    }

    override fun write(tag: ListBinaryTag) {
        for (i in 0 until values.size) tag.add(writer(values[i]!!))
    }

    override fun read(tag: ListBinaryTag) {
        values.clear()
        for (i in 0 until tag.size()) values + reader(tag.getCompound(i))
    }

    val size = values.size

    override val serializedSize: Int
        get() {
            var varIntSize = size.varIntSize()
            for (i in 0 until size) varIntSize += registry.idOf(values[i]!!)
            return varIntSize
        }
}