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
package org.kryptonmc.krypton.util.nbt

import com.google.common.collect.Iterators
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapLike
import com.mojang.serialization.RecordBuilder
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTByte
import org.jglrxavpok.hephaistos.nbt.NBTByteArray
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTDouble
import org.jglrxavpok.hephaistos.nbt.NBTEnd
import org.jglrxavpok.hephaistos.nbt.NBTFloat
import org.jglrxavpok.hephaistos.nbt.NBTInt
import org.jglrxavpok.hephaistos.nbt.NBTIntArray
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTLong
import org.jglrxavpok.hephaistos.nbt.NBTLongArray
import org.jglrxavpok.hephaistos.nbt.NBTNumber
import org.jglrxavpok.hephaistos.nbt.NBTShort
import org.jglrxavpok.hephaistos.nbt.NBTString
import java.nio.ByteBuffer
import java.util.Arrays
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream
import kotlin.streams.asStream

object NBTOps : DynamicOps<NBT> {

    override fun empty() = NBTEnd

    override fun <U> convertTo(outOps: DynamicOps<U>, input: NBT): U = when (input) {
        NBTEnd -> outOps.empty()
        is NBTByte -> outOps.createByte(input.value)
        is NBTShort -> outOps.createShort(input.value)
        is NBTInt -> outOps.createInt(input.value)
        is NBTLong -> outOps.createLong(input.value)
        is NBTFloat -> outOps.createFloat(input.value)
        is NBTDouble -> outOps.createDouble(input.value)
        is NBTByteArray -> outOps.createByteList(ByteBuffer.wrap(input.value))
        is NBTString -> outOps.createString(input.value)
        is NBTList<*> -> convertList(outOps, input)
        is NBTCompound -> convertMap(outOps, input)
        is NBTIntArray -> outOps.createIntList(Arrays.stream(input.value))
        is NBTLongArray -> outOps.createLongList(Arrays.stream(input.value))
        else -> error("Tag type ${input.ID} not recognised! Unable to convert!")
    }

    override fun createNumeric(i: Number) = NBTDouble(i.toDouble())

    override fun createByte(value: Byte) = NBTByte(value)

    override fun createShort(value: Short) = NBTShort(value)

    override fun createInt(value: Int) = NBTInt(value)

    override fun createLong(value: Long) = NBTLong(value)

    override fun createFloat(value: Float) = NBTFloat(value)

    override fun createDouble(value: Double) = NBTDouble(value)

    override fun createBoolean(value: Boolean) = NBTByte(value)

    override fun createString(value: String) = NBTString(value)

    override fun createByteList(input: ByteBuffer) = NBTByteArray(input.array())

    override fun createIntList(input: IntStream) = NBTIntArray(input.toArray())

    override fun createLongList(input: LongStream) = NBTLongArray()

    override fun createList(input: Stream<NBT>): NBT {
        val iterator = Iterators.peekingIterator(input.iterator())
        if (!iterator.hasNext()) return NBTList<NBT>(0)
        return when (iterator.peek()) {
            is NBTByte -> NBTByteArray(iterator.asSequence().map { (it as NBTByte).value }.toList().toByteArray())
            is NBTInt -> NBTIntArray(iterator.asSequence().map { (it as NBTInt).value }.toList().toIntArray())
            is NBTLong -> NBTLongArray(iterator.asSequence().map { (it as NBTLong).value }.toList().toLongArray())
            else -> NBTList<NBT>(0).apply { iterator.asSequence().filter { it !== NBTEnd }.forEach { add(it) } }
        }
    }

    override fun createMap(map: Stream<Pair<NBT, NBT>>) = NBTCompound().apply { map.forEach { set(it.first.asString(), it.second) } }

    override fun getNumberValue(input: NBT): DataResult<Number> = if (input is NBTNumber<*>) DataResult.success(input.value) else DataResult.error("Not a number!")

    override fun getStringValue(input: NBT): DataResult<String> = if (input is NBTString) DataResult.success(input.value) else DataResult.error("Not a string!")

    override fun getMap(input: NBT): DataResult<MapLike<NBT>> = if (input is NBTCompound) {
        DataResult.success(MapLike.forMap(input.iterator().asSequence().associate { NBTString(it.first) to it.second }, this))
    } else {
        DataResult.error("Tag type ${input.ID} is not a compound! (required to convert to map)")
    }

    override fun getMapValues(input: NBT): DataResult<Stream<Pair<NBT, NBT?>>> = if (input is NBTCompound) {
        DataResult.success(input.getKeys().map { Pair.of(createString(it) as NBT, input[it]) }.stream())
    } else {
        DataResult.error("Tag type ${input.ID} is not a compound! (required to convert to map to retrieve values)")
    }

    override fun getMapEntries(input: NBT): DataResult<Consumer<BiConsumer<NBT, NBT?>>> = if (input is NBTCompound) {
        DataResult.success(Consumer { consumer -> input.getKeys().forEach { consumer.accept(createString(it) as NBT, input[it]) } })
    } else {
        DataResult.error("Tag type ${input.ID} is not a compound! (required to convert to map to retrieve entries)")
    }

    override fun getStream(input: NBT): DataResult<Stream<NBT>> = when (input) {
        is NBTByteArray -> DataResult.success(input.value.toList().stream().map { NBTByte(it) })
        is NBTIntArray -> DataResult.success(input.value.toList().stream().map { NBTInt(it) })
        is NBTLongArray -> DataResult.success(input.value.toList().stream().map { NBTLong(it) })
        is NBTList<*> -> DataResult.success(input.iterator().asSequence().asStream())
        else -> DataResult.error("Tag type ${input.ID} is not a list! (required to convert to stream)")
    }

    override fun getList(input: NBT): DataResult<Consumer<Consumer<NBT>>> = when (input) {
        is NBTByteArray -> DataResult.success(Consumer { consumer -> input.value.asSequence().map(::NBTByte).forEach { consumer.accept(it) } })
        is NBTIntArray -> DataResult.success(Consumer { consumer -> input.value.asSequence().map(::NBTInt).forEach { consumer.accept(it) } })
        is NBTLongArray -> DataResult.success(Consumer { consumer -> input.value.asSequence().map(::NBTLong).forEach { consumer.accept(it) } })
        is NBTList<*> -> DataResult.success(Consumer { input.forEach(it) })
        else -> DataResult.error("Tag type ${input.ID} is not a list! (required to convert to list)")
    }

    override fun getByteBuffer(input: NBT): DataResult<ByteBuffer> = if (input !is NBTByteArray) {
        DataResult.error("Tag type ${input.ID} is not a byte array! (required to convert to ByteBuffer)")
    } else {
        DataResult.success(ByteBuffer.wrap(input.value))
    }

    override fun getIntStream(input: NBT): DataResult<IntStream> = if (input !is NBTIntArray) {
        DataResult.error("Tag type ${input.ID} is not an integer array! (required to convert to IntStream)")
    } else {
        DataResult.success(Arrays.stream(input.value))
    }

    override fun getLongStream(input: NBT): DataResult<LongStream> = if (input !is NBTLongArray) {
        DataResult.error("Tag type ${input.ID} is not a long array! (required to convert to LongStream)")
    } else {
        DataResult.success(Arrays.stream(input.value))
    }

    @Suppress("UNCHECKED_CAST") // Nothing we can do here, blame generics
    override fun mergeToList(list: NBT, value: NBT): DataResult<NBT> {
        return when (list) {
            is NBTByteArray -> {
                if (value !is NBTByte) return DataResult.error("Tag type ${value.ID} is not a byte! (required for list type ${list.ID}")
                DataResult.success(NBTByteArray(list.value + value.value))
            }
            is NBTIntArray -> {
                if (value !is NBTInt) return DataResult.error("Tag type ${value.ID} is not an integer! (required for list type ${list.ID}")
                DataResult.success(NBTIntArray(list.value + value.value))
            }
            is NBTLongArray -> {
                if (value !is NBTLong) return DataResult.error("Tag type ${value.ID} is not a long! (required for list type ${list.ID})")
                DataResult.success(NBTLongArray(list.value + value.value))
            }
            is NBTList<*> -> DataResult.success((list as NBTList<NBT>).apply { add(value) })
            else -> DataResult.error("Tag type ${list.ID} cannot be converted to a list and have $value added to it!")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeToList(list: NBT, values: List<NBT>): DataResult<NBT> {
        if (values.isEmpty()) return DataResult.success(list)
        return when (list) {
            is NBTByteArray -> {
                if (values.first() !is NBTByte) return DataResult.error("Tag type ${values.first().ID} is not a byte! (required for list type ${list.ID})")
                DataResult.success(NBTByteArray(list.value + (values as List<NBTByte>).map(NBTByte::value).toByteArray()))
            }
            is NBTIntArray -> {
                if (values.first() !is NBTInt) return DataResult.error("Tag type ${values.first().ID} is not an integer! (required for list type ${list.ID})")
                DataResult.success(NBTIntArray(list.value + (values as List<NBTInt>).map(NBTInt::value).toIntArray()))
            }
            is NBTLongArray -> {
                if (values.first() !is NBTLong) return DataResult.error("Tag type ${values.first().ID} is not a long! (required for list type ${list.ID})")
                DataResult.success(NBTLongArray(list.value + (values as List<NBTLong>).map(NBTLong::value).toLongArray()))
            }
            is NBTList<*> -> DataResult.success(NBTList<NBT>(list.subtagType).apply { list.forEach { add(it) } })
            else -> DataResult.error("Tag type ${list.ID} cannot be converted to a list and have $values added to it!")
        }
    }

    override fun mergeToMap(map: NBT, key: NBT, value: NBT): DataResult<NBT> {
        if (map !is NBTCompound) return DataResult.error("Tag type ${map.ID} cannot be converted to a compound and have key $key and value $value put in it!")
        if (key !is NBTString) return DataResult.error("Key type ${key.ID} must be a string!")
        return DataResult.success(map.set(key.value, value))
    }

    override fun mergeToMap(map: NBT, values: MapLike<NBT>): DataResult<NBT> {
        if (map !is NBTCompound) return DataResult.error("Tag type ${map.ID} cannot be converted to a compound and have $values put in it!")
        val nonStringTags = mutableListOf<NBT>()
        var newCompound = map
        values.entries().forEach {
            val key = it.first
            if (key !is NBTString) nonStringTags += key else newCompound = map.set(key.value, it.second)
        }
        if (nonStringTags.isNotEmpty()) return DataResult.error("All keys in map $values must be strings, $nonStringTags were not!")
        return DataResult.success(newCompound)
    }

    override fun remove(input: NBT, key: String): NBT {
        if (input !is NBTCompound) return input
        return input.removeTag(key)
    }

    override fun mapBuilder() = BinaryTagRecordBuilder()

    override fun toString() = "NBT"

    class BinaryTagRecordBuilder : RecordBuilder.AbstractStringBuilder<NBT, NBTCompound>(NBTOps) {

        override fun initBuilder() = NBTCompound()

        override fun append(key: String, value: NBT, builder: NBTCompound) = builder.set(key, value)

        override fun build(builder: NBTCompound, prefix: NBT?): DataResult<NBT> {
            if (prefix == null || prefix is NBTEnd) return DataResult.success(builder)
            if (prefix !is NBTCompound) return DataResult.error("Tag type ${prefix.ID} is not allowed for prefix of binary tag record builder!")
            return DataResult.success(NBTCompound().apply {
                prefix.iterator().forEach { set(it.first, it.second) }
                builder.iterator().forEach { set(it.first, it.second) }
            })
        }
    }
}
