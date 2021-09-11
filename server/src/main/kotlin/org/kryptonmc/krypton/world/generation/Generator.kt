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
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.world.biome.gen.BiomeGenerator
import org.kryptonmc.krypton.world.chunk.ChunkAccessor
import java.util.function.Function

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

    abstract val codec: Codec<out Generator>

    abstract fun buildSurface(region: GenerationRegion, chunk: ChunkAccessor)

    companion object {

        val CODEC: Codec<Generator> = InternalRegistries.GENERATOR.dispatchStable(Generator::codec, Function.identity())
    }
}
