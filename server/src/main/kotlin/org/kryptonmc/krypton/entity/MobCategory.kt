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
package org.kryptonmc.krypton.entity

import com.mojang.serialization.Codec
import org.kryptonmc.api.util.StringSerializable
import org.kryptonmc.krypton.util.codec

enum class MobCategory(
    override val serialized: String,
    val max: Int,
    val isFriendly: Boolean,
    val isPersistent: Boolean,
    val despawnDistance: Int
) : StringSerializable {

    MONSTER("monster", 70, false, false, 128),
    CREATURE("creature", 10, true, true, 128),
    AMBIENT("ambient", 15, true, false, 128),
    UNDERGROUND_WATER_CREATURE("underground_water_creature", 5, true, false, 128),
    WATER_CREATURE("water_creature", 5, true, false, 128),
    WATER_AMBIENT("water_ambient", 20, true, false, 64),
    MISC("misc", -1, true, true, 128);

    val noDespawnDistance = 32

    companion object {

        private val BY_NAME = values().associateBy { it.serialized }
        val CODEC: Codec<MobCategory> = values().codec { BY_NAME[it] }
    }
}
