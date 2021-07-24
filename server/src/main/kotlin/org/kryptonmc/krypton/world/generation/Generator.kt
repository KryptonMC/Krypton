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
package org.kryptonmc.krypton.world.generation

import com.mojang.serialization.Dynamic
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.util.toKey
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

abstract class Generator(val id: Key) {

    abstract fun toNBT(): CompoundTag
}

// we want to use this in the constructor, but if we use a property, it's not initialised
// by the time we need it
private val DEBUG_GENERATOR_ID = key("debug")

object DebugGenerator : Generator(DEBUG_GENERATOR_ID) {

    override fun toNBT() = compound { string("type", DEBUG_GENERATOR_ID.toString()) }
}

fun Dynamic<*>.toGenerator() = when (val type = get("type").asString("").toKey()) {
    FlatGenerator.ID -> FlatGenerator(FlatGeneratorSettings.of(get("settings").orElseEmptyMap()))
    NoiseGenerator.ID -> NoiseGenerator(
        get("seed").asInt(0),
        get("settings").asString("").toKey(),
        BiomeGenerator.of(get("biome_source").orElseEmptyMap())
    )
    DEBUG_GENERATOR_ID -> DebugGenerator
    else -> throw IllegalArgumentException("Unsupported generator type $type")
}
