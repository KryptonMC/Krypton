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
import org.kryptonmc.krypton.world.chunk.data.ChunkStatus
import org.kryptonmc.krypton.world.data.PrimaryWorldData
import org.kryptonmc.krypton.world.data.WorldDataManager
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.dimension.KryptonDimensionTypes
import org.kryptonmc.krypton.world.rule.WorldGameRules
import java.io.File
import java.nio.file.Path
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

class KryptonWorldManager(override val server: KryptonServer, private val worldFolder: Path) : WorldManager {

    private val storageManager = WorldDataManager(worldFolder)
    private val worldExecutor = ThreadPoolBuilder.create()
        .coreSize(0)
        .maximumSize(max(1, Runtime.getRuntime().availableProcessors() / 2))
        .keepAlive(Duration.ofSeconds(60))
        .factory(daemonThreadFactory("World Handler #%d") { setUncaughtExceptionHandler(DefaultPoolUncaughtExceptionHandler(LOGGER)) })
        .build()
    private val data = checkNotNull(storageManager.load(name())) { "You must provide an existing world for Krypton!" }

    override val worlds: MutableMap<ResourceKey<World>, KryptonWorld> = ConcurrentHashMap()
    override val default: KryptonWorld
        get() = worlds.get(World.OVERWORLD) ?: error("The default world has not yet been loaded!")

    private fun name(): String = server.config.world.name

    fun statsFolder(): Path = storageManager.resolve("stats")

    fun init() {
        create()
        prepare()
    }

    override fun getWorld(key: Key): World? = worlds.get(ResourceKey.of(ResourceKeys.DIMENSION, key))

    override fun loadWorld(key: Key): CompletableFuture<KryptonWorld> {
        val resourceKey = ResourceKey.of(ResourceKeys.DIMENSION, key)
        if (resourceKey === World.OVERWORLD) return failFuture(IllegalArgumentException("The default world cannot be loaded!"))
        val loaded = worlds.get(resourceKey)
        if (loaded != null) return CompletableFuture.completedFuture(loaded)

        val dimensionType = KryptonDynamicRegistries.DIMENSION_TYPE.get(key) as? KryptonDimensionType
            ?: return failFuture(IllegalStateException("No dimension type found for given key $key!"))

        LOGGER.info("Loading world ${key.asString()}...")
        val folderName = getStorageFolder(key)
        val isSubWorld = folderName == "DIM-1" || folderName == "DIM1"

        return CompletableFuture.supplyAsync({
            val path = if (isSubWorld) folderName else key.namespace() + File.separator + key.value()
            val worldData = storageManager.load(path) ?: PrimaryWorldData(
                folderName,
                worldFolder.resolve(path),
                server.config.world.defaultGameMode,
                server.config.world.difficulty,
                server.config.world.hardcore,
                WorldGameRules(),
                data.generationSettings
            )
            KryptonWorld(server, worldData, resourceKey, dimensionType, data.generationSettings.seed, true)
        }, worldExecutor)
    }

    override fun saveWorld(world: World): CompletableFuture<Void> {
        val kryptonWorld = world.downcast() // Moved outside of the block to fail fast
        return CompletableFuture.runAsync({ kryptonWorld.save(false, false) }, worldExecutor)
    }

    override fun isLoaded(key: Key): Boolean = worlds.containsKey(ResourceKey.of(ResourceKeys.DIMENSION, key))

    fun saveAllChunks(suppressLog: Boolean, flush: Boolean, forced: Boolean): Boolean {
        var successful = false
        worlds.values.forEach { world ->
            if (!suppressLog) LOGGER.info("Saving chunks for world $world in ${world.dimension.location}...")
            world.save(flush, world.doNotSave && !forced)
            successful = true
        }
        storageManager.save(name(), data)
        return successful
    }

    private fun create() {
        val world = KryptonWorld(server, data, World.OVERWORLD, KryptonDimensionTypes.OVERWORLD, data.generationSettings.seed, true)
        worlds.put(World.OVERWORLD, world)
        if (!data.isInitialized) data.isInitialized = true
    }

    private fun prepare() {
        val listener = ChunkProgressListener(9)
        LOGGER.info("Preparing start region for dimension ${default.dimension.location}...")
        listener.tick()
        default.chunkManager.loadStartingArea(SectionPos.blockToSection(default.data.spawnX), SectionPos.blockToSection(default.data.spawnZ)) {
            listener.updateStatus(ChunkStatus.FULL)
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
