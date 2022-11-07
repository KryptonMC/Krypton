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
package org.kryptonmc.krypton.util.nbt

import net.kyori.adventure.key.Keyed
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.spongepowered.math.vector.Vector3i

private const val NUMBER_TYPE = 99
private const val DATA_VERSION_TAG = "DataVersion"

fun CompoundTag.hasNumber(name: String): Boolean = contains(name, NUMBER_TYPE)

fun CompoundTag.getDataVersion(): Int = if (contains(DATA_VERSION_TAG, IntTag.ID)) getInt(DATA_VERSION_TAG) else -1

fun CompoundTag.Builder.putDataVersion(): CompoundTag.Builder = putInt(DATA_VERSION_TAG, KryptonPlatform.worldVersion)

fun <E : Enum<E>> CB.putStringEnum(name: String, value: E): CB = putString(name, value.name.lowercase())

fun <T : Keyed> CB.putKeyed(name: String, value: T): CB = putString(name, value.key().asString())

inline fun <T> CompoundTag.getNullable(name: String, type: Int, getter: CompoundTag.(String) -> T & Any): T? =
    if (contains(name, type)) getter(this, name) else null

fun CompoundTag.getNullableCompound(name: String): CompoundTag? = getNullable(name, CompoundTag.ID, CompoundTag::getCompound)

fun CompoundTag.hasBlockPos(prefix: String = ""): Boolean = hasNumber(prefix + 'X') && hasNumber(prefix + 'Y') && hasNumber(prefix + 'Z')

fun CompoundTag.getBlockPos(prefix: String = ""): Vector3i = Vector3i.from(getInt(prefix + 'X'), getInt(prefix + 'Y'), getInt(prefix + 'Z'))

fun CompoundTag.putBlockPos(key: String, pos: Vector3i): CompoundTag = put(key, ImmutableCompoundTag.builder().putBlockPosParts(pos).build())

fun CB.putBlockPos(key: String, pos: Vector3i): CB = put(key, ImmutableCompoundTag.builder().putBlockPosParts(pos).build())

fun CB.putBlockPosParts(pos: Vector3i, prefix: String = ""): CB =
    putInt(prefix + 'X', pos.x()).putInt(prefix + 'Y', pos.y()).putInt(prefix + 'Z', pos.z())

inline fun <T> CB.putNullable(name: String, value: T, setter: CB.(String, T & Any) -> CB): CB =
    if (value == null) this else setter(this, name, value)

inline fun <T> LB.addNullable(value: T, adder: LB.(T & Any) -> LB): LB = if (value == null) this else adder(this, value)

private typealias CB = CompoundTag.Builder
private typealias LB = ListTag.Builder
