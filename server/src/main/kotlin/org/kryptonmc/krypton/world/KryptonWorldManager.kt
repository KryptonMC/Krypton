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
package org.kryptonmc.krypton.world

import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.world.util.ChunkProgressListener
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.registry.KryptonDynamicRegistries
import org.kryptonmc.krypton.util.executor.DefaultPoolUncaughtExceptionHandler
import org.kryptonmc.krypton.util.executor.ThreadPoolBuilder
import org.kryptonmc.krypton.util.executor.daemonThreadFactory
import org.kryptonmc.krypton.world.chunk.ChunkLoader
import org.kryptonmc.krypton.world.data.WorldDataSerializer
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.dimension.KryptonDimensionTypes
import java.io.File
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

class KryptonWorldManager(
    override val server: KryptonServer,
    private val dataSerializer: WorldDataSerializer,
    private val chunkLoader: ChunkLoader
) : WorldManager {

    private val worldExecutor = ThreadPoolBuilder.create()
        .coreSize(0)
        .maximumSize(max(1, Runtime.getRuntime().availableProcessors() / 2))
        .keepAlive(Duration.ofSeconds(60))
        .factory(daemonThreadFactory("World Handler #%d") { setUncaughtExceptionHandler(DefaultPoolUncaughtExceptionHandler(LOGGER)) })
        .build()
    private val data = checkNotNull(dataSerializer.load(name())) { "You must provide an existing world for Krypton!" }

    override val worlds: MutableMap<ResourceKey<World>, KryptonWorld> = ConcurrentHashMap()
    override val default: KryptonWorld
        get() = worlds.get(World.OVERWORLD) ?: error("The default world has not yet been loaded!")

    private fun name(): String = server.config.world.name

    fun init() {
        create()
        prepare()
    }

    override fun getWorld(key: Key): World? = worlds.get(ResourceKey.of(ResourceKeys.DIMENSION, key))

    override fun loadWorld(key: Key): CompletableFuture<KryptonWorld?> {
        val resourceKey = ResourceKey.of(ResourceKeys.DIMENSION, key)
        if (resourceKey === World.OVERWORLD) return failFuture(IllegalArgumentException("The default world cannot be loaded!"))
        val loaded = worlds.get(resourceKey)
        if (loaded != null) return CompletableFuture.completedFuture(loaded)

        val dimensionType = KryptonDynamicRegistries.DIMENSION_TYPE.get(key) as? KryptonDimensionType
            ?: return failFuture(IllegalStateException("No dimension type found for given key $key!"))

        LOGGER.info("Loading world ${key.asString()}...")
        val folderName = getStorageFolder(key)
        val isSubWorld = folderName == "DIM-1" || folderName == "DIM1"

        val path = if (isSubWorld) folderName else key.namespace() + File.separator + key.value()
        return CompletableFuture.supplyAsync({
            val worldData = dataSerializer.load(path) ?: return@supplyAsync null
            KryptonWorld(server, worldData, resourceKey, dimensionType, chunkLoader)
        }, worldExecutor)
    }

    override fun saveWorld(world: World): CompletableFuture<Void> {
        val kryptonWorld = world.downcast() // Moved outside of the block to fail fast
        return CompletableFuture.runAsync({ kryptonWorld.save() }, worldExecutor)
    }

    override fun isLoaded(key: Key): Boolean = worlds.containsKey(ResourceKey.of(ResourceKeys.DIMENSION, key))

    fun saveAllChunks(suppressLog: Boolean, forced: Boolean): Boolean {
        var successful = false
        worlds.values.forEach { world ->
            if (!suppressLog) LOGGER.info("Saving chunks for world $world in ${world.dimension.location}...")
            if (!world.doNotSave || forced) world.save()
            successful = true
        }
        dataSerializer.save(name(), data)
        return successful
    }

    private fun create() {
        val dimension = KryptonDimensionTypes.OVERWORLD
        val world = KryptonWorld(server, data, World.OVERWORLD, dimension, chunkLoader)
        worlds.put(World.OVERWORLD, world)
        if (!data.isInitialized) data.isInitialized = true
    }

    private fun prepare() {
        val listener = ChunkProgressListener(9)
        LOGGER.info("Preparing start region for dimension ${default.dimension.location}...")
        listener.tick()
        default.chunkManager.loadStartingArea(SectionPos.blockToSection(default.data.spawnX), SectionPos.blockToSection(default.data.spawnZ)) {
            listener.updateStatus()
        }
        listener.stop()
    }

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun <T> failFuture(exception: Exception): CompletableFuture<T> = CompletableFuture.failedFuture(exception)

        @JvmStatic
        private fun getStorageFolder(location: Key): String = when (location) {
            World.OVERWORLD.location -> ""
            World.NETHER.location -> "DIM-1"
            World.END.location -> "DIM1"
            else -> location.value()
        }
    }
}
