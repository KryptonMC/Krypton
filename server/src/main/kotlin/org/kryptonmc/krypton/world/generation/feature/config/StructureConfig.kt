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
package org.kryptonmc.krypton.world.generation.feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.krypton.util.Codecs
import java.util.function.Function

@JvmRecord
data class StructureConfig(
    val spacing: Int,
    val separation: Int,
    val salt: Int
) {

    companion object {

        val CODEC: Codec<StructureConfig> = RecordCodecBuilder.create<StructureConfig> {
            it.group(
                Codec.intRange(0, 4096).fieldOf("spacing").forGetter(StructureConfig::spacing),
                Codec.intRange(0, 4096).fieldOf("separation").forGetter(StructureConfig::separation),
                Codecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(StructureConfig::salt)
            ).apply(it, ::StructureConfig)
        }.comapFlatMap({
            if (it.spacing <= it.separation) {
                DataResult.error("Spacing must be larger than separation!")
            } else {
                DataResult.success(it)
            }
        }, Function.identity())
    }
}
