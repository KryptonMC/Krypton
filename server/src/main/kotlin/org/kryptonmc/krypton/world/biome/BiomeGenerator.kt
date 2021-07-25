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
package org.kryptonmc.krypton.world.biome

import com.mojang.serialization.Codec
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries
import java.util.function.Function

sealed class BiomeGenerator(val possibleBiomes: List<KryptonBiome>) {

    abstract val codec: Codec<out BiomeGenerator>

    constructor(possibleBiomes: Sequence<() -> KryptonBiome>) : this(possibleBiomes.map { it() }.toList())

    companion object {

        val CODEC: Codec<BiomeGenerator> = InternalRegistries.BIOME_GENERATOR.dispatchStable(BiomeGenerator::codec, Function.identity())

        init {
            Registries.register(InternalRegistries.BIOME_GENERATOR, "fixed", FixedBiomeGenerator.CODEC)
            Registries.register(InternalRegistries.BIOME_GENERATOR, "multi_noise", MultiNoiseBiomeGenerator.CODEC)
            Registries.register(InternalRegistries.BIOME_GENERATOR, "checkerboard", CheckerboardBiomeGenerator.CODEC)
            Registries.register(InternalRegistries.BIOME_GENERATOR, "vanilla_layered", VanillaLayeredBiomeGenerator.CODEC)
            Registries.register(InternalRegistries.BIOME_GENERATOR, "the_end", TheEndBiomeGenerator.CODEC)
        }
    }
}
