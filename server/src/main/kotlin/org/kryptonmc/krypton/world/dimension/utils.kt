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
package org.kryptonmc.krypton.world.dimension

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.generation.noise.NoiseGeneratorSettings

fun DimensionType.equalTo(other: DimensionType): Boolean {
    if (this === other) return true
    return hasSkylight == other.hasSkylight && hasCeiling == other.hasCeiling && isUltrawarm == other.isUltrawarm &&
            isNatural == other.isNatural && coordinateScale == other.coordinateScale && isPiglinSafe == other.isPiglinSafe &&
            bedWorks == other.bedWorks && respawnAnchorWorks == other.respawnAnchorWorks && hasRaids == other.hasRaids &&
            minimumY == other.minimumY && height == other.height && logicalHeight == other.logicalHeight &&
            ambientLight == other.ambientLight && fixedTime == other.fixedTime && infiniburn == other.infiniburn &&
            effects == other.effects
}

fun Registry<DimensionType>.defaults(biomes: Registry<KryptonBiome>, noiseSettings: Registry<NoiseGeneratorSettings>) = KryptonRegistry(InternalResourceKeys.DIMENSION).apply {
    // TODO: When noise generator exists, add nether and end dimensions
}
