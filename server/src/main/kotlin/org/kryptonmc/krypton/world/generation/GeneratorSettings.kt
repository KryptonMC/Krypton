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
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

abstract class GeneratorSettings {

    abstract val structures: GeneratorStructures

    abstract fun toNBT(): CompoundTag
}

data class GeneratorStructures(
    val stronghold: GeneratorStronghold,
    val structures: Map<Key, GeneratorStructure>
) {

    fun toNBT() = compound {
        put("stronghold", stronghold.toNBT())
        compound("structures") { structures.forEach { put(it.key.asString(), it.value.toNBT()) } }
    }
}

data class GeneratorStronghold(
    val distance: Int,
    val count: Int,
    val spread: Int
) {

    fun toNBT() = compound {
        int("distance", distance)
        int("count", count)
        int("spread", spread)
    }
}

data class GeneratorStructure(
    val spacing: Int,
    val separation: Int,
    val salt: Int
) {

    fun toNBT() = compound {
        int("spacing", spacing)
        int("separation", separation)
        int("salt", salt)
    }
}
