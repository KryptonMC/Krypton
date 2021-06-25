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

import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList

operator fun NBTCompound.get(key: String, default: NBT) = get(key) ?: default

fun NBTCompound.getByte(key: String, default: Byte) = getByte(key) ?: default

fun NBTCompound.getByteArray(key: String, default: ByteArray) = getByteArray(key) ?: default

fun NBTCompound.getCompound(key: String, default: NBTCompound) = getCompound(key) ?: default

fun NBTCompound.getDouble(key: String, default: Double) = getDouble(key) ?: default

fun NBTCompound.getFloat(key: String, default: Float) = getFloat(key) ?: default

fun NBTCompound.getInt(key: String, default: Int) = getInt(key) ?: default

fun NBTCompound.getIntArray(key: String, default: IntArray) = getIntArray(key) ?: default

fun NBTCompound.getLong(key: String, default: Long) = getLong(key) ?: default

fun NBTCompound.getLongArray(key: String, default: LongArray) = getLongArray(key) ?: default

fun NBTCompound.getShort(key: String, default: Short) = getShort(key) ?: default

fun NBTCompound.getNumber(key: String, default: Number) = getNumber(key) ?: default

fun NBTCompound.getString(key: String, default: String) = getString(key) ?: default

fun <T : NBT> NBTCompound.getList(key: String, default: NBTList<T>) = getList(key) ?: default

fun NBTCompound.getBoolean(key: String, default: Boolean) = getBoolean(key) ?: default
