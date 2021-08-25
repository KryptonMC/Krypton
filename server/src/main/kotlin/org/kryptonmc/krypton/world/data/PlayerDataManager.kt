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

import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.tryCreateFile
import org.kryptonmc.krypton.util.createTempFile
import org.kryptonmc.krypton.util.daemon
import org.kryptonmc.krypton.util.datafix.DATA_FIXER
import org.kryptonmc.krypton.util.datafix.FixType
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.util.threadFactory
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
        Runtime.getRuntime().availableProcessors() / 2,
        threadFactory("Player Data IO %d") { daemon() }
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
        player.load(DATA_FIXER.update(FixType.PLAYER.type, Dynamic(NBTOps, nbt), version, KryptonPlatform.worldVersion).value as CompoundTag)
        nbt
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
