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
package org.kryptonmc.krypton.resource

import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.krypton.entity.memory.MemoryKey
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.event.GameEvent
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.generation.feature.Structure
import org.kryptonmc.krypton.world.generation.noise.NoiseGeneratorSettings

object InternalResourceKeys {

    @JvmField val MEMORIES = ResourceKeys.minecraft<MemoryKey<Any>>("memory_module_type")
    @JvmField val DIMENSION = ResourceKeys.minecraft<Dimension>("dimension")
    @JvmField val GAME_EVENT = ResourceKeys.minecraft<GameEvent>("game_event")

    // World generation resources
    @JvmField val STRUCTURE = ResourceKeys.minecraft<Structure<*>>("worldgen/structure_feature")
    @JvmField val NOISE_GENERATOR_SETTINGS = ResourceKeys.minecraft<NoiseGeneratorSettings>("worldgen/noise_settings")
    @JvmField val CHUNK_STATUS = ResourceKeys.minecraft<ChunkStatus>("chunk_status")
}
