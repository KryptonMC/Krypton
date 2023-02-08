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

import org.kryptonmc.api.util.Color
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.krypton.world.biome.data.KryptonAmbientMoodSettings
import org.kryptonmc.krypton.world.biome.data.KryptonClimate

object EndBiomes {

    private const val TEMPERATURE = 0.5F
    private const val DOWNFALL = 0.5F
    private val FOG = Color(160, 128, 160)

    @JvmStatic
    fun endBarrens(): KryptonBiome = baseEnd()

    @JvmStatic
    fun theEnd(): KryptonBiome = baseEnd()

    @JvmStatic
    fun endMidlands(): KryptonBiome = baseEnd()

    @JvmStatic
    fun endHighlands(): KryptonBiome = baseEnd()

    @JvmStatic
    fun smallEndIslands(): KryptonBiome = baseEnd()

    @JvmStatic
    private fun baseEnd(): KryptonBiome = KryptonBiome.Builder().apply {
        climate(KryptonClimate(Precipitation.NONE, TEMPERATURE, DOWNFALL, TemperatureModifier.NONE))
        effects {
            waterColor(OverworldBiomes.OVERWORLD_WATER)
            waterFogColor(OverworldBiomes.OVERWORLD_WATER_FOG)
            fogColor(FOG)
            skyColor(Color.BLACK)
            mood(KryptonAmbientMoodSettings.CAVE)
        }
    }.build()
}
