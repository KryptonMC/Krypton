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
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.ByteArrayBinaryTag
import net.kyori.adventure.nbt.ByteBinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.DoubleBinaryTag
import net.kyori.adventure.nbt.EndBinaryTag
import net.kyori.adventure.nbt.FloatBinaryTag
import net.kyori.adventure.nbt.IntArrayBinaryTag
import net.kyori.adventure.nbt.IntBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import net.kyori.adventure.nbt.LongBinaryTag
import net.kyori.adventure.nbt.ShortBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import net.kyori.adventure.nbt.TagStringIO
import java.nio.ByteBuffer
import java.util.Arrays
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream
import kotlin.streams.asSequence

object BinaryTagOps : DynamicOps<BinaryTag> {

    override fun empty() = EndBinaryTag.get()

    override fun <U> convertTo(outOps: DynamicOps<U>, input: BinaryTag): U = when (input) {
        is EndBinaryTag -> outOps.empty()
        is ByteBinaryTag -> outOps.createByte(input.value())
        is ShortBinaryTag -> outOps.createShort(input.value())
        is IntBinaryTag -> outOps.createInt(input.value())
        is LongBinaryTag -> outOps.createLong(input.value())
        is FloatBinaryTag -> outOps.createFloat(input.value())
        is DoubleBinaryTag -> outOps.createDouble(input.value())
        is ByteArrayBinaryTag -> outOps.createByteList(ByteBuffer.wrap(input.value()))
        is StringBinaryTag -> outOps.createString(input.value())
        is ListBinaryTag -> convertList(outOps, input)
        is CompoundBinaryTag -> convertMap(outOps, input)
        is IntArrayBinaryTag -> outOps.createIntList(Arrays.stream(input.value()))
        is LongArrayBinaryTag -> outOps.createLongList(Arrays.stream(input.value()))
        else -> error("Tag type ${input.type()} not recognised!")
    }

    override fun createNumeric(i: Number) = DoubleBinaryTag.of(i.toDouble())

    override fun createByte(value: Byte) = ByteBinaryTag.of(value)

    override fun createShort(value: Short) = ShortBinaryTag.of(value)

    override fun createInt(value: Int) = IntBinaryTag.of(value)

    override fun createLong(value: Long) = LongBinaryTag.of(value)

    override fun createFloat(value: Float) = FloatBinaryTag.of(value)

    override fun createDouble(value: Double) = DoubleBinaryTag.of(value)

    override fun createBoolean(value: Boolean): ByteBinaryTag = if (value) ByteBinaryTag.ONE else ByteBinaryTag.ZERO

    override fun createString(value: String) = StringBinaryTag.of(value)

    override fun createByteList(input: ByteBuffer) = ByteArrayBinaryTag.of(*input.array())

    override fun createIntList(input: IntStream) = IntArrayBinaryTag.of(*input.toArray())

    override fun createLongList(input: LongStream) = LongArrayBinaryTag.of(*input.toArray())

    override fun createList(input: Stream<BinaryTag>): BinaryTag {
        val iterator = Iterators.peekingIterator(input.iterator())
        if (!iterator.hasNext()) return ListBinaryTag.empty()
        return when (iterator.peek()) {
            is ByteBinaryTag -> ByteArrayBinaryTag.of(*iterator.asSequence().map { (it as ByteBinaryTag).value() }.toList().toByteArray())
            is IntBinaryTag -> IntArrayBinaryTag.of(*iterator.asSequence().map { (it as IntBinaryTag).value() }.toList().toIntArray())
            is LongBinaryTag -> LongArrayBinaryTag.of(*iterator.asSequence().map { (it as LongBinaryTag).value() }.toList().toLongArray())
            else -> ListBinaryTag.from(iterator.asSequence().asIterable())
        }
    }

    override fun createMap(map: Stream<Pair<BinaryTag, BinaryTag>>) = CompoundBinaryTag.from(map.asSequence()
        .associate { it.first.toString() to it.second })

    override fun getNumberValue(input: BinaryTag): DataResult<Number> = when (input) {
        is ByteBinaryTag -> DataResult.success(input.value())
        is ShortBinaryTag -> DataResult.success(input.value())
        is IntBinaryTag -> DataResult.success(input.value())
        is LongBinaryTag -> DataResult.success(input.value())
        is FloatBinaryTag -> DataResult.success(input.value())
        is DoubleBinaryTag -> DataResult.success(input.value())
        else -> DataResult.error("Tag type ${input.type()} is not a number! (required to convert to number)")
    }

    override fun getStringValue(input: BinaryTag): DataResult<String> = if (input is StringBinaryTag) {
        DataResult.success(input.value())
    } else {
        DataResult.error("Tag type ${input.type()} is not a string! (required to convert to string)")
    }

    override fun getMap(input: BinaryTag): DataResult<MapLike<BinaryTag>> = if (input is CompoundBinaryTag) {
        DataResult.success(MapLike.forMap(input.associate { StringBinaryTag.of(it.key) to it.value }, this))
    } else {
        DataResult.error("Tag type ${input.type()} is not a compound! (required to convert to map)")
    }

    override fun getMapValues(input: BinaryTag): DataResult<Stream<Pair<BinaryTag, BinaryTag?>>> = if (input is CompoundBinaryTag) {
        DataResult.success(input.keySet().map { Pair.of(createString(it) as BinaryTag, input.get(it)) }.stream())
    } else {
        DataResult.error("Tag type ${input.type()} is not a compound! (required to convert to map to retrieve values)")
    }

    override fun getMapEntries(input: BinaryTag): DataResult<Consumer<BiConsumer<BinaryTag, BinaryTag?>>> = if (input is CompoundBinaryTag) {
        DataResult.success(Consumer { consumer -> input.keySet().forEach { consumer.accept(createString(it) as BinaryTag, input.get(it)) } })
    } else {
        DataResult.error("Tag type ${input.type()} is not a compound! (required to convert to map to retrieve entries)")
    }

    override fun getStream(input: BinaryTag): DataResult<Stream<BinaryTag>> = when (input) {
        is ByteArrayBinaryTag -> DataResult.success(input.value().toList().stream().map { ByteBinaryTag.of(it) })
        is IntArrayBinaryTag -> DataResult.success(input.value().toList().stream().map { IntBinaryTag.of(it) })
        is LongArrayBinaryTag -> DataResult.success(input.value().toList().stream().map { LongBinaryTag.of(it) })
        is ListBinaryTag -> DataResult.success(input.stream())
        else -> DataResult.error("Tag type ${input.type()} is not a list! (required to convert to stream)")
    }

    override fun getList(input: BinaryTag): DataResult<Consumer<Consumer<BinaryTag>>> = when (input) {
        is ByteArrayBinaryTag -> DataResult.success(Consumer { input.map(ByteBinaryTag::of).forEach(it) })
        is IntArrayBinaryTag -> DataResult.success(Consumer { input.map(IntBinaryTag::of).forEach(it) })
        is LongArrayBinaryTag -> DataResult.success(Consumer { input.map(LongBinaryTag::of).forEach(it) })
        is ListBinaryTag -> DataResult.success(Consumer { input.forEach(it) })
        else -> DataResult.error("Tag type ${input.type()} is not a list! (required to convert to list)")
    }

    override fun getByteBuffer(input: BinaryTag): DataResult<ByteBuffer> = if (input !is ByteArrayBinaryTag) {
        DataResult.error("Tag type ${input.type()} is not a byte array! (required to convert to ByteBuffer)")
    } else {
        DataResult.success(ByteBuffer.wrap(input.value()))
    }

    override fun getIntStream(input: BinaryTag): DataResult<IntStream> = if (input !is IntArrayBinaryTag) {
        DataResult.error("Tag type ${input.type()} is not an integer array! (required to convert to IntStream)")
    } else {
        DataResult.success(Arrays.stream(input.value()))
    }

    override fun getLongStream(input: BinaryTag): DataResult<LongStream> = if (input !is LongArrayBinaryTag) {
        DataResult.error("Tag type ${input.type()} is not a long array! (required to convert to LongStream)")
    } else {
        DataResult.success(Arrays.stream(input.value()))
    }

    override fun mergeToList(list: BinaryTag, value: BinaryTag): DataResult<BinaryTag> {
        return when (list) {
            is ByteArrayBinaryTag -> {
                if (value !is ByteBinaryTag) return DataResult.error("Tag type ${value.type()} is not a byte! (required for list type ${list.type()}")
                DataResult.success(ByteArrayBinaryTag.of(*list.value(), value.value()))
            }
            is IntArrayBinaryTag -> {
                if (value !is IntBinaryTag) return DataResult.error("Tag type ${value.type()} is not an integer! (required for list type ${list.type()}")
                DataResult.success(IntArrayBinaryTag.of(*list.value(), value.value()))
            }
            is LongArrayBinaryTag -> {
                if (value !is LongBinaryTag) return DataResult.error("Tag type ${value.type()} is not a long! (required for list type ${list.type()})")
                DataResult.success(LongArrayBinaryTag.of(*list.value(), value.value()))
            }
            is ListBinaryTag -> DataResult.success(list.add(value))
            else -> DataResult.error("Tag type ${list.type()} cannot be converted to a list and have $value added to it!")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeToList(list: BinaryTag, values: List<BinaryTag>): DataResult<BinaryTag> {
        if (values.isEmpty()) return DataResult.success(list)
        return when (list) {
            is ByteArrayBinaryTag -> {
                if (values.first() !is ByteBinaryTag) return DataResult.error("Tag type ${values.first().type()} is not a byte! (required for list type ${list.type()})")
                DataResult.success(ByteArrayBinaryTag.of(*list.value(), *(values as List<ByteBinaryTag>).map(ByteBinaryTag::value).toByteArray()))
            }
            is IntArrayBinaryTag -> {
                if (values.first() !is IntBinaryTag) return DataResult.error("Tag type ${values.first().type()} is not an integer! (required for list type ${list.type()})")
                DataResult.success(IntArrayBinaryTag.of(*list.value(), *(values as List<IntBinaryTag>).map(IntBinaryTag::value).toIntArray()))
            }
            is LongArrayBinaryTag -> {
                if (values.first() !is LongBinaryTag) return DataResult.error("Tag type ${values.first().type()} is not a long! (required for list type ${list.type()})")
                DataResult.success(LongArrayBinaryTag.of(*list.value(), *(values as List<LongBinaryTag>).map(LongBinaryTag::value).toLongArray()))
            }
            else -> DataResult.error("Tag type ${list.type()} cannot be converted to a list and have $values added to it!")
        }
    }

    override fun mergeToMap(map: BinaryTag, key: BinaryTag, value: BinaryTag): DataResult<BinaryTag> {
        if (map !is CompoundBinaryTag) return DataResult.error("Tag type ${map.type()} cannot be converted to a compound and have key $key and value $value put in it!")
        if (key !is StringBinaryTag) return DataResult.error("Key type ${key.type()} must be a string!")
        return DataResult.success(map.put(key.value(), value))
    }

    override fun mergeToMap(map: BinaryTag, values: MapLike<BinaryTag>): DataResult<BinaryTag> {
        if (map !is CompoundBinaryTag) return DataResult.error("Tag type ${map.type()} cannot be converted to a compound and have $values put in it!")
        val nonStringTags = mutableListOf<BinaryTag>()
        var newCompound = map
        values.entries().forEach {
            val key = it.first
            if (key !is StringBinaryTag) nonStringTags += key else newCompound = map.put(key.value(), it.second)
        }
        if (nonStringTags.isNotEmpty()) return DataResult.error("All keys in map $values must be strings, $nonStringTags were not!")
        return DataResult.success(newCompound)
    }

    override fun remove(input: BinaryTag, key: String): BinaryTag {
        if (input !is CompoundBinaryTag) return input
        return input.remove(key)
    }

    override fun mapBuilder() = BinaryTagRecordBuilder

    object BinaryTagRecordBuilder : RecordBuilder.AbstractStringBuilder<BinaryTag, CompoundBinaryTag.Builder>(BinaryTagOps) {

        override fun initBuilder() = CompoundBinaryTag.builder()

        override fun append(key: String, value: BinaryTag, builder: CompoundBinaryTag.Builder) = with (builder) {
            put(key, value)
        }

        override fun build(builder: CompoundBinaryTag.Builder, prefix: BinaryTag?): DataResult<BinaryTag> {
            if (prefix == null || prefix == EndBinaryTag.get()) return DataResult.success(builder.build())
            if (prefix !is CompoundBinaryTag) return DataResult.error("Tag type ${prefix.type()} is not allowed for prefix of binary tag record builder!")
            return DataResult.success(CompoundBinaryTag.builder().put(prefix).put(builder.build()).build())
        }
    }
}
