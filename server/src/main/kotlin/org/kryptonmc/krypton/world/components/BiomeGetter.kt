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
package org.kryptonmc.krypton.world.components

import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeContainer
import org.kryptonmc.krypton.util.Quart
import org.kryptonmc.krypton.world.biome.BiomeManager
import org.kryptonmc.krypton.world.biome.NoiseBiomeSource
import org.kryptonmc.krypton.world.chunk.ChunkStatus

interface BiomeGetter : ChunkGetter, BiomeContainer, NoiseBiomeSource {

    val biomeManager: BiomeManager

    fun getUncachedNoiseBiome(x: Int, y: Int, z: Int): Biome

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome =
        getChunk(Quart.toSection(x), Quart.toSection(z), ChunkStatus.BIOMES, false)?.getNoiseBiome(x, y, z) ?: getUncachedNoiseBiome(x, y, z)

    override fun getBiome(x: Int, y: Int, z: Int): Biome = biomeManager.getBiome(x, y, z)

    override fun getBiome(position: Vec3i): Biome = getBiome(position.x, position.y, position.z)
}
