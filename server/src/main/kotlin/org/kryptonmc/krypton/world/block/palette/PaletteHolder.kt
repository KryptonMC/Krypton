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
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.nbt.CompoundTag

class PaletteHolder<T>(
    private val type: PaletteType<T>,
    private var palette: Palette<T> = type.globalPalette
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
            storage = if (field == 0) ZeroBitStorage(type.size) else SimpleBitStorage(field, type.size)
        }
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
        val paletteData = data.getList("palette", paletteType)
        val size = type.size
        val bits = calculateSerializationBits(paletteData.size)
        if (bits != this.bits) this.bits = bits
        palette.load(paletteData)
        if (bits == 0) {
            storage = ZeroBitStorage(size)
            return
        }
        val states = data.getLongArray("data")
        if (palette === type.globalPalette) {
            val newPalette = type.mapPalette(dummyResizer(), bits).apply { load(data.getList("palette", paletteType)) }
            val bitStorage = SimpleBitStorage(bits, size, states)
            val ids = IntArray(bitStorage.size) { type.registry.idOf(newPalette[bitStorage[it]]!!) }
            storage = SimpleBitStorage(bits, size).apply {
                var index = -1
                ids.forEach { set(index++, it) }
            }
            return
        }
        storage = SimpleBitStorage(bits, size, states)
    }

    @Synchronized
    @Suppress("UNCHECKED_CAST")
    fun save(builder: CompoundTag.Builder) {
        val newPalette = type.mapPalette(dummyResizer(), bits)
        var default = type.defaultValue
        var defaultId = newPalette[type.defaultValue]
        val states = IntArray(type.size)

        for (i in 0 until type.size) {
            val value = get(i)
            if (value !== default) {
                default = value
                defaultId = newPalette[value]
            }
            states[i] = defaultId
        }

        val paletteData = newPalette.save()
        builder.put("palette", paletteData)
        val bits = calculateSerializationBits(paletteData.size)
        val data = if (bits == 0) {
            LongArray(0)
        } else {
            val storage = SimpleBitStorage(bits, type.size)
            for (i in states.indices) {
                storage[i] = states[i]
            }
            storage.data
        }
        builder.longArray("data", data)
    }

    fun forEachLocation(consumer: PaletteConsumer<T>) = storage.forEach { location, data ->
        if (palette[data] == null) return@forEach
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

    fun interface PaletteConsumer<T> {

        operator fun invoke(element: T, location: Int)
    }

    companion object {

        private val DUMMY_RESIZER: PaletteResizer<Any?> = PaletteResizer { _, _ -> 0 }

        @Suppress("UNCHECKED_CAST")
        private fun <T> dummyResizer(): PaletteResizer<T> = DUMMY_RESIZER as PaletteResizer<T>
    }
}
