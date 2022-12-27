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
package org.kryptonmc.krypton.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.pack.resources.PreparableReloadListener
import org.kryptonmc.krypton.pack.resources.ResourceManager
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
