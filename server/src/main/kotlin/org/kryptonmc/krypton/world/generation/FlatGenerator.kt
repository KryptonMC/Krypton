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

import com.mojang.serialization.Codec
import org.kryptonmc.krypton.world.biome.Climate
import org.kryptonmc.krypton.world.biome.KryptonBiomes
import org.kryptonmc.krypton.world.biome.gen.FixedBiomeGenerator
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import org.kryptonmc.krypton.world.generation.flat.FlatGeneratorSettings

// FIXME: Get the biomes from the settings
class FlatGenerator(val settings: FlatGeneratorSettings) : Generator(
    FixedBiomeGenerator(KryptonBiomes.PLAINS),
    FixedBiomeGenerator(settings.biome),
    settings.structureSettings
) {

    override val codec: Codec<out Generator> = CODEC
    override val climateSampler: Climate.Sampler = Climate.Sampler { _, _, _ -> Climate.TargetPoint.ZERO }

    override fun buildSurface(region: GenerationRegion, chunk: ChunkAccessor) {
        // no surface to build for flat generator... yet
    }

    companion object {

        @JvmField
        val CODEC: Codec<FlatGenerator> = FlatGeneratorSettings.CODEC.fieldOf("settings")
            .xmap(::FlatGenerator, FlatGenerator::settings)
            .codec()
    }
}
