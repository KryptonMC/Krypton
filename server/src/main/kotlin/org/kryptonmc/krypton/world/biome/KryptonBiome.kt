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
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

class KryptonBiome(override val climate: Climate, override val effects: BiomeEffects) : Biome {

    override fun key(): Key = KryptonRegistries.BIOME.getKey(this) ?: UNREGISTERED_KEY

    override fun toBuilder(): Biome.Builder = Builder(this)

    class Builder() : Biome.Builder {

        private var climate = KryptonClimate.DEFAULT
        private var effects = KryptonBiomeEffects.DEFAULT

        constructor(biome: Biome) : this() {
            climate = biome.climate
            effects = biome.effects
        }

        override fun climate(climate: Climate): Builder = apply { this.climate = climate }

        inline fun climate(builder: Climate.Builder.() -> Unit): Builder = climate(KryptonClimate.Builder().apply(builder).build())

        override fun effects(effects: BiomeEffects): Builder = apply { this.effects = effects }

        inline fun effects(builder: BiomeEffects.Builder.() -> Unit): Builder = effects(KryptonBiomeEffects.Builder().apply(builder).build())

        override fun build(): KryptonBiome = KryptonBiome(climate, effects)
    }

    object Factory : Biome.Factory {

        override fun builder(): Builder = Builder()
    }

    companion object {

        private val UNREGISTERED_KEY = Key.key("krypton", "unregistered_biome")

        // When we add mob spawn and biome generation settings, this will actually differ from the network codec
        @JvmField
        val DIRECT_CODEC: Codec<Biome> = RecordCodecBuilder.create { instance ->
            instance.group(
                KryptonClimate.CODEC.getting { it.climate },
                KryptonBiomeEffects.CODEC.fieldOf("effects").getting { it.effects }
            ).apply(instance) { climate, effects -> KryptonBiome(climate, effects) }
        }
        @JvmField
        val NETWORK_CODEC: Codec<Biome> = RecordCodecBuilder.create { instance ->
            instance.group(
                KryptonClimate.CODEC.getting { it.climate },
                KryptonBiomeEffects.CODEC.fieldOf("effects").getting { it.effects }
            ).apply(instance) { climate, effects -> KryptonBiome(climate, effects) }
        }
    }
}
