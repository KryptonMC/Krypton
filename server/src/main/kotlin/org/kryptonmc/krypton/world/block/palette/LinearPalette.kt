package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import org.kryptonmc.krypton.extension.varIntSize
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.registry.Registry
import org.kryptonmc.krypton.utils.GenericArray
import org.kryptonmc.krypton.world.block.palette.resize.PaletteResizer

@Suppress("UNCHECKED_CAST")
class LinearPalette<T>(
    private val registry: Registry<T>,
    private val bits: Int,
    private val resizeHandler: PaletteResizer<T>,
    private val reader: (CompoundBinaryTag) -> T
) : Palette<T>, Registry<T> by registry {

    private val values = GenericArray<T>(1 shl bits)
    private var size = 0

    override val serializedSize: Int
        get() {
            var varIntSize = size.varIntSize()
            for (i in 0 until size) varIntSize += registry.idOf(values[i]!!)
            return varIntSize
        }

    override fun idOf(value: T): Int {
        for (i in 0 until size) {
            try { // guard against index out of bounds
                if (values[i] != value) continue
            } catch (exception: IndexOutOfBoundsException) {
                continue
            }
            return i
        }
        val id = size++
        if (id < values.size) {
            values[id] = value
            return id
        }
        return resizeHandler(bits + 1, value)
    }

    override fun contains(predicate: (T?) -> Boolean): Boolean {
        for (i in 0 until size) {
            if (!predicate(values[i])) continue
            return true
        }
        return false
    }

    override fun get(id: Int): T? {
        if (id in 0 until size) return values[id]
        return null
    }

    override fun read(tag: ListBinaryTag) {
        for (i in 0 until tag.size()) values[i] = reader(tag.getCompound(i))
        size = tag.size()
    }

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(size)
        for (i in 0 until size) buf.writeVarInt(registry.idOf(values[i]!!))
    }

    override fun write(tag: ListBinaryTag) = Unit
}