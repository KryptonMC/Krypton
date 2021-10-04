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

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.nullableFieldOf
import org.kryptonmc.krypton.world.generation.feature.Structure
import org.kryptonmc.krypton.world.generation.feature.config.StrongholdConfig
import org.kryptonmc.krypton.world.generation.feature.config.StructureConfig
import java.util.Optional

@JvmRecord
data class StructureSettings(
    val structures: Map<Structure<*>, StructureConfig>,
    val stronghold: StrongholdConfig?
) {

    constructor(default: Boolean) : this(DEFAULTS, if (default) DEFAULT_STRONGHOLD else null)

    companion object {

        val CODEC: Codec<StructureSettings> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.simpleMap(InternalRegistries.STRUCTURE, StructureConfig.CODEC, InternalRegistries.STRUCTURE)
                    .fieldOf("structures")
                    .forGetter(StructureSettings::structures),
                StrongholdConfig.CODEC.nullableFieldOf("stronghold").forGetter(StructureSettings::stronghold)
            ).apply(instance, ::StructureSettings)
        }
        val DEFAULTS = emptyMap<Structure<*>, StructureConfig>()
        val DEFAULT_STRONGHOLD = StrongholdConfig(32, 3, 128)
    }
}
