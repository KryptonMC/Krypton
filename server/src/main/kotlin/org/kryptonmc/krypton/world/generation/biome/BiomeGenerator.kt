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
package org.kryptonmc.krypton.world.generation.biome

import com.mojang.serialization.Codec
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.world.biome.KryptonBiome
import java.util.function.Function
import java.util.function.Supplier

sealed class BiomeGenerator(val possibleBiomes: List<KryptonBiome>) {

    abstract val codec: Codec<out BiomeGenerator>

    constructor(possibleBiomes: Sequence<Supplier<KryptonBiome>>) : this(possibleBiomes.map { it.get() }.toList())

    companion object {

        val CODEC: Codec<BiomeGenerator> = InternalRegistries.BIOME_GENERATOR.dispatchStable(BiomeGenerator::codec, Function.identity())
    }
}
