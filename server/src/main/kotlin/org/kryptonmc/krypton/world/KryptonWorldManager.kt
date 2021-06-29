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

import com.mojang.serialization.Dynamic
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTReader
import org.kryptonmc.api.util.toKey
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameVersion
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.concurrent.NamedThreadFactory
import org.kryptonmc.krypton.util.datafix.DATA_FIXER
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.util.nbt.getBoolean
import org.kryptonmc.krypton.util.nbt.getCompound
import org.kryptonmc.krypton.util.nbt.getInt
import org.kryptonmc.krypton.util.nbt.getLong
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.generation.toGenerator
import org.spongepowered.math.vector.Vector3i
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.file.Path
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream
import kotlin.system.exitProcess

@Suppress("MemberVisibilityCanBePrivate")
class KryptonWorldManager(override val server: KryptonServer, name: String) : WorldManager {

    private val worldExecutor = Executors.newCachedThreadPool(NamedThreadFactory("World Handler %d"))
    override val worlds = mutableMapOf<String, KryptonWorld>()
    override val default: KryptonWorld

    init {
        default = try {
            Messages.WORLD.LOAD.info(LOGGER, name)
            load(name).get()
        } catch (exception: Exception) {
            if (exception !is IllegalArgumentException) Messages.WORLD.LOAD_ERROR.error(LOGGER, name, exception)
            exitProcess(0)
        }
        worlds[name] = default
        Messages.WORLD.LOADED.info(LOGGER)
    }

    override fun load(name: String): CompletableFuture<KryptonWorld> {
        val folder = CURRENT_DIRECTORY.resolve(name)
        require(folder.exists()) {
            Messages.WORLD.NOT_FOUND.error(LOGGER, name)
            "World with key $name does not exist!"
        }
        return loadWorld(folder)
    }

    override fun save(world: World): CompletableFuture<Unit> = CompletableFuture.supplyAsync({
        world.save()
        if (world is KryptonWorld) world.chunkManager.saveAll()
    }, worldExecutor)

    override fun contains(name: String) = worlds.containsKey(name)

    // TODO: Possibly also make this preload spawn chunks
    private fun loadWorld(folder: Path): CompletableFuture<KryptonWorld> = CompletableFuture.supplyAsync({
        val dataFile = folder.resolve("level.dat")
        val nbt = NBTReader(dataFile.inputStream()).use { it.read() as NBTCompound }.getCompound("Data", NBTCompound())

        val version = nbt.getInt("DataVersion", -1)
        val data = DATA_FIXER.update(References.LEVEL, Dynamic(NBTOps, nbt), version, ServerInfo.WORLD_VERSION)

        // TODO: Make the world gen settings also use the updated data from the DFU
        val worldGenSettings = nbt.getCompound("WorldGenSettings")?.let { settings ->
            val dimensions = settings.getCompound("dimensions", NBTCompound()).iterator().asSequence()
                .filterIsInstance<Pair<String, NBTCompound>>()
                .associate { (key, value) ->
                    key.toKey() to Dimension((value.getString("type") ?: "").toKey(), (value.getCompound("generator") ?: NBTCompound()).toGenerator())
                }
            WorldGenerationSettings(settings.getLong("seed", 0L), settings.getBoolean("generate_features", false), dimensions)
        } ?: WorldGenerationSettings(0L, false, emptyMap())

        val gamemode = Gamemode.fromId(data["GameType"].asInt(0)) ?: Gamemode.SURVIVAL
        val time = data["Time"].asLong(0L)

        KryptonWorld(
            server,
            folder,
            loadUUID(folder),
            data["LevelName"].asString(""),
            data["allowCommands"].asBoolean(gamemode == Gamemode.CREATIVE),
            BorderBuilder(
                data["BorderCenterX"].asDouble(0.0),
                data["BorderCenterZ"].asDouble(0.0),
                data["BorderDamagePerBlock"].asDouble(0.2),
                data["BorderSize"].asDouble(5.9999968E7),
                data["BorderSafeZone"].asDouble(5.0),
                data["BorderSizeLerpTarget"].asDouble(0.0),
                data["BorderSizeLerpTime"].asLong(0L),
                data["BorderWarningBlocks"].asInt(5),
                data["BorderWarningTime"].asInt(15)
            ),
            data["clearWeatherTime"].asInt(0),
            data["DayTime"].asLong(time),
            data["Difficulty"].asNumber().map { Difficulty.fromId(it.toInt()) }.result().orElse(Difficulty.NORMAL),
            KryptonGameRuleHolder(data["GameRules"]),
            worldGenSettings,
            gamemode,
            data["hardcore"].asBoolean(false),
            LocalDateTime.ofInstant(Instant.ofEpochMilli(data["LastPlayed"].asLong(System.currentTimeMillis())), ZoneOffset.UTC),
            data["raining"].asBoolean(false),
            data["MapFeatures"].asBoolean(false),
            data["rainTime"].asInt(0),
            Vector3i(data["SpawnX"].asInt(0), data["SpawnY"].asInt(0), data["SpawnZ"].asInt(0)),
            data["SpawnAngle"].asFloat(0F),
            data["thundering"].asBoolean(false),
            data["thunderTime"].asInt(0),
            time,
            version,
            data["Version"].let {
                GameVersion(it["Id"].asInt(ServerInfo.WORLD_VERSION), it["Name"].asString(KryptonServerInfo.minecraftVersion), data["Snapshot"].asBoolean(false))
            },
            DEFAULT_BUILD_LIMIT,
            data["ServerBrands"].asStream().flatMap { dynamic ->
                dynamic.asString().result().map { Stream.of(it) }.orElseGet { Stream.empty() }
            }.collect(Collectors.toSet())
        )
    }, worldExecutor)

    private fun loadUUID(folder: Path): UUID {
        val uuidFile = folder.resolve("uid.dat")
        return if (uuidFile.exists()) {
            DataInputStream(uuidFile.inputStream()).use { UUID(it.readLong(), it.readLong()) }
        } else {
            val uuid = UUID.randomUUID()
            DataOutputStream(uuidFile.outputStream()).use {
                it.writeLong(uuid.mostSignificantBits)
                it.writeLong(uuid.leastSignificantBits)
            }
            return uuid
        }
    }

    fun saveAll() = worlds.forEach { (_, world) -> save(world).get() }

    companion object {

        private const val DEFAULT_BUILD_LIMIT = 256

        private val LOGGER = logger<KryptonWorldManager>()
    }
}

fun <K, V, K1, V1> Map<K, V>.transform(function: (Map.Entry<K, V>) -> Pair<K1, V1>): Map<K1, V1> {
    val temp = mutableMapOf<K1, V1>()
    for (entry in this) {
        temp += function(entry)
    }
    return temp
}
