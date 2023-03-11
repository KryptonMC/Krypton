/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
