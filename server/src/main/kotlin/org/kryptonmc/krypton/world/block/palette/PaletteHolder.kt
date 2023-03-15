/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.block.palette

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.util.bits.BitStorage
import org.kryptonmc.krypton.util.ByteBufExtras
import org.kryptonmc.krypton.util.map.IntBiMap
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.util.bits.SimpleBitStorage
import org.kryptonmc.krypton.util.bits.ZeroBitStorage
import org.kryptonmc.krypton.world.block.BlockStateSerialization
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
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
        data.palette.getId(value) // Preload
    }

    @Synchronized
    fun getAndSet(x: Int, y: Int, z: Int, value: T): T {
        val id = data.palette.getId(value)
        val newId = data.storage.getAndSet(strategy.indexOf(x, y, z), id)
        return data.palette.get(newId)
    }

    fun get(x: Int, y: Int, z: Int): T = data.palette.get(data.storage.get(strategy.indexOf(x, y, z)))

    fun get(index: Int): T = data.palette.get(data.storage.get(index))

    @Synchronized
    fun set(x: Int, y: Int, z: Int, value: T) {
        data.storage.set(strategy.indexOf(x, y, z), data.palette.getId(value))
    }

    @Synchronized
    fun write(writer: BinaryWriter) {
        data.write(writer)
    }

    @Synchronized
    fun write(encoder: ValueEncoder<T>): CompoundTag {
        val palette = MapPalette(strategy.registry, data.storage.bits, dummyResizer())
        val size = strategy.calculateSize()
        val ids = IntArray(size)
        data.storage.unpack(ids)
        swapPalette(ids) { palette.getId(data.palette.get(it)) }
        val bits = strategy.calculateSerializationBits(palette.size())
        val data = if (bits != 0) SimpleBitStorage(bits, size, ids).data else LongArray(0)
        return compound {
            list(PALETTE_TAG) { palette.entries().forEach { add(encoder.encode(it)) } }
            putLongArray(DATA_TAG, data)
        }
    }

    fun calculateSerializedSize(): Int = data.calculateSerializedSize()

    fun forEachLocation(consumer: PaletteConsumer<T>) {
        data.storage.forEach { location, data -> consumer.apply(this.data.palette.get(data)!!, location) }
    }

    override fun onResize(newBits: Int, value: T): Int {
        val old = data
        val new = createOrReuseData(old, newBits)
        new.copyFrom(data.palette, data.storage)
        data = new
        return new.palette.getId(value)
    }

    private fun createOrReuseData(old: Data<T>?, bits: Int): Data<T> {
        val config = strategy.configuration(bits)
        if (old != null && config == old.configuration) return old
        return config.createData(strategy.registry, this, strategy.calculateSize())
    }

    fun copy(): PaletteHolder<T> = PaletteHolder(strategy, data.copy())

    fun interface PaletteConsumer<T> {

        fun apply(element: T, location: Int)
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
                storage.set(i, palette.getId(value))
            }
        }

        fun write(writer: BinaryWriter) {
            writer.writeByte(storage.bits.toByte())
            palette.write(writer)
            writer.writeLongArray(storage.data)
        }

        fun calculateSerializedSize(): Int {
            return 1 + palette.calculateSerializedSize() + ByteBufExtras.getVarIntBytes(storage.size) + storage.data.size * Long.SIZE_BYTES
        }

        fun copy(): Data<T> = Data(configuration, storage.copy(), palette.copy())
    }

    abstract class Strategy<T>(val registry: IntBiMap<T>, private val sizeBits: Int) {

        private val cache = Int2ObjectOpenHashMap<Configuration<T>>()

        protected abstract fun createConfiguration(bits: Int): Configuration<T>

        fun configuration(bits: Int): Configuration<T> = cache.computeIfAbsent(bits, Int2ObjectFunction { createConfiguration(it) })

        fun indexOf(x: Int, y: Int, z: Int): Int = y shl sizeBits or z shl sizeBits or x

        fun calculateSize(): Int = 1 shl sizeBits * 3

        fun calculateSerializationBits(size: Int): Int {
            val sizeBits = Maths.ceillog2(size)
            val config = configuration(sizeBits)
            if (config.factory === GlobalPalette.Factory) return sizeBits
            return config.bits
        }

        private class Biomes(registry: IntBiMap<Biome>) : Strategy<Biome>(registry, 2) {

            override fun createConfiguration(bits: Int): Configuration<Biome> = when (bits) {
                0 -> Configuration(SingleValuePalette.Factory, bits)
                in 1..3 -> Configuration(ArrayPalette.Factory, bits)
                else -> Configuration(GlobalPalette.Factory, Maths.ceillog2(registry.size()))
            }
        }

        companion object {

            @JvmField
            val BLOCKS: Strategy<KryptonBlockState> = object : Strategy<KryptonBlockState>(KryptonBlock.STATES, 4) {

                override fun createConfiguration(bits: Int): Configuration<KryptonBlockState> = when (bits) {
                    0 -> Configuration(SingleValuePalette.Factory, bits)
                    in 1..4 -> Configuration(ArrayPalette.Factory, 4)
                    in 5..8 -> Configuration(MapPalette.Factory, bits)
                    else -> Configuration(GlobalPalette.Factory, Maths.ceillog2(registry.size()))
                }
            }

            @JvmStatic
            fun biomes(registry: IntBiMap<Biome>): Strategy<Biome> = Biomes(registry)
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
            data.getList(PALETTE_TAG, CompoundTag.ID).forEachCompound { entries.add(BlockStateSerialization.decode(it)) }
            return read(Strategy.BLOCKS, entries, data.getLongArray(DATA_TAG))
        }

        @JvmStatic
        fun readBiomes(data: CompoundTag, registry: Registry<Biome>): PaletteHolder<Biome> {
            val entries = ArrayList<Biome>()
            data.getList(PALETTE_TAG, StringTag.ID).forEachString {
                val biome = registry.get(Key.key(it))
                entries.add(checkNotNull(biome) { "Invalid palette data! Failed to find biome with key $it!" })
            }
            return read(Strategy.biomes(registry as KryptonRegistry<Biome>), entries, data.getLongArray(DATA_TAG))
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
                swapPalette(ids) { strategy.registry.getId(palette.get(it)) }
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
