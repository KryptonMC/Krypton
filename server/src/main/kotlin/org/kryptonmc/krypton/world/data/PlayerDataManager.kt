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
package org.kryptonmc.krypton.world.data

import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.converter.MCDataConverter.convertData
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.createTempFile
import org.kryptonmc.krypton.util.daemon
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.threadFactory
import org.kryptonmc.krypton.util.tryCreateFile
import org.kryptonmc.krypton.util.uncaughtExceptionHandler
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.io.IOException
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.moveTo

/**
 * Responsible for loading and saving player data files
 */
class PlayerDataManager(private val folder: Path) {

    private val executor = Executors.newFixedThreadPool(
        2,
        threadFactory("Player Data IO %d") {
            daemon()
            uncaughtExceptionHandler { thread, exception ->
                LOGGER.error("Caught unhandled exception in thread ${thread.name}!", exception)
            }
        }
    )

    fun load(player: KryptonPlayer): CompletableFuture<CompoundTag?> = CompletableFuture.supplyAsync({
        val playerFile = folder.resolve("${player.uuid}.dat")
        if (!playerFile.exists()) {
            playerFile.tryCreateFile()
            return@supplyAsync null
        }

        val nbt = try {
            TagIO.read(playerFile, TagCompression.GZIP)
        } catch (exception: IOException) {
            LOGGER.warn("Failed to load player data for player ${player.name}!", exception)
            return@supplyAsync null
        }

        val version = if (nbt.contains("DataVersion", 99)) nbt.getInt("DataVersion") else -1
        // We won't upgrade data if use of the data converter is disabled.
        if (version < KryptonPlatform.worldVersion && !player.server.useDataConverter) {
            LOGGER.error("The server attempted to load a chunk from a earlier version of Minecraft when data " +
                    "conversion is disabled!")
            LOGGER.info("If you would like to use data conversion, provide the --upgrade-data or " +
                    "--use-data-converter flag(s) to the JAR on startup.")
            LOGGER.warn("Beware that this is an experimental tool and has known issues with pre-1.13 worlds.")
            LOGGER.warn("USE THIS TOOL AT YOUR OWN RISK. If the tool corrupts your data, that is YOUR responsibility!")
            error("Tried to load old player data from version $version when data conversion is disabled!")
        }

        // Don't use data converter if the version is not older than our version.
        val data = if (player.server.useDataConverter && version < KryptonPlatform.worldVersion) {
            nbt.convertData(MCTypeRegistry.PLAYER, version, KryptonPlatform.worldVersion)
        } else {
            nbt
        }
        player.load(data)
        data
    }, executor)

    fun save(player: KryptonPlayer) {
        val data = player.save().build()

        // Create temp file and write data
        val temp = folder.createTempFile(player.uuid.toString(), ".dat")
        TagIO.write(temp, data, TagCompression.GZIP)

        // Resolve actual file, and if it doesn't exist, rename the temp file
        val dataPath = folder.resolve("${player.uuid}.dat")
        if (!dataPath.exists()) {
            temp.moveTo(dataPath)
            return
        }

        // Save the old data and then save the new data
        val oldDataPath = folder.resolve("${player.uuid}.dat_old").apply { deleteIfExists() }
        dataPath.moveTo(oldDataPath)
        dataPath.deleteIfExists()
        temp.moveTo(dataPath)
    }

    companion object {

        private val LOGGER = logger<PlayerDataManager>()
    }
}
