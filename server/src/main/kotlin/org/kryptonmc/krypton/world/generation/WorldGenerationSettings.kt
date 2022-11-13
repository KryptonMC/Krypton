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
package org.kryptonmc.krypton.world.generation

import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

@JvmRecord
data class WorldGenerationSettings(val seed: Long, val generateFeatures: Boolean, val bonusChest: Boolean, val dimensions: CompoundTag) {

    fun save(): CompoundTag = compound {
        putLong("seed", seed)
        putBoolean("generate_features", generateFeatures)
        putBoolean("bonus_chest", bonusChest)
        put("dimensions", dimensions)
    }

    companion object {

        private val RANDOM = RandomSource.createThreadLocal()

        @JvmStatic
        fun parse(data: CompoundTag): WorldGenerationSettings = WorldGenerationSettings(
            data.getLong("seed", RANDOM.nextLong()),
            data.getBoolean("generate_features"),
            data.getBoolean("bonus_chest"),
            data.getCompound("dimensions")
        )
    }
}
