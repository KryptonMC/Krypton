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
package org.kryptonmc.krypton.tags

import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.resource.ResourceManager
import org.kryptonmc.krypton.resource.reload.ReloadListener
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.profiling.Profiler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class TagManager(private val registryHolder: RegistryHolder) : ReloadListener {

    var tags = TagContainer.EMPTY
        private set

    override fun reload(barrier: ReloadListener.Barrier, manager: ResourceManager, preparationProfiler: Profiler, reloadProfiler: Profiler, executor: Executor, syncExecutor: Executor): CompletableFuture<Void> {
        val infos = mutableListOf<LoaderInfo<*>>()
        StaticTags.visitHelpers { helper -> createLoader(manager, executor, helper)?.let { infos.add(it) } }
        return CompletableFuture.allOf(*infos.map { it.pendingLoad }.toTypedArray()).thenCompose(barrier::wait).thenAcceptAsync({
            val builder = TagContainer.Builder()
            infos.forEach { it.addToBuilder(builder) }
            val container = builder.build()
            val missingTags = StaticTags.missing(container)
            check(missingTags.isEmpty) { "Missing required tags: ${missingTags.entries().asSequence().map { "${it.key}:${it.value}" }.sorted().joinToString()}" }
            tags = container
        }, syncExecutor)
    }

    private fun <T : Any> createLoader(manager: ResourceManager, executor: Executor, helper: StaticTagHelper<T>): LoaderInfo<T>? {
        val registry = registryHolder.registry(helper.key) ?: kotlin.run {
            LOGGER.warn("Failed to find registry for ${helper.key}!")
            return null
        }
        val loader = TagLoader(registry::get, helper.directory)
        val pendingLoad = CompletableFuture.supplyAsync({ loader.loadAndBuild(manager) }, executor)
        return LoaderInfo(helper, pendingLoad)
    }

    class LoaderInfo<T : Any>(val helper: StaticTagHelper<T>, val pendingLoad: CompletableFuture<out TagCollection<T>>) {

        fun addToBuilder(builder: TagContainer.Builder) {
            builder.add(helper.key, pendingLoad.join())
        }
    }

    companion object {

        private val LOGGER = logger<TagManager>()
    }
}
