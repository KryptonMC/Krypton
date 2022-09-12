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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.Climate
import org.kryptonmc.krypton.util.serialization.CompoundEncoder
import org.kryptonmc.krypton.util.serialization.encode
import org.kryptonmc.nbt.compound

@JvmRecord
data class KryptonBiome(private val key: Key, override val climate: Climate, override val effects: BiomeEffects) : Biome {

    override fun key(): Key = key

    override fun toBuilder(): Biome.Builder = Builder(this)

    class Builder(private var key: Key) : Biome.Builder {

        private var climate = KryptonClimate.DEFAULT
        private var effects = KryptonBiomeEffects.DEFAULT

        constructor(biome: Biome) : this(biome.key()) {
            climate = biome.climate
            effects = biome.effects
        }

        override fun key(key: Key): Builder = apply { this.key = key }

        override fun climate(climate: Climate): Builder = apply { this.climate = climate }

        override fun effects(effects: BiomeEffects): Builder = apply { this.effects = effects }

        override fun build(): KryptonBiome = KryptonBiome(key, climate, effects)
    }

    object Factory : Biome.Factory {

        override fun builder(key: Key): Builder = Builder(key)
    }

    companion object {

        @JvmField
        val ENCODER: CompoundEncoder<Biome> = CompoundEncoder {
            compound {
                encode(KryptonClimate.ENCODER, it.climate)
                encode(KryptonBiomeEffects.ENCODER, "effects", it.effects)
            }
        }
    }
}
