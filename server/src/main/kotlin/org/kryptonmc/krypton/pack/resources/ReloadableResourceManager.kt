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
