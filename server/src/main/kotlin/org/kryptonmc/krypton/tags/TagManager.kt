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
package org.kryptonmc.krypton.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.pack.resources.ResourceManager
import org.kryptonmc.krypton.pack.resources.reload.PreparableReloadListener
import org.kryptonmc.krypton.registry.dynamic.RegistryAccess
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.ImmutableMaps
import org.kryptonmc.krypton.util.NoSpread
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.stream.Collectors

class TagManager(private val registryAccess: RegistryAccess) : PreparableReloadListener {

    private var results: List<LoadResult<*>> = ImmutableLists.of()

    fun result(): List<LoadResult<*>> = results

    override fun reload(barrier: PreparableReloadListener.PreparationBarrier, manager: ResourceManager, backgroundExecutor: Executor,
                        mainExecutor: Executor): CompletableFuture<Void> {
        val resultFutures = registryAccess.registries().map { createLoader(manager, backgroundExecutor, it) }.toList()
        return NoSpread.completableFutureAllOf(resultFutures.toTypedArray())
            .thenCompose { barrier.wait(it) }
            .thenAcceptAsync({ results = resultFutures.stream().map { it.join() }.collect(Collectors.toUnmodifiableList()) }, mainExecutor)
    }

    private fun <T> createLoader(resourceManager: ResourceManager, backgroundExecutor: Executor,
                                 registryEntry: RegistryAccess.RegistryEntry<T>): CompletableFuture<LoadResult<T>> {
        val (key, registry) = registryEntry
        val loader = TagLoader<Holder<T>>({ registry.getHolder(KryptonResourceKey.of(key, it)) }, getTagDirectory(key))
        return CompletableFuture.supplyAsync({ LoadResult(key, loader.loadAndBuild(resourceManager)) }, backgroundExecutor)
    }

    @JvmRecord
    data class LoadResult<T>(val key: ResourceKey<out Registry<T>>, val tags: Map<Key, Collection<Holder<T>>>)

    companion object {

        private val CUSTOM_REGISTRY_DIRECTORIES = ImmutableMaps.of(
            KryptonResourceKeys.BLOCK, "tags/blocks",
            KryptonResourceKeys.ENTITY_TYPE, "tags/entity_types",
            KryptonResourceKeys.FLUID, "tags/fluids",
            KryptonResourceKeys.GAME_EVENT, "tags/game_events",
            KryptonResourceKeys.ITEM, "tags/items"
        )

        @JvmStatic
        fun getTagDirectory(key: ResourceKey<out Registry<*>>): String = CUSTOM_REGISTRY_DIRECTORIES.get(key) ?: "tags/${key.location.value()}"
    }
}
