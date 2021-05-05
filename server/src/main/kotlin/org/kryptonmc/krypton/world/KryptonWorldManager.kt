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

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.BinaryTagIO.Compression.GZIP
import net.kyori.adventure.nbt.ByteBinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.WorldManager
import org.kryptonmc.krypton.api.world.WorldVersion
import org.kryptonmc.krypton.util.concurrent.NamedThreadFactory
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.bossbar.Bossbar
import org.kryptonmc.krypton.world.dimension.Dimension
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.generation.toGenerator
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.file.Path
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
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
            LOGGER.info("Loading world $name...")
            load(name).get()
        } catch (exception: Exception) {
            if (exception !is IllegalArgumentException) LOGGER.error("Exception loading world $name!", exception)
            exitProcess(0)
        }
        worlds[name] = default
        LOGGER.info("World loaded!")
    }

    override fun load(name: String): Future<KryptonWorld> {
        val folder = CURRENT_DIRECTORY.resolve(name)
        require(folder.exists()) { "World with name $name does not exist!".apply { LOGGER.error(this) } }
        return loadWorld(folder)
    }

    override fun save(world: World): Future<*> = worldExecutor.submit {
        world.save()
        if (world is KryptonWorld) world.chunkManager.saveAll()
    }

    override fun contains(name: String) = worlds.containsKey(name)

    // TODO: Possibly also make this preload spawn chunks
    private fun loadWorld(folder: Path): Future<KryptonWorld> = worldExecutor.submit(Callable {
        val dataFile = folder.resolve("level.dat")
        val nbt = BinaryTagIO.unlimitedReader().read(dataFile, GZIP).getCompound("Data")

        val bossbars = nbt.getCompound("CustomBossEvents").map { (key, bossbar) ->
            bossbar as CompoundBinaryTag
            val players = bossbar.getList("Players").map {
                it as CompoundBinaryTag
                UUID(it.getLong("M"), it.getLong("L"))
            }

            val flags = mutableSetOf<BossBar.Flag>()
            if (bossbar.getBoolean("CreateWorldFog")) flags += BossBar.Flag.CREATE_WORLD_FOG
            if (bossbar.getBoolean("DarkenScreen")) flags += BossBar.Flag.DARKEN_SCREEN
            if (bossbar.getBoolean("PlayBossMusic")) flags += BossBar.Flag.PLAY_BOSS_MUSIC

            Bossbar(
                BossBar.bossBar(
                    GsonComponentSerializer.gson().deserialize(bossbar.getString("Name")),
                    bossbar.getInt("Value").toFloat() / bossbar.getInt("Max").toFloat(),
                    BossBar.Color.NAMES.value(bossbar.getString("Color")) ?: BossBar.Color.WHITE,
                    BossBar.Overlay.NAMES.value(bossbar.getString("Overlay")) ?: BossBar.Overlay.PROGRESS,
                    flags
                ),
                key.toNamespacedKey(),
                bossbar.getBoolean("Visible"),
                players
            )
        }

        val worldGenSettings = nbt.getCompound("WorldGenSettings").let { settings ->
            val dimensions = settings.getCompound("dimensions").associate { (key, value) ->
                key.toNamespacedKey() to (value as CompoundBinaryTag).let {
                    Dimension(it.getString("type").toNamespacedKey(), it.getCompound("generator").toGenerator())
                }
            }
            WorldGenerationSettings(settings.getLong("seed"), settings.getBoolean("generate_features"), dimensions)
        }

        val spawnX = nbt.getInt("SpawnX")
        val spawnZ = nbt.getInt("SpawnZ")
        val spawnLocation = LocationBuilder(spawnX.toDouble(), nbt.getInt("SpawnY").toDouble(), spawnZ.toDouble())

        KryptonWorld(
            server,
            folder,
            loadUUID(folder),
            nbt.getString("LevelName"),
            mutableSetOf(),
            bossbars,
            nbt.getBoolean("allowCommands"),
            BorderBuilder(
                nbt.getDouble("BorderCenterX"),
                nbt.getDouble("BorderCenterZ"),
                nbt.getDouble("BorderDamagePerBlock"),
                nbt.getDouble("BorderSize"),
                nbt.getDouble("BorderSafeZone"),
                nbt.getDouble("BorderSizeLerpTarget"),
                nbt.getLong("BorderSizeLerpTime"),
                nbt.getDouble("BorderWarningBlocks"),
                nbt.getDouble("BorderWarningTime")
            ),
            nbt.getInt("clearWeatherTime"),
            nbt.getLong("DayTime"),
            Difficulty.fromId(nbt.getByte("Difficulty").toInt()),
            nbt.getBoolean("DifficultyLocked"),
            nbt.getCompound("GameRules").associate { Gamerule.fromName(it.key) to (it.value as StringBinaryTag).value() },
            worldGenSettings,
            requireNotNull(Gamemode.fromId(nbt.getInt("GameType"))),
            nbt.getBoolean("hardcore"),
            LocalDateTime.ofInstant(Instant.ofEpochMilli(nbt.getLong("LastPlayed")), ZoneOffset.UTC),
            nbt.getBoolean("raining"),
            nbt.getBoolean("MapFeatures"),
            nbt.getInt("rainTime"),
            nbt.getLong("RandomSeed"),
            spawnLocation,
            nbt.getBoolean("thundering"),
            nbt.getInt("thunderTime"),
            nbt.getLong("Time"),
            nbt.getInt("version"),
            nbt.getCompound("Version").let {
                WorldVersion(it.getInt("Id"), it.getString("Name"), it.getBoolean("Snapshot"))
            },
            DEFAULT_BUILD_LIMIT,
            nbt.getLong("RandomSeed"),
            mutableListOf(),
            nbt.getList("ServerBrands").add(StringBinaryTag.of("Krypton")).map { (it as StringBinaryTag).value() }.toSet()
        )
    })

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

    fun saveAll() = worlds.forEach { (_, world) -> save(world) }

    companion object {

        private const val DEFAULT_BUILD_LIMIT = 256

        private val LOGGER = logger<KryptonWorldManager>()
    }
}

// I know this looks stupid, and it probably is stupid, but this avoids me having to use toByte()
// also means I don't declare extra unnecessary integers on the stack
private const val ONE: Byte = 1

val ByteBinaryTag.boolean: Boolean
    get() {
        if (value() > ONE) throw IllegalArgumentException("Boolean value cannot be greater than 1!")
        return value() == ONE
    }

fun <K, V, K1, V1> Map<K, V>.transform(function: (Map.Entry<K, V>) -> Pair<K1, V1>): Map<K1, V1> {
    val temp = mutableMapOf<K1, V1>()
    for (entry in this) {
        temp += function(entry)
    }
    return temp
}
