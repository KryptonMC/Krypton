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
package org.kryptonmc.krypton.pack.resources

import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.util.Supplier
import org.kryptonmc.krypton.pack.PackResources
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Predicate

class ReloadableResourceManager : ResourceManager, AutoCloseable {

    private var manager = MultiPackResourceManager(emptyList())
    private val listeners = ArrayList<ReloadListener>()

    fun registerListener(listener: ReloadListener) {
        listeners.add(listener)
    }

    fun createReload(background: Executor, main: Executor, waitingFor: CompletableFuture<Unit>, packs: List<PackResources>): ReloadInstance {
        LOGGER.info(Supplier { "Reloading resource manager: ${packs.joinToString(", ") { it.name() }}" })
        manager.close()
        manager = MultiPackResourceManager(packs)
        return SimpleReloadInstance.of(manager, listeners, background, main, waitingFor)
    }

    override fun getResource(location: Key): Resource? = manager.getResource(location)

    override fun listResources(path: String, predicate: Predicate<Key>): Map<Key, Resource> = manager.listResources(path, predicate)

    override fun listResourceStacks(path: String, predicate: Predicate<Key>): Map<Key, List<Resource>> = manager.listResourceStacks(path, predicate)

    override fun close() {
        manager.close()
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
