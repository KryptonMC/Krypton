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
package org.kryptonmc.krypton.world.biome.gen

import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.world.biome.Climate
import org.spongepowered.math.vector.Vector3i
import java.util.Random
import kotlin.math.abs

sealed class BiomeGenerator(val possibleBiomes: List<Biome>) {

    abstract operator fun get(x: Int, y: Int, z: Int, sampler: Climate.Sampler): Biome

    open fun findBiomeHorizontal(
        x: Int,
        y: Int,
        z: Int,
        radius: Int,
        random: Random,
        sampler: Climate.Sampler,
        step: Int = 1,
        absolute: Boolean = false,
        predicate: (Biome) -> Boolean
    ): Vector3i? {
        val quartX = x shr 2
        val quartY = y shr 2
        val quartZ = z shr 2
        val quartRadius = radius shr 2
        var position: Vector3i? = null
        var h = 0
        val lower = if (absolute) 0 else quartRadius
        for (i in lower..quartRadius step step) {
            for (zo in -i..i step step) {
                val equal = abs(zo) == i
                for (xo in -i..i step step) {
                    if (absolute) {
                        val otherEqual = abs(xo) == i
                        if (!otherEqual && !equal) continue
                    }
                    val offX = quartX + xo
                    val offZ = quartZ + zo
                    if (predicate(get(offX, quartY, offZ, sampler))) {
                        if (position == null || random.nextInt(h + 1) == 0) {
                            position = Vector3i(offX shl 2, y, offZ shl 2)
                            if (absolute) return position
                        }
                        ++h
                    }
                }
            }
        }
        return position
    }
}
