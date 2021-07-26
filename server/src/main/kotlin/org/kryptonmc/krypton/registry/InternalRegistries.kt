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
package org.kryptonmc.krypton.registry

import com.mojang.serialization.Codec
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.world.biome.BiomeGenerator
import org.kryptonmc.krypton.world.generation.Generator
import org.kryptonmc.krypton.world.generation.feature.Feature
import org.kryptonmc.krypton.world.generation.feature.Structure

object InternalRegistries {

    val ATTRIBUTE = Registries.create(InternalResourceKeys.ATTRIBUTE)
    val METADATA = Registries.create(InternalResourceKeys.METADATA)
    val GAME_EVENT = Registries.create(InternalResourceKeys.GAME_EVENT)
    val FLUID = Registries.createDefaulted(InternalResourceKeys.FLUID, Key.key("empty"))
    val BIOME = Registries.create(InternalResourceKeys.BIOME)

    // World generation registries
    val GENERATOR = Registries.create(InternalResourceKeys.GENERATOR) as KryptonRegistry<Codec<out Generator>>
    val BIOME_GENERATOR = Registries.create(InternalResourceKeys.BIOME_GENERATOR) as KryptonRegistry<Codec<out BiomeGenerator>>
    val FEATURE = Registries.create(InternalResourceKeys.FEATURE) as KryptonRegistry<Feature<*>>
    val STRUCTURE = Registries.create(InternalResourceKeys.STRUCTURE) as KryptonRegistry<Structure<*>>
    val NOISE_GENERATOR_SETTINGS = Registries.create(InternalResourceKeys.NOISE_GENERATOR_SETTINGS)
    val CHUNK_STATUS = Registries.createDefaulted(InternalResourceKeys.CHUNK_STATUS, Key.key("empty"))
}
