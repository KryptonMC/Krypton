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
package org.kryptonmc.krypton.world.generation

import net.kyori.adventure.key.Key
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.kryptonmc.krypton.world.transform

abstract class GeneratorSettings {

    abstract val structures: GeneratorStructures

    abstract fun toNBT(): NBTCompound
}

data class GeneratorStructures(
    val stronghold: GeneratorStronghold,
    val structures: Map<Key, GeneratorStructure>
) {

    fun toNBT() = NBTCompound()
        .set("stronghold", stronghold.toNBT())
        .set("structures", NBTCompound().apply { structures.transform { it.key.toString() to it.value.toNBT() }.forEach { set(it.key, it.value) } })
}

data class GeneratorStronghold(
    val distance: Int,
    val count: Int,
    val spread: Int
) {

    fun toNBT() = NBTCompound()
        .setInt("distance", distance)
        .setInt("count", count)
        .setInt("spread", spread)
}

data class GeneratorStructure(
    val spacing: Int,
    val separation: Int,
    val salt: Int
) {

    fun toNBT() = NBTCompound()
        .setInt("spacing", spacing)
        .setInt("separation", separation)
        .setInt("salt", salt)
}
