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
package org.kryptonmc.krypton.server

import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.krypton.pack.resources.ResourceManager
import org.kryptonmc.krypton.pack.resources.reload.PreparableReloadListener
import org.kryptonmc.krypton.pack.resources.reload.SimpleReloadInstance
import org.kryptonmc.krypton.registry.dynamic.RegistryAccess
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.tags.KryptonTagKey
import org.kryptonmc.krypton.tags.TagManager
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.flag.FeatureFlagSet
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class ReloadableResources(registryAccess: RegistryAccess.Frozen, featureFlags: FeatureFlagSet, functionCompilationLevel: Int) {

    private val tagManager = TagManager(registryAccess)

    fun listeners(): List<PreparableReloadListener> = ImmutableLists.of(tagManager)

    fun updateRegistryTags(registryAccess: RegistryAccess) {
        tagManager.result().forEach { Companion.updateRegistryTags(registryAccess, it) }
        KryptonBlocks.rebuildCaches()
    }

    companion object {

        private val DATA_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit)

        @JvmStatic
        fun loadResources(resourceManager: ResourceManager, registryAccess: RegistryAccess.Frozen, featureFlags: FeatureFlagSet,
                          functionCompilationLevel: Int, backgroundExecutor: Executor,
                          mainExecutor: Executor): CompletableFuture<ReloadableResources> {
            val resources = ReloadableResources(registryAccess, featureFlags, functionCompilationLevel)
            return SimpleReloadInstance.create(resourceManager, resources.listeners(), backgroundExecutor, mainExecutor, DATA_RELOAD_INITIAL_TASK)
                .done()
                .whenComplete { _, _ -> /* TODO: Set missing access policy for commands */ }
                .thenApply { resources }
        }

        @JvmStatic
        private fun <T> updateRegistryTags(registryAccess: RegistryAccess, result: TagManager.LoadResult<T>) {
            val tags: Map<TagKey<T>, List<Holder<T>>> = result.tags.entries.associate { (key, value) ->
                KryptonTagKey.of(result.key, key) to ImmutableLists.copyOf(value)
            }
            registryAccess.registryOrThrow(result.key).bindTags(tags)
        }
    }
}
