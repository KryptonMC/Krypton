/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.generation

import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey

abstract class Generator(val id: NamespacedKey) {

    abstract fun toNBT(): CompoundBinaryTag
}

// we want to use this in the constructor, but if we use a property, it's not initialised
// by the time we need it
private val DEBUG_GENERATOR_ID = NamespacedKey(value = "debug")

object DebugGenerator : Generator(DEBUG_GENERATOR_ID) {

    override fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", DEBUG_GENERATOR_ID.toString())
        .build()
}

// TODO: Add support for generators when world generation exists
fun CompoundBinaryTag.toGenerator() = when (val type = getString("type").toNamespacedKey()) {
    FlatGenerator.ID -> FlatGenerator(FlatGeneratorSettings.fromNBT(getCompound("settings")))
    NoiseGenerator.ID -> NoiseGenerator(
        getInt("seed"),
        getString("settings").toNamespacedKey(),
        BiomeGenerator.fromNBT(getCompound("biome_source"))
    )
    DEBUG_GENERATOR_ID -> DebugGenerator
    else -> throw IllegalArgumentException("Unsupported generator type $type")
}
