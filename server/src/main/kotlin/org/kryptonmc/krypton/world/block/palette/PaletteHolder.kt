/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.BitStorage
import org.kryptonmc.krypton.util.SimpleBitStorage
import org.kryptonmc.krypton.util.ZeroBitStorage
import org.kryptonmc.krypton.util.ceillog2
import org.kryptonmc.krypton.util.varIntBytes
import org.kryptonmc.krypton.util.writeLongArray
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag

class PaletteHolder<T>(
    private var palette: Palette<T>,
    private val type: PaletteType<T>
) : PaletteResizer<T> {

    private lateinit var storage: BitStorage
    private var bits = 0
        set(value) {
            if (field == value) return
            field = value
            palette = when {
                field == 0 -> type.singlePalette(this)
                type.arrayRange != null && field in type.arrayRange -> type.arrayPalette(this, field)
                type.mapRange != null && field in type.mapRange -> type.mapPalette(this, field)
                else -> type.globalPalette
            }
            storage = if (field == 0) ZeroBitStorage(1 shl 0) else SimpleBitStorage(field, 1 shl (field * 3))
        }
    val size: Int
        get() = 1 shl (bits * 3)
    val serializedSize: Int
        get() = 1 + palette.serializedSize + storage.size.varIntBytes + storage.data.size * Long.SIZE_BYTES

    @Synchronized
    fun getAndSet(x: Int, y: Int, z: Int, value: T): T {
        val id = palette[value]
        val newId = storage.getAndSet(indexOf(x, y, z), id)
        return palette[newId] ?: type.defaultValue
    }

    operator fun get(x: Int, y: Int, z: Int): T = palette[storage[indexOf(x, y, z)]] ?: type.defaultValue

    operator fun get(index: Int): T = palette[storage[index]] ?: type.defaultValue

    @Synchronized
    operator fun set(x: Int, y: Int, z: Int, value: T) = set(indexOf(x, y, z), value)

    @Synchronized
    fun write(buf: ByteBuf) {
        buf.writeByte(bits)
        palette.write(buf)
        buf.writeLongArray(storage.data)
    }

    @Synchronized
    fun load(data: CompoundTag, paletteType: Int) {
        val size = size
        val bits = calculateSerializationBits(size)
        if (bits != this.bits) this.bits = bits
        palette.load(data.getList("palette", paletteType))
        if (bits == 0) {
            storage = ZeroBitStorage(size)
            return
        }
        val states = data.getLongArray("data")
        val storageBits = states.size * Long.SIZE_BITS / size
        when {
            palette === type.globalPalette -> {
                val newPalette = type.mapPalette(dummyResizer(), bits).apply { load(data.getList("palette", paletteType)) }
                val bitStorage = SimpleBitStorage(bits, size, states)
                for (i in 0 until size) storage[i] = type.globalPalette[newPalette[bitStorage[i]]!!]
            }
            storageBits == this.bits -> System.arraycopy(states, 0, storage.data, 0, states.size)
            else -> {
                val bitStorage = SimpleBitStorage(storageBits, size, states)
                for (i in 0 until size) storage[i] = bitStorage[i]
            }
        }
    }

    @Synchronized
    @Suppress("UNCHECKED_CAST")
    fun save(builder: CompoundTag.Builder) {
        val size = size
        val newPalette = type.mapPalette(dummyResizer(), bits)
        var default = type.defaultValue
        var defaultId = newPalette[type.defaultValue]
        val states = IntArray(size)

        for (i in 0 until size) {
            val value = get(i)
            if (value !== default) {
                default = value
                defaultId = newPalette[value]
            }
            states[i] = defaultId
        }

        val paletteData = newPalette.save()
        builder.put("palette", paletteData)
        val bits = calculateSerializationBits(size)
        val storage = SimpleBitStorage(bits, size)
        for (i in states.indices) storage[i] = states[i]
        builder.longArray("data", storage.data)
    }

    fun forEachLocation(consumer: (T, Int) -> Unit) = storage.forEach { location, data ->
        consumer(palette[data]!!, location)
    }

    override fun invoke(newBits: Int, value: T): Int {
        val oldStorage = storage
        val oldPalette = palette
        bits = newBits
        for (i in 0 until oldStorage.size) oldPalette[oldStorage[i]]?.let { set(i, it) }
        return palette[value]
    }

    private fun set(index: Int, value: T) = storage.set(index, palette[value])

    private fun indexOf(x: Int, y: Int, z: Int): Int = (((y shl bits) or z) shl bits) or x

    private fun calculateSerializationBits(size: Int): Int {
        if (palette === type.globalPalette) return size.ceillog2()
        return bits
    }

    companion object {

        private val DUMMY_RESIZER: PaletteResizer<Any?> = PaletteResizer { _, _ -> 0 }

        @Suppress("UNCHECKED_CAST")
        private fun <T> dummyResizer(): PaletteResizer<T> = DUMMY_RESIZER as PaletteResizer<T>
    }
}
