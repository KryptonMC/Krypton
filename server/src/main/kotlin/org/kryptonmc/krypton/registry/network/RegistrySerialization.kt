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
package org.kryptonmc.krypton.registry.network

import com.google.common.collect.ImmutableMap
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.registry.RegistryRoots
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.dynamic.CombinedRegistryHolder
import org.kryptonmc.krypton.registry.dynamic.FilteredRegistryHolder
import org.kryptonmc.krypton.registry.dynamic.SimpleRegistryHolder
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.resultOrError
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.serialization.codecs.UnboundedMapCodec

object RegistrySerialization {

    private val NETWORKABLE_REGISTRIES = ImmutableMap.builder<ResourceKey<out Registry<*>>, NetworkedRegistryData<*>>().apply {
        put(this, ResourceKeys.BIOME, KryptonBiome.NETWORK_CODEC)
        put(this, KryptonResourceKeys.CHAT_TYPE, RichChatType.CODEC)
        put(this, KryptonResourceKeys.DIMENSION_TYPE, KryptonDimensionType.DIRECT_CODEC)
    }.build()
    @JvmField
    val NETWORK_CODEC: Codec<RegistryHolder> = createNetworkCodec<Any>()

    @JvmStatic
    private fun <E> put(map: ImmutableMap.Builder<ResourceKey<out Registry<*>>, NetworkedRegistryData<*>>, key: ResourceKey<out Registry<E>>,
                        networkCodec: Codec<E>) {
        map.put(key, NetworkedRegistryData(key, networkCodec))
    }

    @JvmStatic
    private fun ownedNetworkableRegistries(holder: RegistryHolder): RegistryHolder {
        return FilteredRegistryHolder(holder) { NETWORKABLE_REGISTRIES.containsKey(it) }
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <E> getNetworkCodec(key: ResourceKey<out Registry<E>>): DataResult<out Codec<E>> {
        return NETWORKABLE_REGISTRIES.get(key)?.networkCodec
            .resultOrError { "Unknown or not serializable registry $key!" } as DataResult<out Codec<E>>
    }

    @JvmStatic
    private fun <E> createNetworkCodec(): Codec<RegistryHolder> {
        val keyCodec: Codec<ResourceKey<out Registry<E>>> = Keys.CODEC.xmap({ KryptonResourceKey.of(RegistryRoots.MINECRAFT, it) }, { it.location })
        val registryCodec: Codec<KryptonRegistry<E>> = keyCodec.partialDispatch(
            "type",
            { DataResult.success(it.key) },
            { key -> getNetworkCodec(key).map { RegistryCodecs.network(key, it) } }
        )
        val mapCodec = Codec.map(keyCodec, registryCodec) as UnboundedMapCodec<out ResourceKey<out Registry<*>>, out KryptonRegistry<*>>
        return captureMap(mapCodec)
    }

    @JvmStatic
    private fun <K : ResourceKey<out Registry<*>>, V : KryptonRegistry<*>> captureMap(codec: UnboundedMapCodec<K, V>): Codec<RegistryHolder> {
        return codec.xmap(
            { registries -> SimpleRegistryHolder(registries) },
            { holder ->
                @Suppress("UNCHECKED_CAST")
                holder.registries.associate { it.key as K to it as V }
            }
        )
    }

    @JvmStatic
    fun networkedRegistries(holder: RegistryHolder): RegistryHolder = ownedNetworkableRegistries(holder)

    @JvmStatic
    fun networkSafeRegistries(dynamic: RegistryHolder): RegistryHolder {
        val statics = KryptonRegistries.StaticHolder
        val networked = networkedRegistries(dynamic)
        return CombinedRegistryHolder(statics, networked)
    }

    @JvmRecord
    private data class NetworkedRegistryData<E>(val key: ResourceKey<out Registry<E>>, val networkCodec: Codec<E>)
}
