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
package org.kryptonmc.krypton.resource.reload

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.resource.FallbackResourceManager
import org.kryptonmc.krypton.resource.Resource
import org.kryptonmc.krypton.resource.ResourceManager
import org.kryptonmc.krypton.util.logger
import java.io.FileNotFoundException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class ReloadableResourceManager : ResourceManager, AutoCloseable {

    private val namespacedPacks = mutableMapOf<String, FallbackResourceManager>()
    private val listeners = mutableListOf<ReloadListener>()
    private val packList = mutableListOf<PackResources>()
    override val namespaces = mutableSetOf<String>()

    fun add(pack: PackResources) {
        packList.add(pack)
        pack.namespaces.forEach {
            namespaces.add(it)
            namespacedPacks.getOrPut(it) { FallbackResourceManager(it) }.add(pack)
        }
    }

    fun registerListener(listener: ReloadListener) = listeners.add(listener)

    fun reload(executor: Executor, syncExecutor: Executor, task: CompletableFuture<Unit>, packs: List<PackResources>) = createReload(executor, syncExecutor, task, packs).done()

    override fun contains(key: Key) = namespacedPacks[key.namespace()]?.contains(key) ?: false

    override fun resources(key: Key) = namespacedPacks[key.namespace()]?.resources(key) ?: throw FileNotFoundException(key.asString())

    override fun list(name: String, predicate: (String) -> Boolean): Collection<Key> {
        val resources = mutableSetOf<Key>()
        namespacedPacks.values.forEach { resources.addAll(it.list(name, predicate)) }
        return resources.toMutableList().apply { sort() }
    }

    override fun invoke(p1: Key) = namespacedPacks[p1.namespace()]?.invoke(p1) ?: throw FileNotFoundException(p1.asString())

    override fun close() = clear()

    private fun clear() {
        namespacedPacks.clear()
        namespaces.clear()
        packList.forEach(PackResources::close)
        packList.clear()
    }

    private fun createReload(executor: Executor, syncExecutor: Executor, task: CompletableFuture<Unit>, packs: List<PackResources>): ReloadInstance {
        LOGGER.info("Reloading resource manager, loaded packs: ${packs.joinToString { it.name }}")
        clear()
        packs.forEach {
            try {
                add(it)
            } catch (exception: Exception) {
                LOGGER.error("Failed to add data pack ${it.name}!", exception)
                return FailingReloadInstance(PackLoadException(it, exception))
            }
        }
        return if (LOGGER.isDebugEnabled) {
            ProfiledReloadInstance(this, listeners.toList(), executor, syncExecutor, task)
        } else {
            SimpleReloadInstance.of(this, listeners.toList(), executor, syncExecutor, task)
        }
    }

    override val packs: Sequence<PackResources>
        get() = packList.asSequence()

    companion object {

        private val LOGGER = logger<ReloadableResourceManager>()
    }
}
