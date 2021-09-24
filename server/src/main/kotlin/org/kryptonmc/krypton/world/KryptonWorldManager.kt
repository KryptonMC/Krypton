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
package org.kryptonmc.krypton.world

import com.google.common.hash.Hashing
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameModes
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.InternalResourceKeys
import org.kryptonmc.krypton.util.ChunkProgressListener
import org.kryptonmc.krypton.util.daemon
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.threadFactory
import org.kryptonmc.krypton.util.uncaughtExceptionHandler
import org.kryptonmc.krypton.world.chunk.ChunkStatus
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.data.DerivedWorldData
import org.kryptonmc.krypton.world.data.PrimaryWorldData
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.dimension.KryptonDimensionTypes
import org.kryptonmc.krypton.world.generation.DebugGenerator
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.rule.KryptonGameRuleHolder
import org.kryptonmc.krypton.world.data.WorldDataManager
import java.io.File
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Suppress("MemberVisibilityCanBePrivate")
class KryptonWorldManager(
    override val server: KryptonServer,
    val worldFolder: Path
) : WorldManager {

    private val storageManager = WorldDataManager(worldFolder, server.useDataConverter)
    private val worldExecutor = ThreadPoolExecutor(
        0,
        Runtime.getRuntime().availableProcessors() / 2,
        60L,
        TimeUnit.SECONDS,
        SynchronousQueue(),
        threadFactory("World Handler %d") {
            daemon()
            uncaughtExceptionHandler { thread, exception ->
                LOGGER.error("Caught unhandled exception in thread ${thread.name}!", exception)
                server.stop()
            }
        }
    )
    private val customWorldFolder = worldFolder.resolve("dimensions")

    private val name = server.config.world.name
    private val data = storageManager.load(server.config.world.name) ?: PrimaryWorldData(
        name,
        worldFolder.resolve(name),
        server.config.world.gameMode,
        server.config.world.difficulty,
        server.config.world.hardcore,
        KryptonGameRuleHolder(),
        DataPackConfig.DEFAULT,
        WorldGenerationSettings.default()
    ).apply { storageManager.save(server.config.world.name, this) }

    override val worlds = mutableMapOf<ResourceKey<World>, KryptonWorld>()
    override val default: KryptonWorld
        get() = worlds[World.OVERWORLD] ?: error("The default world has not yet been loaded!")

    fun init() {
        create()
        prepare()
    }

    override fun get(key: Key) = worlds[ResourceKey.of(ResourceKeys.DIMENSION, key)]

    override fun load(key: Key): CompletableFuture<KryptonWorld> {
        val resourceKey = ResourceKey.of(ResourceKeys.DIMENSION, key)
        if (resourceKey === World.OVERWORLD) return failFuture(IllegalArgumentException("The default world cannot be loaded!"))
        val loaded = worlds[resourceKey]
        if (loaded != null) return CompletableFuture.completedFuture(loaded)
        val dimensionType = Registries.DIMENSION_TYPE[key] as? KryptonDimensionType
            ?: return failFuture(IllegalStateException("No dimension type found for given key $key!"))
        val generator = data.worldGenerationSettings.dimensions[ResourceKey.of(
            InternalResourceKeys.DIMENSION,
            key
        )]?.generator ?: return failFuture(IllegalStateException("No generator found for given key $key!"))

        LOGGER.info("Loading world ${key.asString()}...")
        val folderName = key.storageFolder()
        val isSubWorld = folderName == "DIM-1" || folderName == "DIM1"

        return CompletableFuture.supplyAsync({
            val path = if (isSubWorld) folderName else key.namespace() + File.separator + key.value()
            val worldData = storageManager.load(path) ?: kotlin.run {
                val gameMode = server.config.world.gameMode
                val difficulty = server.config.world.difficulty
                val hardcore = server.config.world.hardcore
                val rules = KryptonGameRuleHolder()
                PrimaryWorldData(
                    folderName,
                    worldFolder.resolve(path),
                    gameMode,
                    difficulty,
                    hardcore,
                    rules,
                    data.dataPackConfig,
                    data.worldGenerationSettings
                )
            }

            worldData.setModdedInfo(KryptonPlatform.name, true)
            val isDebug = worldData.worldGenerationSettings.isDebug
            val seed = Hashing.sha256().hashLong(worldData.worldGenerationSettings.seed).asLong()
            KryptonWorld(server, worldData, resourceKey, dimensionType, generator, isDebug, seed, true)
        }, worldExecutor)
    }

    override fun save(world: World): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync({ world.save() }, worldExecutor)

    override fun contains(key: Key) = worlds.containsKey(ResourceKey.of(ResourceKeys.DIMENSION, key))

    private fun create() {
        val generationSettings = data.worldGenerationSettings
        val isDebug = generationSettings.isDebug
        val seed = Hashing.sha256().hashLong(generationSettings.seed).asLong()
        val dimensions = generationSettings.dimensions
        val overworld = dimensions[Dimension.OVERWORLD]
        val dimensionType = (overworld?.type as? KryptonDimensionType) ?: KryptonDimensionTypes.OVERWORLD
        val generator = overworld?.generator ?: DebugGenerator(InternalRegistries.BIOME)
        val world = KryptonWorld(
            server,
            data,
            World.OVERWORLD,
            dimensionType,
            generator,
            isDebug,
            seed,
            true
        )

        worlds[World.OVERWORLD] = world
        if (!data.isInitialized) {
            data.isInitialized = true
            if (isDebug) setupDebugWorld(data)
        }

        if (dimensions.isEmpty()) return
        val derived = DerivedWorldData(data)
        dimensions.entries.forEach { (key, dimension) ->
            if (key === Dimension.OVERWORLD) return@forEach
            val resourceKey = ResourceKey.of(ResourceKeys.DIMENSION, key.location)
            val type = dimension.type as? KryptonDimensionType ?: KryptonDimensionTypes.OVERWORLD

            worlds[resourceKey] = KryptonWorld(
                server,
                derived,
                resourceKey,
                type,
                dimension.generator,
                isDebug,
                seed,
                false
            )
        }
    }

    private fun prepare() {
        val listener = ChunkProgressListener(9)
        LOGGER.info("Preparing start region for dimension ${default.dimension.location}...")
        listener.tick()
        default.chunkManager.addTicket(
            default.data.spawnX shr 4,
            default.data.spawnZ shr 4,
            TicketTypes.START,
            22,
            Unit
        ) { listener.updateStatus(ChunkStatus.FULL) }
        listener.stop()
    }

    private fun setupDebugWorld(data: PrimaryWorldData) {
        data.difficulty = Difficulty.PEACEFUL
        data.isRaining = false
        data.isThundering = false
        data.clearWeatherTime = 1000000000
        data.dayTime = 6000L
        data.gameMode = (GameModes.SPECTATOR as KryptonGameMode)
    }

    fun saveAll(): Boolean {
        var successful = false
        worlds.values.forEach {
            it.save()
            successful = true
        }
        storageManager.save(name, data)
        return successful
    }

    companion object {

        private val LOGGER = logger<KryptonWorldManager>()

        private fun Key.storageFolder() = when (this) {
            World.OVERWORLD.location -> ""
            World.NETHER.location -> "DIM-1"
            World.END.location -> "DIM1"
            else -> value()
        }

        private fun <T> failFuture(exception: Exception): CompletableFuture<T> =
            CompletableFuture.failedFuture(exception)
    }
}
