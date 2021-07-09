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
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.krypton.util.ceillog2
import org.kryptonmc.krypton.util.varIntSize
import org.kryptonmc.krypton.util.writeLongArray
import org.kryptonmc.krypton.world.block.BLOCKS
import org.kryptonmc.krypton.world.data.BitStorage
import java.util.concurrent.Semaphore
import kotlin.math.max

class PaletteHolder(private var palette: Palette) : (Int, Block) -> Int {

    private val lock = Semaphore(1)
    private lateinit var storage: BitStorage
    private var bits = 0
        set(value) {
            if (field == value) return
            field = value
            palette = when {
                field <= 4 -> {
                    field = 4
                    ArrayPalette(field, this)
                }
                field < 9 -> MapPalette(field, this)
                else -> {
                    field = BLOCKS.size.ceillog2()
                    GlobalPalette
                }
            }
            storage = BitStorage(field, 4096)
        }

    init {
        bits = 4
    }

    fun getAndSet(x: Int, y: Int, z: Int, value: Block) = try {
        lock.acquire()
        getAndSet(indexOf(x, y, z), value)
    } finally {
        lock.release()
    }

    operator fun get(x: Int, y: Int, z: Int) = palette[storage[indexOf(x, y, z)]] ?: DEFAULT

    operator fun set(x: Int, y: Int, z: Int, value: Block) = try {
        lock.acquire()
        set(indexOf(x, y, z), value)
    } finally {
        lock.release()
    }

    fun write(buf: ByteBuf) = try {
        lock.acquire()
        buf.writeByte(bits)
        palette.write(buf)
        buf.writeLongArray(storage.data)
    } finally {
        lock.release()
    }

    fun load(paletteData: NBTList<NBTCompound>, states: LongArray) = try {
        lock.acquire()
        val bits = max(4, paletteData.size.ceillog2())
        if (bits != this.bits) this.bits = bits
        palette.load(paletteData)
        val storageBits = states.size * 64 / 4096
        when {
            palette === GlobalPalette -> {
                val newPalette = MapPalette(bits, DUMMY_RESIZER).apply { load(paletteData) }
                val bitStorage = BitStorage(storageBits, SIZE, states)
                for (i in 0 until SIZE) storage[i] = GlobalPalette[newPalette[bitStorage[i]]!!]
            }
            storageBits == this.bits -> System.arraycopy(states, 0, storage.data, 0, states.size)
            else -> {
                val bitStorage = BitStorage(storageBits, SIZE, states)
                for (i in 0 until SIZE) storage[i] = bitStorage[i]
            }
        }
    } finally {
        lock.release()
    }

    fun save(tag: NBTCompound) = try {
        lock.acquire()
        val newPalette = MapPalette(bits, DUMMY_RESIZER)
        var default = DEFAULT
        var defaultId = newPalette[DEFAULT]
        val states = IntArray(4096)

        for (i in 0 until SIZE) {
            val value = palette[storage[i]] ?: DEFAULT
            if (value != default) {
                default = value
                defaultId = newPalette[value]
            }
            states[i] = defaultId
        }

        val paletteData = newPalette.save()
        tag[PALETTE_TAG] = paletteData
        val bits = max(4, paletteData.size.ceillog2())
        val storage = BitStorage(bits, SIZE)
        for (i in states.indices) storage[i] = states[i]
        tag.setLongArray(STATES_TAG, storage.data)
    } finally {
        lock.release()
    }

    fun count(consumer: (Block, Int) -> Unit) {
        val map = Int2IntOpenHashMap()
        storage.count { map[it] = map[it] + 1 }
        map.int2IntEntrySet().fastForEach { consumer(palette[it.intKey]!!, it.intValue) }
    }

    private fun getAndSet(index: Int, value: Block): Block {
        val id = palette[value]
        val newId = storage.getAndSet(index, id)
        return palette[newId] ?: DEFAULT
    }

    private fun set(index: Int, value: Block) = storage.set(index, palette[value])

    override fun invoke(newBits: Int, value: Block): Int {
        val storage = storage
        val palette = palette
        bits = newBits
        for (i in 0 until storage.size) palette[storage[i]]?.let { set(i, it) }
        return palette[value]
    }

    val serializedSize: Int
        get() = 1 + palette.serializedSize + storage.size.varIntSize() + storage.data.size * 8

    companion object {

        const val GLOBAL_PALETTE_SIZE = 9
        const val MINIMUM_PALETTE_SIZE = 4
        private const val SIZE = 4096
        private const val PALETTE_TAG = "Palette"
        private const val STATES_TAG = "BlockStates"
        private val DEFAULT = Blocks.AIR
        private val DUMMY_RESIZER: (Int, Block) -> Int = { _, _ -> 0 }
    }
}

private fun indexOf(x: Int, y: Int, z: Int) = y shl 8 or (z shl 4) or x
