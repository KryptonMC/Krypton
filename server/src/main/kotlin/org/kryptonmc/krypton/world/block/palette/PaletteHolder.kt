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
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.krypton.util.ceillog2
import org.kryptonmc.krypton.util.varIntBytes
import org.kryptonmc.krypton.util.writeLongArray
import org.kryptonmc.krypton.world.block.BLOCKS
import org.kryptonmc.krypton.world.data.BitStorage
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import kotlin.math.max

class PaletteHolder(private var palette: Palette) : (Int, Block) -> Int {

    private lateinit var storage: BitStorage
    private var bits = 0
        set(value) {
            if (field == value) return
            field = value
            palette = when {
                field <= MINIMUM_PALETTE_SIZE -> {
                    field = MINIMUM_PALETTE_SIZE
                    ArrayPalette(field, this)
                }
                field < GLOBAL_PALETTE_SIZE -> MapPalette(field, this)
                else -> {
                    field = BLOCKS.size.ceillog2()
                    GlobalPalette
                }
            }
            storage = BitStorage(field, SIZE)
        }

    init {
        bits = MINIMUM_PALETTE_SIZE
    }

    @Synchronized
    fun getAndSet(x: Int, y: Int, z: Int, value: Block): Block {
        val id = palette[value]
        val newId = storage.getAndSet(indexOf(x, y, z), id)
        return palette[newId] ?: DEFAULT
    }

    operator fun get(x: Int, y: Int, z: Int) = palette[storage[indexOf(x, y, z)]] ?: DEFAULT

    @Synchronized
    operator fun set(x: Int, y: Int, z: Int, value: Block) = set(indexOf(x, y, z), value)

    @Synchronized
    fun write(buf: ByteBuf) {
        buf.writeByte(bits)
        palette.write(buf)
        buf.writeLongArray(storage.data)
    }

    @Synchronized
    fun load(data: ListTag, states: LongArray) {
        val bits = max(MINIMUM_PALETTE_SIZE, data.size.ceillog2())
        if (bits != this.bits) this.bits = bits
        palette.load(data)
        val storageBits = states.size * Long.SIZE_BITS / SIZE
        when {
            palette === GlobalPalette -> {
                val newPalette = MapPalette(bits, DUMMY_RESIZER)
                newPalette.load(data)
                val bitStorage = BitStorage(bits, SIZE, states)
                for (i in 0 until SIZE) storage[i] = GlobalPalette[newPalette[bitStorage[i]]!!]
            }
            storageBits == this.bits -> System.arraycopy(states, 0, storage.data, 0, states.size)
            else -> {
                val bitStorage = BitStorage(storageBits, SIZE, states)
                for (i in 0 until SIZE) storage[i] = bitStorage[i]
            }
        }
    }

    @Synchronized
    fun save(builder: CompoundTag.Builder) {
        val newPalette = MapPalette(bits, DUMMY_RESIZER)
        var default = DEFAULT
        var defaultId = newPalette[DEFAULT]
        val states = IntArray(SIZE)

        for (i in 0 until SIZE) {
            val value = get(i)
            if (value !== default) {
                default = value
                defaultId = newPalette[value]
            }
            states[i] = defaultId
        }

        val paletteData = newPalette.save()
        builder.put("Palette", paletteData)
        val bits = max(MINIMUM_PALETTE_SIZE, paletteData.size.ceillog2())
        val storage = BitStorage(bits, SIZE)
        for (i in states.indices) storage[i] = states[i]
        builder.longArray("BlockStates", storage.data)
    }

    fun count(consumer: (Block, Int) -> Unit) {
        val map = Int2IntOpenHashMap()
        storage.count { map[it] = map[it] + 1 }
        map.int2IntEntrySet().fastForEach { consumer(palette[it.intKey]!!, it.intValue) }
    }

    private fun get(index: Int) = palette[storage[index]] ?: DEFAULT

    private fun set(index: Int, value: Block) = storage.set(index, palette[value])

    override fun invoke(newBits: Int, value: Block): Int {
        val oldStorage = storage
        val oldPalette = palette
        bits = newBits
        for (i in 0 until oldStorage.size) oldPalette[oldStorage[i]]?.let { set(i, it) }
        return palette[value]
    }

    val serializedSize: Int
        get() = 1 + palette.serializedSize + storage.size.varIntBytes + storage.data.size * Long.SIZE_BYTES

    companion object {

        const val GLOBAL_PALETTE_SIZE = 9
        const val MINIMUM_PALETTE_SIZE = 4
        private const val SIZE = 4096
        private val DEFAULT = Blocks.AIR
        private val DUMMY_RESIZER: (Int, Block) -> Int = { _, _ -> 0 }
    }
}

private fun indexOf(x: Int, y: Int, z: Int) = y shl 8 or (z shl 4) or x
