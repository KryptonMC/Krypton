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
package org.kryptonmc.krypton.world.biome

import net.kyori.adventure.key.Key
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.world.biome.Climate
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.api.world.biome.biome

object EndBiomes {

    private const val TEMPERATURE = 0.5F
    private const val DOWNFALL = 0.5F
    private val FOG = Color.of(160, 128, 160)

    @JvmStatic
    fun endBarrens(key: Key): KryptonBiome = baseEnd(key)

    @JvmStatic
    fun theEnd(key: Key): KryptonBiome = baseEnd(key)

    @JvmStatic
    fun endMidlands(key: Key): KryptonBiome = baseEnd(key)

    @JvmStatic
    fun endHighlands(key: Key): KryptonBiome = baseEnd(key)

    @JvmStatic
    fun smallEndIslands(key: Key): KryptonBiome = baseEnd(key)

    @JvmStatic
    private fun baseEnd(key: Key): KryptonBiome = biome(key) {
        climate(Climate.of(Precipitation.NONE, TEMPERATURE, DOWNFALL, TemperatureModifier.NONE))
        effects {
            waterColor(OverworldBiomes.OVERWORLD_WATER)
            waterFogColor(OverworldBiomes.OVERWORLD_WATER_FOG)
            fogColor(FOG)
            skyColor(Color.BLACK)
            mood(KryptonAmbientMoodSettings.CAVE)
        }
    } as KryptonBiome
}
