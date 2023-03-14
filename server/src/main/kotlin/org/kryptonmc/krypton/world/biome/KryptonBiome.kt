/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.biome

import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.Climate
import org.kryptonmc.krypton.registry.KryptonDynamicRegistries
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.registry.holder.HolderSet
import org.kryptonmc.krypton.registry.network.RegistryCodecs
import org.kryptonmc.krypton.registry.network.RegistryFileCodec
import org.kryptonmc.krypton.world.biome.data.KryptonBiomeEffects
import org.kryptonmc.krypton.world.biome.data.KryptonClimate
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

class KryptonBiome(override val climate: Climate, override val effects: BiomeEffects) : Biome {

    override fun key(): Key = KryptonDynamicRegistries.BIOME.getKey(this) ?: UNREGISTERED_KEY

    class Builder : Biome.Builder {

        private var climate = KryptonClimate.DEFAULT
        private var effects = KryptonBiomeEffects.DEFAULT

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
            ).apply(instance, ::KryptonBiome)
        }
        @JvmField
        val NETWORK_CODEC: Codec<Biome> = RecordCodecBuilder.create { instance ->
            instance.group(
                KryptonClimate.CODEC.getting { it.climate },
                KryptonBiomeEffects.CODEC.fieldOf("effects").getting { it.effects }
            ).apply(instance, ::KryptonBiome)
        }
        @JvmField
        val CODEC: Codec<Holder<Biome>> = RegistryFileCodec.create(ResourceKeys.BIOME, DIRECT_CODEC)
        @JvmField
        val LIST_CODEC: Codec<HolderSet<Biome>> = RegistryCodecs.homogenousList(ResourceKeys.BIOME, DIRECT_CODEC)
    }
}
