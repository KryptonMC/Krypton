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

import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.world.biome.Climate
import org.kryptonmc.krypton.world.biome.gen.BiomeGenerator
import org.kryptonmc.krypton.world.chunk.ChunkAccessor

abstract class Generator(
    val biomeGenerator: BiomeGenerator,
    protected val runtimeBiomeGenerator: BiomeGenerator,
    private val settings: StructureSettings,
    private val strongholdSeed: Long = 0L
) {

    constructor(
        biomeGenerator: BiomeGenerator,
        structures: StructureSettings
    ) : this(biomeGenerator, biomeGenerator, structures, 0L)

    abstract val climateSampler: Climate.Sampler

    abstract fun buildSurface(region: GenerationRegion, chunk: ChunkAccessor)

    fun biome(x: Int, y: Int, z: Int): Biome = biomeGenerator[x, y, z, climateSampler]
}
