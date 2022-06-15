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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.util.ChunkProgressListener
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.pool.daemonThreadFactory
import org.kryptonmc.krypton.util.pool.uncaughtExceptionHandler
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.data.PrimaryWorldData
import org.kryptonmc.krypton.world.data.WorldDataManager
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.dimension.KryptonDimensionTypes
import org.kryptonmc.krypton.world.rule.KryptonGameRuleHolder
import java.io.File
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.max

class KryptonWorldManager(
    override val server: KryptonServer,
    private val worldFolder: Path
) : WorldManager {

    private val storageManager = WorldDataManager(worldFolder, server.useDataConverter)
    private val worldExecutor = ThreadPoolExecutor(
        0,
        max(1, Runtime.getRuntime().availableProcessors() / 2),
        60L,
        TimeUnit.SECONDS,
        SynchronousQueue(),
        daemonThreadFactory("World Handler %d") {
            uncaughtExceptionHandler { thread, exception ->
                LOGGER.error("Caught unhandled exception in thread ${thread.name}!", exception)
                server.stop()
            }
        }
    )
    val statsFolder: Path = storageManager.resolve("stats")

    private val name = server.config.world.name
    private val data = checkNotNull(storageManager.load(server.config.world.name)) { "You must provide an existing world for Krypton!" }

    override val worlds: MutableMap<ResourceKey<World>, KryptonWorld> = ConcurrentHashMap()
    override val default: KryptonWorld
        get() = worlds[World.OVERWORLD] ?: error("The default world has not yet been loaded!")

    fun init() {
        create()
        prepare()
    }

    override fun get(key: Key): World? = worlds[ResourceKey.of(ResourceKeys.DIMENSION, key)]

    override fun load(key: Key): CompletableFuture<KryptonWorld> {
        val resourceKey = ResourceKey.of(ResourceKeys.DIMENSION, key)
        if (resourceKey === World.OVERWORLD) return failFuture(IllegalArgumentException("The default world cannot be loaded!"))
        val loaded = worlds[resourceKey]
        if (loaded != null) return CompletableFuture.completedFuture(loaded)

        val dimensionType = Registries.DIMENSION_TYPE[key] as? KryptonDimensionType
            ?: return failFuture(IllegalStateException("No dimension type found for given key $key!"))

        LOGGER.info("Loading world ${key.asString()}...")
        val folderName = key.storageFolder()
        val isSubWorld = folderName == "DIM-1" || folderName == "DIM1"

        return CompletableFuture.supplyAsync({
            val path = if (isSubWorld) folderName else key.namespace() + File.separator + key.value()
            val worldData = storageManager.load(path) ?: PrimaryWorldData(
                folderName,
                worldFolder.resolve(path),
                server.config.world.gameMode,
                server.config.world.difficulty,
                server.config.world.hardcore,
                KryptonGameRuleHolder(),
                data.generationSettings
            )
            KryptonWorld(server, worldData, resourceKey, dimensionType, data.generationSettings.seed, true)
        }, worldExecutor)
    }

    override fun save(world: World): CompletableFuture<Unit> {
        if (world !is KryptonWorld) return CompletableFuture.completedFuture(Unit)
        return CompletableFuture.supplyAsync({ world.save(false) }, worldExecutor)
    }

    override fun contains(key: Key): Boolean = worlds.containsKey(ResourceKey.of(ResourceKeys.DIMENSION, key))

    fun saveAll(shouldClose: Boolean): Boolean {
        var successful = false
        worlds.values.forEach {
            it.save(shouldClose)
            successful = true
        }
        storageManager.save(name, data)
        return successful
    }

    private fun create() {
        val world = KryptonWorld(
            server,
            data,
            World.OVERWORLD,
            KryptonDimensionTypes.OVERWORLD,
            data.generationSettings.seed,
            true
        )
        worlds[World.OVERWORLD] = world
        if (!data.isInitialized) data.isInitialized = true
    }

    private fun prepare() {
        val listener = ChunkProgressListener(9)
        LOGGER.info("Preparing start region for dimension ${default.dimension.location}...")
        listener.tick()
        default.chunkManager.addStartTicket(default.data.spawnX shr 4, default.data.spawnZ shr 4) {
            listener.updateStatus(ChunkStatus.FULL)
        }
        listener.stop()
    }

    companion object {

        private val LOGGER = logger<KryptonWorldManager>()

        @JvmStatic
        private fun Key.storageFolder(): String = when (this) {
            World.OVERWORLD.location -> ""
            World.NETHER.location -> "DIM-1"
            World.END.location -> "DIM1"
            else -> value()
        }

        @JvmStatic
        private fun <T> failFuture(exception: Exception): CompletableFuture<T> = CompletableFuture.failedFuture(exception)
    }
}
