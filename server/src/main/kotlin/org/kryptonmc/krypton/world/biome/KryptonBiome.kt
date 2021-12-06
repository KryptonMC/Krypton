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
import net.kyori.adventure.key.Key
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeCategories
import org.kryptonmc.api.world.biome.BiomeCategory
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.Climate

@JvmRecord
data class KryptonBiome(
    private val key: Key,
    override val climate: Climate,
    override val category: BiomeCategory,
    override val effects: BiomeEffects
) : Biome {

    override fun key(): Key = key

    override fun toBuilder(): Biome.Builder = Builder(this)

    class Builder(private var key: Key) : Biome.Builder {

        private var climate = KryptonClimate.DEFAULT
        private var category = BiomeCategories.NONE
        private var effects = KryptonBiomeEffects.DEFAULT

        constructor(biome: Biome) : this(biome.key()) {
            climate = biome.climate
            category = biome.category
            effects = biome.effects
        }

        override fun key(key: Key): Biome.Builder = apply { this.key = key }

        override fun climate(climate: Climate): Biome.Builder = apply { this.climate = climate }

        override fun category(category: BiomeCategory): Biome.Builder = apply { this.category = category }

        override fun effects(effects: BiomeEffects): Biome.Builder = apply { this.effects = effects }

        override fun build(): Biome = KryptonBiome(key, climate, category, effects)
    }

    object Factory : Biome.Factory {

        override fun of(
            key: Key,
            climate: Climate,
            depth: Float,
            scale: Float,
            category: BiomeCategory,
            effects: BiomeEffects
        ): Biome = KryptonBiome(key, climate, category, effects)

        override fun builder(key: Key): Biome.Builder = Builder(key)
    }

    companion object {

        @JvmField
        val CODEC: Codec<Biome> = RecordCodecBuilder.create {
            it.group(
                KryptonClimate.CODEC.forGetter(Biome::climate),
                KryptonBiomeCategory.CODEC.fieldOf("category").forGetter(Biome::category),
                KryptonBiomeEffects.CODEC.fieldOf("effects").forGetter(Biome::effects)
            ).apply(it) { _, _, _ -> error("Cannot decode biomes!") }
        }
    }
}
