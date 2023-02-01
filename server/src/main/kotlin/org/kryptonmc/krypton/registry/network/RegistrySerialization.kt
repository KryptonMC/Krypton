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
package org.kryptonmc.krypton.registry.network

import com.google.common.collect.ImmutableMap
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryRoots
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.dynamic.LayeredRegistryAccess
import org.kryptonmc.krypton.registry.dynamic.RegistryAccess
import org.kryptonmc.krypton.registry.dynamic.RegistryLayer
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.resultOrError
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.serialization.codecs.UnboundedMapCodec
import java.util.stream.Stream

object RegistrySerialization {

    private val NETWORKABLE_REGISTRIES = ImmutableMap.builder<ResourceKey<out Registry<*>>, NetworkedRegistryData<*>>().apply {
        put(this, ResourceKeys.BIOME, KryptonBiome.NETWORK_CODEC)
        put(this, KryptonResourceKeys.CHAT_TYPE, RichChatType.CODEC)
        put(this, KryptonResourceKeys.DIMENSION_TYPE, KryptonDimensionType.DIRECT_CODEC)
    }.build()
    @JvmField
    val NETWORK_CODEC: Codec<RegistryAccess> = createNetworkCodec<Any>()

    @JvmStatic
    private fun <E> put(map: ImmutableMap.Builder<ResourceKey<out Registry<*>>, NetworkedRegistryData<*>>, key: ResourceKey<out Registry<E>>,
                        networkCodec: Codec<E>) {
        map.put(key, NetworkedRegistryData(key, networkCodec))
    }

    @JvmStatic
    private fun ownedNetworkableRegistries(access: RegistryAccess): Stream<RegistryAccess.RegistryEntry<*>> =
        access.registries().filter { NETWORKABLE_REGISTRIES.containsKey(it.key) }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <E> getNetworkCodec(key: ResourceKey<out Registry<E>>): DataResult<out Codec<E>> =
        NETWORKABLE_REGISTRIES.get(key)?.networkCodec.resultOrError { "Unknown or not serializable registry $key!" } as DataResult<out Codec<E>>

    @JvmStatic
    private fun <E> createNetworkCodec(): Codec<RegistryAccess> {
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
    private fun <K : ResourceKey<out Registry<*>>, V : KryptonRegistry<*>> captureMap(codec: UnboundedMapCodec<K, V>): Codec<RegistryAccess> {
        return codec.xmap(
            { RegistryAccess.ImmutableImpl(it) },
            { access ->
                @Suppress("UNCHECKED_CAST")
                ownedNetworkableRegistries(access).collect(ImmutableMap.toImmutableMap({ it.key as K }, { it.value as V }))
            }
        )
    }

    @JvmStatic
    fun networkedRegistries(access: LayeredRegistryAccess<RegistryLayer>): Stream<RegistryAccess.RegistryEntry<*>> =
        ownedNetworkableRegistries(access.getAccessFrom(RegistryLayer.NETWORK))

    @JvmStatic
    fun networkSafeRegistries(access: LayeredRegistryAccess<RegistryLayer>): Stream<RegistryAccess.RegistryEntry<*>> {
        val statics = access.getLayer(RegistryLayer.STATIC).registries()
        val others = networkedRegistries(access)
        return Stream.concat(others, statics)
    }

    @JvmRecord
    private data class NetworkedRegistryData<E>(val key: ResourceKey<out Registry<E>>, val networkCodec: Codec<E>)
}
