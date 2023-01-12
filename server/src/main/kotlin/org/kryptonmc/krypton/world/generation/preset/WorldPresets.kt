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
package org.kryptonmc.krypton.world.generation.preset

import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys

object WorldPresets {

    @JvmField
    val NORMAL: ResourceKey<WorldPreset> = register("normal")
    @JvmField
    val FLAT: ResourceKey<WorldPreset> = register("flat")
    @JvmField
    val LARGE_BIOMES: ResourceKey<WorldPreset> = register("large_biomes")
    @JvmField
    val AMPLIFIED: ResourceKey<WorldPreset> = register("amplified")
    @JvmField
    val SINGLE_BIOME_SURFACE: ResourceKey<WorldPreset> = register("single_biome_surface")
    @JvmField
    val DEBUG: ResourceKey<WorldPreset> = register("debug_all_block_states")

    @JvmStatic
    private fun register(name: String): ResourceKey<WorldPreset> = KryptonResourceKey.of(KryptonResourceKeys.WORLD_PRESET, Key.key(name))
}
