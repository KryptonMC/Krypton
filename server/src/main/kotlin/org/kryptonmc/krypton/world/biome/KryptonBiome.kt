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
package org.kryptonmc.krypton.world.biome

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeCategory
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.Climate

@JvmRecord
data class KryptonBiome(
    override val climate: Climate,
    override val depth: Float,
    override val scale: Float,
    override val category: BiomeCategory,
    override val effects: BiomeEffects
) : Biome {

    object Factory : Biome.Factory {

        override fun of(
            climate: Climate,
            depth: Float,
            scale: Float,
            category: BiomeCategory,
            effects: BiomeEffects
        ): Biome = KryptonBiome(climate, depth, scale, category, effects)
    }

    companion object {

        // TODO: Add the network codec (when there is generation and mob spawn settings for the direct codec)
        val CODEC: Codec<Biome> = RecordCodecBuilder.create {
            it.group(
                KryptonClimate.CODEC.forGetter(Biome::climate),
                Codec.FLOAT.fieldOf("depth").forGetter(Biome::depth),
                Codec.FLOAT.fieldOf("scale").forGetter(Biome::scale),
                KryptonBiomeCategory.CODEC.fieldOf("category").forGetter(Biome::category),
                KryptonBiomeEffects.CODEC.fieldOf("effects").forGetter(Biome::effects)
            ).apply(it, ::KryptonBiome)
        }
    }
}
