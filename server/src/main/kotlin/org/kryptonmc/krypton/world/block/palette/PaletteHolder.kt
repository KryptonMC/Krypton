/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.util.BitStorage
import org.kryptonmc.krypton.util.IntBiMap
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.SimpleBitStorage
import org.kryptonmc.krypton.util.ZeroBitStorage
import org.kryptonmc.krypton.util.varIntBytes
import org.kryptonmc.krypton.util.writeLongArray
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.toBlockState
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.compound
import org.kryptonmc.nbt.list
import java.util.function.IntUnaryOperator

class PaletteHolder<T> : PaletteResizer<T> {

    private val strategy: Strategy<T>
    @Volatile
    private var data: Data<T>

    private constructor(strategy: Strategy<T>, data: Data<T>) {
        this.strategy = strategy
        this.data = data
    }

    constructor(strategy: Strategy<T>, configuration: Configuration<T>, storage: BitStorage, entries: List<T>) {
        this.strategy = strategy
        data = Data(configuration, storage, configuration.factory.create(configuration.bits, strategy.registry, this, entries))
    }

    constructor(strategy: Strategy<T>, value: T) {
        this.strategy = strategy
        data = createOrReuseData(null, 0)
        data.palette.get(value) // Preload
    }

    @Synchronized
    fun getAndSet(x: Int, y: Int, z: Int, value: T): T {
        val id = data.palette.get(value)
        val newId = data.storage.getAndSet(strategy.indexOf(x, y, z), id)
        return data.palette.get(newId)
    }

    operator fun get(x: Int, y: Int, z: Int): T = data.palette.get(data.storage.get(strategy.indexOf(x, y, z)))

    operator fun get(index: Int): T = data.palette.get(data.storage.get(index))

    @Synchronized
    operator fun set(x: Int, y: Int, z: Int, value: T) {
        data.storage.set(strategy.indexOf(x, y, z), data.palette.get(value))
    }

    @Synchronized
    fun write(buf: ByteBuf) {
        data.write(buf)
    }

    @Synchronized
    fun write(encoder: ValueEncoder<T>): CompoundTag {
        val palette = MapPalette(strategy.registry, data.storage.bits, dummyResizer())
        val size = strategy.calculateSize()
        val ids = IntArray(size)
        data.storage.unpack(ids)
        swapPalette(ids) { palette.get(data.palette.get(it)) }
        val bits = strategy.calculateSerializationBits(palette.size)
        val data = if (bits != 0) SimpleBitStorage(bits, size, ids).data else LongArray(0)
        return compound {
            list(PALETTE_TAG) { palette.entries.forEach { add(encoder.encode(it)) } }
            putLongArray(DATA_TAG, data)
        }
    }

    fun calculateSerializedSize(): Int = data.calculateSerializedSize()

    fun forEachLocation(consumer: PaletteConsumer<T>) {
        data.storage.forEach { location, data -> consumer(this.data.palette.get(data)!!, location) }
    }

    override fun onResize(newBits: Int, value: T): Int {
        val old = data
        val new = createOrReuseData(old, newBits)
        new.copyFrom(data.palette, data.storage)
        data = new
        return new.palette.get(value)
    }

    private fun createOrReuseData(old: Data<T>?, bits: Int): Data<T> {
        val config = strategy.configuration(bits)
        if (old != null && config == old.configuration) return old
        return config.createData(strategy.registry, this, strategy.calculateSize())
    }

    fun copy(): PaletteHolder<T> = PaletteHolder(strategy, data.copy())

    fun interface PaletteConsumer<T> {

        operator fun invoke(element: T, location: Int)
    }

    @JvmRecord
    data class Configuration<T>(val factory: Palette.Factory, val bits: Int) {

        fun createData(registry: IntBiMap<T>, resizer: PaletteResizer<T>, size: Int): Data<T> {
            val storage = if (bits == 0) ZeroBitStorage(size) else SimpleBitStorage(bits, size)
            val palette = factory.create(bits, registry, resizer, emptyList())
            return Data(this, storage, palette)
        }
    }

    @JvmRecord
    data class Data<T>(val configuration: Configuration<T>, val storage: BitStorage, val palette: Palette<T>) {

        fun copyFrom(oldPalette: Palette<T>, oldStorage: BitStorage) {
            for (i in 0 until oldStorage.size) {
                val value = oldPalette.get(oldStorage.get(i))!!
                storage.set(i, palette.get(value))
            }
        }

        fun write(buf: ByteBuf) {
            buf.writeByte(storage.bits)
            palette.write(buf)
            buf.writeLongArray(storage.data)
        }

        fun calculateSerializedSize(): Int = 1 + palette.calculateSerializedSize() + storage.size.varIntBytes() + storage.sizeBytes()

        fun copy(): Data<T> = Data(configuration, storage.copy(), palette.copy())
    }

    abstract class Strategy<T>(val registry: IntBiMap<T>, private val sizeBits: Int) {

        private val cache = Int2ObjectOpenHashMap<Configuration<T>>()

        protected abstract fun createConfiguration(bits: Int): Configuration<T>

        fun configuration(bits: Int): Configuration<T> = cache.computeIfAbsent(bits, Int2ObjectFunction(::createConfiguration))

        fun indexOf(x: Int, y: Int, z: Int): Int = y shl sizeBits or z shl sizeBits or x

        fun calculateSize(): Int = 1 shl sizeBits * 3

        fun calculateSerializationBits(size: Int): Int {
            val sizeBits = Maths.ceillog2(size)
            val config = configuration(sizeBits)
            if (config.factory === GlobalPalette.Factory) return sizeBits
            return config.bits
        }

        companion object {

            @JvmField
            val BLOCKS: Strategy<KryptonBlockState> = object : Strategy<KryptonBlockState>(KryptonBlock.STATES, 4) {

                override fun createConfiguration(bits: Int): Configuration<KryptonBlockState> = when (bits) {
                    0 -> Configuration(SingleValuePalette.Factory, bits)
                    in 1..4 -> Configuration(ArrayPalette.Factory, 4)
                    in 5..8 -> Configuration(MapPalette.Factory, bits)
                    else -> Configuration(GlobalPalette.Factory, Maths.ceillog2(registry.size))
                }
            }
            @JvmField
            val BIOMES: Strategy<Biome> = object : Strategy<Biome>(Registries.BIOME as KryptonRegistry<Biome>, 2) {

                override fun createConfiguration(bits: Int): Configuration<Biome> = when (bits) {
                    0 -> Configuration(SingleValuePalette.Factory, bits)
                    in 1..3 -> Configuration(ArrayPalette.Factory, bits)
                    else -> Configuration(GlobalPalette.Factory, Maths.ceillog2(registry.size))
                }
            }
        }
    }

    fun interface ValueEncoder<T> {

        fun encode(value: T): Tag
    }

    companion object {

        private const val PALETTE_TAG = "palette"
        private const val DATA_TAG = "data"
        private val DUMMY_RESIZER: PaletteResizer<Any?> = PaletteResizer { _, _ -> 0 }

        @JvmStatic
        fun readBlocks(data: CompoundTag): PaletteHolder<KryptonBlockState> {
            val entries = ArrayList<KryptonBlockState>()
            data.getList(PALETTE_TAG, CompoundTag.ID).forEachCompound { entries.add(it.toBlockState()) }
            return read(Strategy.BLOCKS, entries, data.getLongArray(DATA_TAG))
        }

        @JvmStatic
        fun readBiomes(data: CompoundTag): PaletteHolder<Biome> {
            val entries = ArrayList<Biome>()
            data.getList(PALETTE_TAG, StringTag.ID).forEachString {
                entries.add(checkNotNull(Registries.BIOME.get(Key.key(it))) { "Invalid palette data! Failed to find biome with key $it!" })
            }
            return read(Strategy.BIOMES, entries, data.getLongArray(DATA_TAG))
        }

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        private fun <T> dummyResizer(): PaletteResizer<T> = DUMMY_RESIZER as PaletteResizer<T>

        @JvmStatic
        private fun <T> read(strategy: Strategy<T>, entries: List<T>, values: LongArray): PaletteHolder<T> {
            val size = strategy.calculateSize()
            val bits = strategy.calculateSerializationBits(entries.size)
            val config = strategy.configuration(bits)
            if (bits == 0) return PaletteHolder(strategy, config, ZeroBitStorage(size), entries)
            check(values.isNotEmpty()) { "Expected to have values for non-zero storage, but found none!" }

            if (config.factory === GlobalPalette.Factory) {
                val palette = MapPalette(strategy.registry, bits, dummyResizer(), entries)
                val storage = SimpleBitStorage(bits, size, values)
                val ids = IntArray(size)
                storage.unpack(ids)
                swapPalette(ids) { strategy.registry.idOf(palette.get(it)) }
                return PaletteHolder(strategy, config, SimpleBitStorage(config.bits, size, ids), entries)
            }
            return PaletteHolder(strategy, config, SimpleBitStorage(config.bits, size, values), entries)
        }

        @JvmStatic
        private fun swapPalette(ids: IntArray, operator: IntUnaryOperator) {
            var lastId = -1
            var transformedId = -1

            for (i in ids.indices) {
                val id = ids[i]
                if (id != lastId) {
                    lastId = id
                    transformedId = operator.applyAsInt(id)
                }
                ids[i] = transformedId
            }
        }
    }
}
