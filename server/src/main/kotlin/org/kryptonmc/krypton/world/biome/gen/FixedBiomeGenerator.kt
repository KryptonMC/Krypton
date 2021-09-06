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
package org.kryptonmc.krypton.world.biome.gen

import com.mojang.serialization.Codec
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.spongepowered.math.vector.Vector3i
import java.util.Random

class FixedBiomeGenerator(private val biome: KryptonBiome) : BiomeGenerator(listOf(biome)) {

    override val codec = CODEC

    override fun get(x: Int, y: Int, z: Int) = biome

    override fun findBiomeHorizontal(
        x: Int,
        y: Int,
        z: Int,
        radius: Int,
        random: Random,
        step: Int,
        absolute: Boolean,
        predicate: (KryptonBiome) -> Boolean
    ): Vector3i? {
        if (predicate(biome)) {
            if (absolute) return Vector3i(x, y, z)
            return Vector3i(
                x - radius + random.nextInt(radius * 2 + 1),
                y,
                z - radius + random.nextInt(radius * 2 + 1)
            )
        }
        return null
    }

    companion object {

        val CODEC: Codec<FixedBiomeGenerator> = KryptonBiome.CODEC.fieldOf("biome").xmap(
            ::FixedBiomeGenerator,
            FixedBiomeGenerator::biome
        ).stable().codec()
    }
}
