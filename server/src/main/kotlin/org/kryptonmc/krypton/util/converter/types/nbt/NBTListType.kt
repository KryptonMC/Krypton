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
package org.kryptonmc.krypton.util.converter.types.nbt

import ca.spottedleaf.dataconverter.types.ListType
import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.MutableCompoundTag
import org.kryptonmc.nbt.NumberTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag

data class NBTListType(val list: ListTag = ListTag()) : ListType {

    override fun copy() = NBTListType(list.copy())

    override fun getType(): ObjectType = getType(list.elementType)

    override fun size() = list.size

    override fun remove(index: Int) {
        list.removeAt(index)
    }

    override fun getNumber(index: Int) = (list[index] as? NumberTag ?: error("")).value

    override fun getByte(index: Int) = getNumber(index).toByte()

    override fun setByte(index: Int, to: Byte) {
        list[index] = ByteTag.of(to)
    }

    override fun getShort(index: Int) = getNumber(index).toShort()

    override fun setShort(index: Int, to: Short) {
        list[index] = ShortTag.of(to)
    }

    override fun getInt(index: Int) = getNumber(index).toInt()

    override fun setInt(index: Int, to: Int) {
        list[index] = IntTag.of(to)
    }

    override fun getLong(index: Int) = getNumber(index).toLong()

    override fun setLong(index: Int, to: Long) {
        list[index] = LongTag.of(to)
    }

    override fun getFloat(index: Int) = getNumber(index).toFloat()

    override fun setFloat(index: Int, to: Float) {
        list[index] = FloatTag.of(to)
    }

    override fun getDouble(index: Int) = getNumber(index).toDouble()

    override fun setDouble(index: Int, to: Double) {
        list[index] = DoubleTag.of(to)
    }

    override fun getBytes(index: Int) = (list[index] as? ByteArrayTag ?: error("")).data

    override fun setBytes(index: Int, to: ByteArray) {
        list[index] = ByteArrayTag(to)
    }

    override fun getShorts(index: Int) = throw UnsupportedOperationException() // NBT does not support shorts

    override fun setShorts(index: Int, to: ShortArray?) = throw UnsupportedOperationException() // NBT does not support shorts

    override fun getInts(index: Int) = (list[index] as? IntArrayTag ?: error("")).data

    override fun setInts(index: Int, to: IntArray) {
        list[index] = IntArrayTag(to)
    }

    override fun getLongs(index: Int) = (list[index] as? LongArrayTag ?: error("")).data

    override fun setLongs(index: Int, to: LongArray) {
        list[index] = LongArrayTag(to)
    }

    override fun getList(index: Int) = NBTListType(list[index] as? ListTag ?: error(""))

    override fun setList(index: Int, list: ListType) {
        this.list[index] = (list as NBTListType).list
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getMap(index: Int): MapType<T> {
        val element = list[index]
        if (element is MutableCompoundTag) return NBTMapType(element) as MapType<T>
        if (element is CompoundTag) return NBTMapType(element.mutable()) as MapType<T>
        error("Element at $index was not a map!")
    }

    override fun setMap(index: Int, to: MapType<*>) {
        list[index] = (to as NBTMapType).map
    }

    override fun getString(index: Int) = (list[index] as? StringTag ?: error("")).value

    override fun setString(index: Int, to: String) {
        list[index] = StringTag.of(to)
    }

    override fun addByte(b: Byte) {
        list.add(ByteTag.of(b))
    }

    override fun addByte(index: Int, b: Byte) {
        list.add(index, ByteTag.of(b))
    }

    override fun addShort(s: Short) {
        list.add(ShortTag.of(s))
    }

    override fun addShort(index: Int, s: Short) {
        list.add(index, ShortTag.of(s))
    }

    override fun addInt(i: Int) {
        list.add(IntTag.of(i))
    }

    override fun addInt(index: Int, i: Int) {
        list.add(index, IntTag.of(i))
    }

    override fun addLong(l: Long) {
        list.add(LongTag.of(l))
    }

    override fun addLong(index: Int, l: Long) {
        list.add(index, LongTag.of(l))
    }

    override fun addFloat(f: Float) {
        list.add(FloatTag.of(f))
    }

    override fun addFloat(index: Int, f: Float) {
        list.add(index, FloatTag.of(f))
    }

    override fun addDouble(d: Double) {
        list.add(DoubleTag.of(d))
    }

    override fun addDouble(index: Int, d: Double) {
        list.add(index, DoubleTag.of(d))
    }

    override fun addByteArray(arr: ByteArray) {
        list.add(ByteArrayTag(arr))
    }

    override fun addByteArray(index: Int, arr: ByteArray) {
        list.add(index, ByteArrayTag(arr))
    }

    override fun addShortArray(arr: ShortArray?) = throw UnsupportedOperationException()

    override fun addShortArray(index: Int, arr: ShortArray?) = throw UnsupportedOperationException()

    override fun addIntArray(arr: IntArray) {
        list.add(IntArrayTag(arr))
    }

    override fun addIntArray(index: Int, arr: IntArray) {
        list.add(index, IntArrayTag(arr))
    }

    override fun addLongArray(arr: LongArray) {
        list.add(LongArrayTag(arr))
    }

    override fun addLongArray(index: Int, arr: LongArray) {
        list.add(index, LongArrayTag(arr))
    }

    override fun addList(list: ListType) {
        this.list.add((list as NBTListType).list)
    }

    override fun addList(index: Int, list: ListType) {
        this.list.add(index, (list as NBTListType).list)
    }

    override fun addMap(map: MapType<*>) {
        list.add((map as NBTMapType).map)
    }

    override fun addMap(index: Int, map: MapType<*>) {
        list.add(index, (map as NBTMapType).map)
    }

    override fun addString(string: String) {
        list.add(StringTag.of(string))
    }

    override fun addString(index: Int, string: String) {
        list.add(index, StringTag.of(string))
    }

    companion object {

        fun getType(id: Int) = when (id) {
            EndTag.ID -> ObjectType.NONE
            ByteTag.ID -> ObjectType.BYTE
            ShortTag.ID -> ObjectType.SHORT
            IntTag.ID -> ObjectType.INT
            LongTag.ID -> ObjectType.LONG
            FloatTag.ID -> ObjectType.FLOAT
            DoubleTag.ID -> ObjectType.DOUBLE
            ByteArrayTag.ID -> ObjectType.BYTE_ARRAY
            StringTag.ID -> ObjectType.STRING
            ListTag.ID -> ObjectType.LIST
            CompoundTag.ID -> ObjectType.MAP
            IntArrayTag.ID -> ObjectType.INT_ARRAY
            LongArrayTag.ID -> ObjectType.LONG_ARRAY
            else -> error("Unknown type ${id}!")
        }
    }
}
