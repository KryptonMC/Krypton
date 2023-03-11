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
package org.kryptonmc.krypton.util.nbt

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag

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

fun CompoundTag.getBlockPos(prefix: String = ""): Vec3i = Vec3i(getInt(prefix + 'X'), getInt(prefix + 'Y'), getInt(prefix + 'Z'))

fun CompoundTag.putBlockPos(key: String, pos: Vec3i): CompoundTag = put(key, ImmutableCompoundTag.builder().putBlockPosParts(pos).build())

fun CB.putBlockPos(key: String, pos: Vec3i): CB = put(key, ImmutableCompoundTag.builder().putBlockPosParts(pos).build())

fun CB.putBlockPosParts(pos: Vec3i, prefix: String = ""): CB = putInt(prefix + 'X', pos.x).putInt(prefix + 'Y', pos.y).putInt(prefix + 'Z', pos.z)

inline fun <T> CB.putNullable(name: String, value: T, setter: CB.(String, T & Any) -> CB): CB =
    if (value == null) this else setter(this, name, value)

inline fun <T> LB.addNullable(value: T, adder: LB.(T & Any) -> LB): LB = if (value == null) this else adder(this, value)

private typealias CB = CompoundTag.Builder
private typealias LB = ListTag.Builder
