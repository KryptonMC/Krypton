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

import ca.spottedleaf.dataconverter.minecraft.MCDataConverter
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.io.TagIO
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 * Responsible for loading and saving player data files
 */
class PlayerDataManager(val folder: Path, private val serializeData: Boolean) {

    fun load(player: KryptonPlayer, executor: Executor): CompletableFuture<CompoundTag?> {
        if (!serializeData) return CompletableFuture.completedFuture(null)
        return loadData(player, executor)
    }

    private fun loadData(player: KryptonPlayer, executor: Executor): CompletableFuture<CompoundTag?> = CompletableFuture.supplyAsync({
        val playerFile = folder.resolve("${player.uuid}.dat")
        if (!Files.exists(playerFile)) {
            try {
                Files.createFile(playerFile)
            } catch (exception: Exception) {
                LOGGER.warn("Failed to create player file for player with UUID ${player.uuid}!", exception)
            }
            return@supplyAsync null
        }

        val nbt = try {
            TagIO.read(playerFile, TagCompression.GZIP)
        } catch (exception: IOException) {
            LOGGER.warn("Failed to load player data for player ${player.profile.name}!", exception)
            return@supplyAsync null
        }

        val version = if (nbt.contains("DataVersion", 99)) nbt.getInt("DataVersion") else -1
        // We won't upgrade data if use of the data converter is disabled.
        if (version < KryptonPlatform.worldVersion && !player.server.useDataConverter) {
            LOGGER.error("The server attempted to load a chunk from a earlier version of Minecraft when data conversion is disabled!")
            LOGGER.info("If you would like to use data conversion, provide the --upgrade-data or --use-data-converter flag(s) to the " +
                    "JAR on startup.")
            LOGGER.warn("Beware that this is an experimental tool and has known issues with pre-1.13 worlds.")
            LOGGER.warn("USE THIS TOOL AT YOUR OWN RISK. If the tool corrupts your data, that is YOUR responsibility!")
            error("Tried to load old player data from version $version when data conversion is disabled!")
        }

        // Don't use data converter if the version is not older than our version.
        val data = if (player.server.useDataConverter && version < KryptonPlatform.worldVersion) {
            MCDataConverter.convertTag(MCTypeRegistry.PLAYER, nbt, version, KryptonPlatform.worldVersion)
        } else {
            nbt
        }
        player.load(data)
        data
    }, executor)

    fun save(player: KryptonPlayer): CompoundTag? {
        if (!serializeData) return null
        val data = player.saveWithPassengers().build()

        // Create temp file and write data
        val temp = Files.createTempFile(folder, player.uuid.toString(), ".dat")
        TagIO.write(temp, data, TagCompression.GZIP)

        // Resolve actual file, and if it doesn't exist, rename the temp file
        val dataPath = folder.resolve("${player.uuid}.dat")
        if (!Files.exists(dataPath)) {
            Files.move(temp, dataPath, StandardCopyOption.REPLACE_EXISTING)
            return data
        }

        // Save the old data and then save the new data
        val oldDataPath = folder.resolve("${player.uuid}.dat_old")
        Files.deleteIfExists(oldDataPath)
        Files.move(dataPath, oldDataPath, StandardCopyOption.REPLACE_EXISTING)
        Files.deleteIfExists(dataPath)
        Files.move(temp, dataPath, StandardCopyOption.REPLACE_EXISTING)
        Files.deleteIfExists(temp)
        return data
    }

    companion object {

        private val LOGGER = logger<PlayerDataManager>()
    }
}
