package org.kryptonmc.krypton.world.data

import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import org.kryptonmc.krypton.extension.varIntSize
import org.kryptonmc.krypton.extension.writeLongArray
import org.kryptonmc.krypton.registry.impl.MappedRegistry
import org.kryptonmc.krypton.world.block.palette.HashMapPalette
import org.kryptonmc.krypton.world.block.palette.Palette
import org.kryptonmc.krypton.world.block.palette.LinearPalette
import org.kryptonmc.krypton.world.block.palette.resize.PaletteResizer
import java.util.concurrent.locks.ReentrantLock
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.max

class PalettedContainer<T>(
    private val globalPalette: Palette<T>,
    private val registry: MappedRegistry<T>,
    private val default: T,
    private val reader: (CompoundBinaryTag) -> T,
    private val writer: (T) -> CompoundBinaryTag
) : PaletteResizer<T> {

    private val dummyPaletteResizer = PaletteResizer<T> { _, _ -> 0 }
    private val lock = ReentrantLock()

    private var palette: Palette<T> = LinearPalette(registry, 4, this, reader)
    private lateinit var storage: StateIndexHolder

    private var bitsPerBlock = 4
        set(value) {
            if (field == value) return
            field = value
            palette = when {
                field <= 4 -> {
                    field = 4
                    LinearPalette(registry, field, this, reader)
                }
                field < 9 -> HashMapPalette(registry, field, this, reader, writer)
                else -> {
                    field = ceil(log2(registry.size.toDouble())).toInt()
                    globalPalette
                }
            }
            palette.idOf(default)
            storage = StateIndexHolder(field, 4096)
        }

    init {
        bitsPerBlock = 4
    }

    operator fun contains(predicate: (T?) -> Boolean) = palette.contains(predicate)

    fun getAndSet(x: Int, y: Int, z: Int, value: T): T {
        acquire()
        val result = getAndSet(indexOf(x, y, z), value)
        release()
        return result
    }

    fun getAndSetUnchecked(x: Int, y: Int, z: Int, value: T): T = getAndSet(indexOf(x, y, z), value)

    private fun getAndSet(index: Int, value: T): T {
        val id = palette.idOf(value)
        val old = storage.getAndSet(index, id)
        return palette[old] ?: default
    }

    operator fun get(x: Int, y: Int, z: Int) = get(indexOf(x, y, z))

    private fun get(index: Int) = palette[storage[index]] ?: default

    private fun set(index: Int, value: T) {
        val id = palette.idOf(value)
        storage[index] = id
    }

    fun read(tag: ListBinaryTag, states: LongArray) {
        acquire()
        val bitsPerBlock = max(4, (ceil(log2(tag.size().toFloat()))).toInt())
        if (bitsPerBlock != this.bitsPerBlock) this.bitsPerBlock = bitsPerBlock
        palette.read(tag)
        val bits = states.size * 64 / 4096
        when {
            palette == globalPalette -> {
                val hashPalette = HashMapPalette(registry, bitsPerBlock, dummyPaletteResizer, reader, writer)
                hashPalette.read(tag)
                val storage = StateIndexHolder(bitsPerBlock, 4096, states)
                for (i in 0 until 4096) this.storage[i] = globalPalette.idOf(hashPalette[storage[i]]!!)
            }
            bits == this.bitsPerBlock -> System.arraycopy(states, 0, this.storage.data, 0, states.size)
            else -> {
                val storage = StateIndexHolder(bits, 4096, states)
                for (i in 0 until 4096) this.storage[i] = storage[i]
            }
        }
    }

    fun write(buf: ByteBuf) {
        acquire()
        buf.writeByte(bitsPerBlock)
        palette.write(buf)
        buf.writeLongArray(storage.data)
        release()
    }

    fun write(): CompoundBinaryTag {
        acquire()
        val result = CompoundBinaryTag.empty()
        val palette = HashMapPalette(registry, bitsPerBlock, dummyPaletteResizer, reader, writer)
        var default = this.default
        var defaultId = palette.idOf(default)
        val states = IntArray(4096)
        for (i in 0 until 4096) {
            val value = get(i)
            if (value != default) {
                default = value
                defaultId = palette.idOf(value)
            }
            states[i] = defaultId
        }
        val tag = ListBinaryTag.empty()
        palette.write(tag)
        result.put("Palette", tag)
        val bitsPerBlock = max(4, ceil(log2(tag.size().toDouble())).toInt())
        val storage = StateIndexHolder(bitsPerBlock, 4096)
        for (i in states.indices) storage[i] = states[i]
        result.putLongArray("BlockStates", storage.data)
        release()
        return result
    }

    val serializedSize: Int
        get() = 1 + palette.serializedSize + storage.size.varIntSize() + storage.data.size * 8

    fun count(consumer: (T?, Int) -> Unit) {
        val values = Int2IntOpenHashMap()
        storage.getAll { values[it] += 1 }
        values.int2IntEntrySet().forEach { consumer(palette[it.intKey], it.intValue) }
    }

    private fun acquire() {
        if (lock.isLocked && !lock.isHeldByCurrentThread) {
            // TODO: Crash
            return
        }
        lock.lock()
    }

    // consistency
    private fun release() = lock.unlock()

    // invoke to resize the palette
    override fun invoke(bitsPerBlock: Int, default: T): Int {
        acquire()
        this.bitsPerBlock = bitsPerBlock
        for (i in 0 until storage.size) {
            val value = palette[storage[i]] ?: continue
            set(i, value)
        }
        val defaultId = palette.idOf(default)
        release()
        return defaultId
    }

    companion object {

        private fun indexOf(x: Int, y: Int, z: Int) = (y shr 8) or (z shr 4) or x
    }
}