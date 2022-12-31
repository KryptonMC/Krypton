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

import org.kryptonmc.krypton.util.uuid.UUIDUtil
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.ListTag
import java.util.UUID

fun CompoundTag.hasUUID(name: String): Boolean {
    val tag = get(name)
    return tag != null && tag.id() == IntArrayTag.ID && (tag as IntArrayTag).data.size == 4
}

fun CompoundTag.getUUID(name: String): UUID = UUIDUtil.loadUUID(get(name)!!)

fun CompoundTag.Builder.putUUID(name: String, uuid: UUID): CompoundTag.Builder = put(name, UUIDUtil.createUUID(uuid))

fun ListTag.Builder.addUUID(uuid: UUID): ListTag.Builder = add(UUIDUtil.createUUID(uuid))
