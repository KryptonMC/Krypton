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
package org.kryptonmc.krypton.server

import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.resource.reload.ReloadableResourceManager
import org.kryptonmc.krypton.tags.TagContainer
import org.kryptonmc.krypton.tags.TagManager
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class ServerResources(registryHolder: RegistryHolder) : AutoCloseable {

    val manager = ReloadableResourceManager()
    private val tagManager = TagManager(registryHolder)

    init {
        manager.registerListener(tagManager)
    }

    fun updateGlobals() = tagManager.tags.bindToGlobal()

    override fun close() = manager.close()

    val tags: TagContainer
        get() = tagManager.tags

    companion object {

        private val INITIAL_DATA_RELOAD_TASK: CompletableFuture<Unit> = CompletableFuture.completedFuture(Unit)

        fun load(packs: List<PackResources>, registryHolder: RegistryHolder, executor: Executor, syncExecutor: Executor): CompletableFuture<ServerResources> {
            val resources = ServerResources(registryHolder)
            return resources.manager.reload(executor, syncExecutor, INITIAL_DATA_RELOAD_TASK, packs).whenComplete { _, exception ->
                if (exception != null) resources.close()
            }.thenApply { resources }
        }
    }
}
