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

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.biome.KryptonBiomes
import org.kryptonmc.krypton.world.biome.area.AreaFactory
import org.kryptonmc.krypton.world.biome.area.LazyArea

class Layer(factory: AreaFactory<LazyArea>) {

    private val area = factory.create()

    operator fun get(biomes: Registry<KryptonBiome>, x: Int, z: Int): KryptonBiome {
        val biomeId = area[x, z]
        val biomeKey = KryptonBiomes.ID_TO_KEY[biomeId] ?: error("Unknown biome ID $biomeId! (emitted by layers)")
        val biome = biomes[biomeKey]
        return if (biome == null) {
            LOGGER.error("Unknown biome ID $biomeId!")
            biomes[KryptonBiomes.ID_TO_KEY[0]!!]!!
        } else biome
    }

    companion object {

        private val LOGGER = logger<Layer>()
    }
}
