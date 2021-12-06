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
import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CollectionTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.MutableCollectionTag
import org.kryptonmc.nbt.MutableCompoundTag
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.NumberTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.compound
import java.nio.ByteBuffer
import java.util.Arrays
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.stream.Collectors
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream

// TODO: Remove this when we remove the DFU
object NBTOps : DynamicOps<Tag> {

    override fun empty(): Tag = EndTag

    override fun <U> convertTo(outOps: DynamicOps<U>, input: Tag): U = when (input) {
        EndTag -> outOps.empty()
        is ByteTag -> outOps.createByte(input.value)
        is ShortTag -> outOps.createShort(input.value)
        is IntTag -> outOps.createInt(input.value)
        is LongTag -> outOps.createLong(input.value)
        is FloatTag -> outOps.createFloat(input.value)
        is DoubleTag -> outOps.createDouble(input.value)
        is ByteArrayTag -> outOps.createByteList(ByteBuffer.wrap(input.data))
        is StringTag -> outOps.createString(input.value)
        is ListTag -> convertList(outOps, input)
        is CompoundTag -> convertMap(outOps, input)
        is IntArrayTag -> outOps.createIntList(Arrays.stream(input.data))
        is LongArrayTag -> outOps.createLongList(Arrays.stream(input.data))
        else -> error("Tag type ${input.id} not recognised! Unable to convert!")
    }

    override fun createNumeric(i: Number): Tag = DoubleTag.of(i.toDouble())

    override fun createByte(value: Byte): Tag = ByteTag.of(value)

    override fun createShort(value: Short): Tag = ShortTag.of(value)

    override fun createInt(value: Int): Tag = IntTag.of(value)

    override fun createLong(value: Long): Tag = LongTag.of(value)

    override fun createFloat(value: Float): Tag = FloatTag.of(value)

    override fun createDouble(value: Double): Tag = DoubleTag.of(value)

    override fun createBoolean(value: Boolean): Tag = ByteTag.of(value)

    override fun createString(value: String): Tag = StringTag.of(value)

    override fun createByteList(input: ByteBuffer): Tag = ByteArrayTag(input.array())

    override fun createIntList(input: IntStream): Tag = IntArrayTag(input.toArray())

    override fun createLongList(input: LongStream): Tag = LongArrayTag(input.toArray())

    override fun createList(input: Stream<Tag>): Tag {
        val iterator = Iterators.peekingIterator(input.iterator())
        if (!iterator.hasNext()) return MutableListTag()
        return when (iterator.peek()) {
            is ByteTag -> ByteArrayTag(iterator.asSequence().map { (it as ByteTag).value }.toList().toByteArray())
            is IntTag -> IntArrayTag(iterator.asSequence().map { (it as IntTag).value }.toList().toIntArray())
            is LongTag -> LongArrayTag(iterator.asSequence().map { (it as LongTag).value }.toList().toLongArray())
            else -> MutableListTag(iterator.asSequence().filter { it !== EndTag }.toMutableList())
        }
    }

    override fun createMap(map: Stream<Pair<Tag, Tag>>): Tag = CompoundTag.mutable(map.collect(Collectors.toMap(
        { it.first.asString() },
        { it.second }
    )))

    override fun getNumberValue(input: Tag): DataResult<Number> {
        if (input is NumberTag) return DataResult.success(input.value)
        return DataResult.error("Not a number!")
    }

    override fun getStringValue(input: Tag): DataResult<String> {
        if (input is StringTag) return DataResult.success(input.value)
        return DataResult.error("Not a string!")
    }

    override fun getMap(input: Tag): DataResult<MapLike<Tag>> {
        if (input is CompoundTag) {
            return DataResult.success(MapLike.forMap(
                input.iterator().asSequence().associate { StringTag.of(it.key) to it.value },
                this
            ))
        }
        return DataResult.error("Tag type ${input.id} is not a compound! (required to convert to map)")
    }

    override fun getMapValues(input: Tag): DataResult<Stream<Pair<Tag, Tag?>>> {
        if (input is CompoundTag) return DataResult.success(input.keys.stream().map { Pair.of(createString(it), input[it]) })
        return DataResult.error("Tag type ${input.id} is not a compound! (required to convert to a map to retrieve values)")
    }

    override fun getMapEntries(input: Tag): DataResult<Consumer<BiConsumer<Tag, Tag?>>> {
        if (input is CompoundTag) {
            return DataResult.success(Consumer { consumer -> input.keys.forEach { consumer.accept(createString(it), input[it]) } })
        }
        return DataResult.error("Tag type ${input.id} is not a compound! (required to convert to map to retrieve entries)")
    }

    override fun getStream(input: Tag): DataResult<Stream<Tag>> {
        if (input is CollectionTag<*>) return DataResult.success(input.stream().map { it })
        return DataResult.error("Tag type ${input.id} is not a collection!")
    }

    override fun getList(input: Tag): DataResult<Consumer<Consumer<Tag>>> {
        if (input is CollectionTag<*>) return DataResult.success(Consumer { input.forEach(it) })
        return DataResult.error("Tag type ${input.id} is not a list!")
    }

    override fun getByteBuffer(input: Tag): DataResult<ByteBuffer> {
        if (input is ByteArrayTag) return DataResult.success(ByteBuffer.wrap(input.data))
        return DataResult.error("Tag type ${input.id} is not a byte array! (required to convert to ByteBuffer)")
    }

    override fun getIntStream(input: Tag): DataResult<IntStream> {
        if (input is IntArrayTag) return DataResult.success(Arrays.stream(input.data))
        return DataResult.error("Tag type ${input.id} is not an integer array! (required to convert to IntStream)")
    }

    override fun getLongStream(input: Tag): DataResult<LongStream> {
        if (input is LongArrayTag) return DataResult.success(Arrays.stream(input.data))
        return DataResult.error("Tag type ${input.id} is not a long array! (required to convert to LongStream)")
    }

    override fun mergeToList(list: Tag, value: Tag): DataResult<Tag> {
        if (list !is CollectionTag<*> && list !is EndTag) {
            return DataResult.error("Tag type ${list.id} cannot have ${value.id} merged in to it as it is not a list!", list)
        }
        return DataResult.success(createGenericList(if (list is CollectionTag<*>) list.elementType else 0, value.id).fillOne(list, value))
    }

    override fun mergeToList(list: Tag, values: List<Tag>): DataResult<Tag> {
        if (list !is CollectionTag<*> && list !is EndTag) {
            return DataResult.error("Tag type ${list.id} cannot have $values merged in to it as it is not a list!", list)
        }
        val firstType = if (list is CollectionTag<*>) list.elementType else EndTag.ID
        val secondType = values.firstOrNull()?.id ?: 0
        return DataResult.success(createGenericList(firstType, secondType).fillMany(list, values))
    }

    override fun mergeToMap(map: Tag, key: Tag, value: Tag): DataResult<Tag> {
        if (map !is CompoundTag && map !is EndTag) {
            return DataResult.error("Tag type ${map.id} cannot have $key, $value merged in to it as it is not a map!", map)
        }
        if (key !is StringTag) return DataResult.error("Tag type ${key.id} for key $key is not a string!")
        val compound = MutableCompoundTag()
        if (map is CompoundTag) map.forEach { compound[it.key] = it.value }
        compound[key.asString()] = value
        return DataResult.success(compound)
    }

    override fun mergeToMap(map: Tag, values: MapLike<Tag>): DataResult<Tag> {
        if (map !is CompoundTag && map !is EndTag) {
            return DataResult.error("Tag type ${map.id} cannot have $values merged in to it as it is not a map!", map)
        }
        val compound = MutableCompoundTag()
        if (map is CompoundTag) map.forEach { compound[it.key] = it.value }
        val invalid = mutableListOf<Tag>()
        values.entries().forEach { if (it.first !is StringTag) invalid.add(it.first) else compound[it.first.asString()] = it.second }
        if (invalid.isNotEmpty()) return DataResult.error("All keys in map $values must be strings, $invalid were not!")
        return DataResult.success(compound)
    }

    override fun remove(input: Tag, key: String): Tag {
        if (input !is CompoundTag) return input
        val compound = MutableCompoundTag()
        input.asSequence().filter { it.key != key }.forEach { compound[it.key] = it.value }
        return compound
    }

    override fun mapBuilder(): RecordBuilder<Tag> = NBTRecordBuilder()

    override fun toString(): String = "NBT"

    private fun createGenericList(first: Int, second: Int): MutableCollectionTag<*> {
        if (typesMatch(first, second, LongTag.ID)) return LongArrayTag(LongArray(0))
        if (typesMatch(first, second, ByteTag.ID)) return ByteArrayTag(ByteArray(0))
        if (typesMatch(first, second, IntTag.ID)) return IntArrayTag(IntArray(0))
        return MutableListTag()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Tag> MutableCollectionTag<T>.fillOne(list: Tag, value: Tag) = apply {
        if (list is MutableCollectionTag<*>) list.forEach { add(it as T) }
        add(value as T)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Tag> MutableCollectionTag<T>.fillMany(list: Tag, values: List<Tag>) = apply {
        if (list is MutableCollectionTag<*>) list.forEach { add(it as T) }
        values.forEach { add(it as T) }
    }

    private fun typesMatch(first: Int, second: Int, third: Int): Boolean = first == third && (second == third || second == 0)

    class NBTRecordBuilder : RecordBuilder.AbstractStringBuilder<Tag, CompoundTag.Builder>(NBTOps) {

        override fun initBuilder(): CompoundTag.Builder = CompoundTag.builder()

        override fun append(key: String, value: Tag, builder: CompoundTag.Builder): CompoundTag.Builder = builder.put(key, value)

        override fun build(builder: CompoundTag.Builder, prefix: Tag?): DataResult<Tag> {
            if (prefix == null || prefix == EndTag) return DataResult.success(builder.build())
            if (prefix !is CompoundTag) {
                return DataResult.error("Tag type ${prefix.id} be merged in to $builder as it is not a map!", prefix)
            }
            return DataResult.success(compound {
                prefix.forEach { put(it.key, it.value) }
                builder.build().forEach { put(it.key, it.value) }
            })
        }
    }
}
