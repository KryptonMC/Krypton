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
import org.kryptonmc.krypton.pack.resources.reload.ReloadInstance
import org.kryptonmc.krypton.pack.resources.reload.PreparableReloadListener
import org.kryptonmc.krypton.pack.resources.reload.SimpleReloadInstance
import org.kryptonmc.krypton.pack.PackType
import org.kryptonmc.krypton.util.ImmutableLists
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Predicate

class ReloadableResourceManager(private val type: PackType) : ResourceManager, AutoCloseable {

    private var resources: CloseableResourceManager = MultiPackResourceManager(type, ImmutableLists.of())
    private val listeners = ArrayList<PreparableReloadListener>()

    override fun close() {
        resources.close()
    }

    fun registerListener(listener: PreparableReloadListener) {
        listeners.add(listener)
    }

    fun createReload(background: Executor, main: Executor, waitingFor: CompletableFuture<Unit>, packs: List<PackResources>): ReloadInstance {
        LOGGER.info(Supplier { "Reloading resource manager: ${packs.joinToString(", ") { it.packId() }}" })
        resources.close()
        resources = MultiPackResourceManager(type, packs)
        return SimpleReloadInstance.create(resources, listeners, background, main, waitingFor)
    }

    override fun getResource(location: Key): Resource? = resources.getResource(location)

    override fun listResources(path: String, predicate: Predicate<Key>): Map<Key, Resource> = resources.listResources(path, predicate)

    override fun listResourceStacks(path: String, predicate: Predicate<Key>): Map<Key, List<Resource>> =
        resources.listResourceStacks(path, predicate)

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
