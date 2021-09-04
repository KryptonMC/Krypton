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
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.MutableCompoundTag
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.NumberTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag

data class NBTMapType(val map: MutableCompoundTag = MutableCompoundTag()) : MapType<String> {

    constructor(tag: CompoundTag) : this(tag as MutableCompoundTag)

    override fun size() = map.size

    override fun isEmpty() = map.isEmpty()

    override fun clear() = map.clear()

    override fun keys() = map.keys

    override fun copy() = NBTMapType(map.copy())

    override fun hasKey(key: String) = map.containsKey(key)

    override fun hasKey(key: String, type: ObjectType): Boolean {
        val entry = map[key] ?: return false
        val valueType = NBTListType.getType(entry.id)
        return valueType === type || (type === ObjectType.NUMBER && valueType.isNumber)
    }

    override fun remove(key: String) {
        map.remove(key)
    }

    override fun getGeneric(key: String): Any? {
        val entry = map[key] ?: return null
        return when (entry.id) {
            ByteTag.ID, ShortTag.ID, IntTag.ID, LongTag.ID, FloatTag.ID, DoubleTag.ID -> (entry as NumberTag).value
            CompoundTag.ID -> NBTMapType(entry as CompoundTag)
            ListTag.ID -> NBTListType(entry as MutableListTag)
            StringTag.ID -> (entry as StringTag).value
            ByteArrayTag.ID -> (entry as ByteArrayTag).data
            IntArrayTag.ID -> (entry as IntArrayTag).data
            LongArrayTag.ID -> (entry as LongArrayTag).data
            else -> error("Unrecognized type ${entry.id}!")
        }
    }

    override fun getNumber(key: String, dfl: Number?) = (map[key] as? NumberTag)?.value ?: dfl

    override fun getBoolean(key: String) = getByte(key) != 0.toByte()

    override fun getBoolean(key: String, dfl: Boolean) = getByte(key, if (dfl) 1 else 0) != 0.toByte()

    override fun setBoolean(key: String, value: Boolean) = setByte(key, if (value) 1 else 0)

    override fun getByte(key: String) = map.getByte(key)

    override fun getByte(key: String, dfl: Byte) = getNumber(key)?.toByte() ?: dfl

    override fun setByte(key: String, value: Byte) {
        map.putByte(key, value)
    }

    override fun getShort(key: String) = map.getShort(key)

    override fun getShort(key: String, dfl: Short) = getNumber(key)?.toShort() ?: dfl

    override fun setShort(key: String, value: Short) {
        map.putShort(key, value)
    }

    override fun getInt(key: String) = map.getInt(key)

    override fun getInt(key: String, dfl: Int) = getNumber(key)?.toInt() ?: dfl

    override fun setInt(key: String, value: Int) {
        map.putInt(key, value)
    }

    override fun getLong(key: String) = map.getLong(key)

    override fun getLong(key: String, dfl: Long) = getNumber(key)?.toLong() ?: dfl

    override fun setLong(key: String, value: Long) {
        map.putLong(key, value)
    }

    override fun getFloat(key: String) = map.getFloat(key)

    override fun getFloat(key: String, dfl: Float) = getNumber(key)?.toFloat() ?: dfl

    override fun setFloat(key: String, value: Float) {
        map.putFloat(key, value)
    }

    override fun getDouble(key: String) = map.getDouble(key)

    override fun getDouble(key: String, dfl: Double) = getNumber(key)?.toDouble() ?: dfl

    override fun setDouble(key: String, value: Double) {
        map.putDouble(key, value)
    }

    override fun getBytes(key: String, dfl: ByteArray?) = (map[key] as? ByteArrayTag)?.data ?: dfl

    override fun setBytes(key: String, value: ByteArray) {
        map.putByteArray(key, value)
    }

    override fun getShorts(key: String, dfl: ShortArray?) = dfl

    override fun setShorts(key: String, value: ShortArray?) = throw UnsupportedOperationException()

    override fun getInts(key: String, dfl: IntArray?) = (map[key] as? IntArrayTag)?.data ?: dfl

    override fun setInts(key: String, value: IntArray) {
        map.putIntArray(key, value)
    }

    override fun getLongs(key: String, dfl: LongArray?) = (map[key] as? LongArrayTag)?.data ?: dfl

    override fun setLongs(key: String, value: LongArray) {
        map.putLongArray(key, value)
    }

    override fun getListUnchecked(key: String, dfl: ListType?) = (map[key] as? MutableListTag)?.let { NBTListType(it) } ?: dfl

    override fun setList(key: String, value: ListType) {
        map[key] = (value as NBTListType).list
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getMap(key: String, dfl: MapType<T>?): MapType<T>? =
        (map[key] as? CompoundTag)?.let { NBTMapType(it) as MapType<T> } ?: dfl

    override fun setMap(key: String, value: MapType<*>) {
        map[key] = (value as NBTMapType).map
    }

    override fun getString(key: String, dfl: String?) = (map[key] as? StringTag)?.value ?: dfl

    override fun setString(key: String, value: String) {
        map.putString(key, value)
    }
}
