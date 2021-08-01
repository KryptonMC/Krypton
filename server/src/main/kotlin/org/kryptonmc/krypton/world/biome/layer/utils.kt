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
package org.kryptonmc.krypton.world.biome.layer

import org.kryptonmc.krypton.world.biome.BiomeConstants

fun Int.isOcean() = this == BiomeConstants.OCEAN ||
        this == BiomeConstants.FROZEN_OCEAN ||
        this == BiomeConstants.DEEP_OCEAN ||
        this == BiomeConstants.WARM_OCEAN ||
        this == BiomeConstants.LUKEWARM_OCEAN ||
        this == BiomeConstants.COLD_OCEAN ||
        this == BiomeConstants.DEEP_WARM_OCEAN ||
        this == BiomeConstants.DEEP_LUKEWARM_OCEAN ||
        this == BiomeConstants.DEEP_COLD_OCEAN ||
        this == BiomeConstants.DEEP_FROZEN_OCEAN

fun Int.isShallowOcean() = this == BiomeConstants.OCEAN ||
        this == BiomeConstants.FROZEN_OCEAN ||
        this == BiomeConstants.WARM_OCEAN ||
        this == BiomeConstants.LUKEWARM_OCEAN ||
        this == BiomeConstants.COLD_OCEAN

fun Int.isSame(other: Int) = if (this == other) true else this == other // FIXME - should check categories
