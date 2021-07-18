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
package org.kryptonmc.krypton.registry

import me.bardy.gsonkt.fromJson
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.registry.biomes.BiomeRegistry
import org.kryptonmc.krypton.registry.dimensions.DimensionRegistry

/**
 * Singleton for all of the registries that we load from files. This is a singleton for ease of use
 *
 * @author Callum Seabrook
 */
// TODO: Replace with new registry system
object FileRegistries {

    val BIOMES = GSON.fromJson<BiomeRegistry>(registryData("registries/custom/biomes.json"))
    val DIMENSIONS = GSON.fromJson<DimensionRegistry>(registryData("registries/custom/dimensions.json"))
}

private fun registryData(path: String) = (Thread.currentThread().contextClassLoader.getResourceAsStream(path)
    ?: error("$path not on classpath! Something has gone horribly wrong!"))
    .reader()
    .readText()
