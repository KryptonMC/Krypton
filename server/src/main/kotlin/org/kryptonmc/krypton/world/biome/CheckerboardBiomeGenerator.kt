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
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.function.Supplier

class CheckerboardBiomeGenerator(
    private val allowedBiomes: List<Supplier<KryptonBiome>>,
    private val scale: Int
) : BiomeGenerator(allowedBiomes.asSequence()) {

    override val codec = CODEC

    companion object {

        val CODEC: Codec<CheckerboardBiomeGenerator> = RecordCodecBuilder.create {
            it.group(
                KryptonBiome.LIST_CODEC.fieldOf("biomes").forGetter(CheckerboardBiomeGenerator::allowedBiomes),
                Codec.intRange(0, 62).fieldOf("scale").orElse(2).forGetter(CheckerboardBiomeGenerator::scale)
            ).apply(it, ::CheckerboardBiomeGenerator)
        }
    }
}
