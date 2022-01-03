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
package org.kryptonmc.krypton.entity

import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.spongepowered.math.vector.Vector3f
import org.spongepowered.math.vector.Vector3i

fun ListTag.readRotation(): Vector3f = Vector3f.from(getFloat(0), getFloat(1), getFloat(2))

fun CompoundTag.Builder.rotation(key: String, rotation: Vector3f): CompoundTag.Builder = list(key) {
    addFloat(rotation.x())
    addFloat(rotation.y())
    addFloat(rotation.z())
}

fun CompoundTag.getVector3i(key: String): Vector3i? {
    if (!contains(key, CompoundTag.ID)) return null
    val position = getCompound(key)
    return Vector3i(position.getInt("X"), position.getInt("Y"), position.getInt("Z"))
}

fun CompoundTag.Builder.vector3i(key: String, vector: Vector3i): CompoundTag.Builder = compound(key) {
    int("X", vector.x())
    int("Y", vector.y())
    int("Z", vector.z())
}
