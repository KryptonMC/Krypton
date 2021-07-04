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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTReader
import org.jglrxavpok.hephaistos.nbt.NBTString
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.jglrxavpok.hephaistos.nbt.NBTWriter
import org.kryptonmc.api.inventory.item.ItemStack
import org.kryptonmc.api.inventory.item.meta.ItemMeta
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.createFile
import org.kryptonmc.krypton.util.createTempFile
import org.kryptonmc.krypton.util.datafix.DATA_FIXER
import org.kryptonmc.krypton.util.datafix.FixType
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.util.nbt.getInt
import java.io.IOException
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.moveTo
import kotlin.io.path.outputStream

/**
 * Responsible for loading and saving player data files
 */
class PlayerDataManager(private val folder: Path) {

    private val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2)

    fun load(player: KryptonPlayer): CompletableFuture<Unit> = CompletableFuture.supplyAsync({
        val playerFile = folder.resolve("${player.uuid}.dat")
        if (!playerFile.exists()) {
            playerFile.createFile()
            return@supplyAsync
        }

        val nbt = try {
            NBTReader(playerFile.inputStream()).read() as NBTCompound
        } catch (exception: IOException) {
            LOGGER.warn("Failed to load player data for player ${player.name}!", exception)
            return@supplyAsync
        }

        val version = nbt.getInt("DataVersion", -1)
        player.load(DATA_FIXER.update(FixType.PLAYER.type, Dynamic(NBTOps, nbt), version, ServerInfo.WORLD_VERSION).value as NBTCompound)
    }, executor)

    fun save(player: KryptonPlayer): Future<*> = executor.submit {
        val data = player.save()

        // Create temp file and write data
        val temp = folder.createTempFile(player.uuid.toString(), ".dat")
        NBTWriter(temp.outputStream()).use { it.writeNamed("", data) }

        // Resolve actual file, and if it doesn't exist, rename the temp file
        val dataPath = folder.resolve("${player.uuid}.dat")
        if (!dataPath.exists()) {
            temp.moveTo(dataPath)
            return@submit
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

private fun ItemStack.toNBT(slot: Int) = NBTCompound()
    .setString("id", type.key().toString())
    .setByte("Slot", slot.toByte())
    .setByte("Count", amount.toByte())
    .apply { if (meta != null) set("display", meta!!.toNBT()) }

private fun ItemMeta.toNBT() = NBTCompound()
    .apply { if (displayName != null) setString("Name", GsonComponentSerializer.gson().serialize(displayName!!)) }
    .apply { if (lore.isNotEmpty()) set("Lore", lore.toNBT()) }

private fun List<Component>.toNBT() = NBTList<NBTString>(NBTTypes.TAG_String).apply {
    this@toNBT.map { add(NBTString(GsonComponentSerializer.gson().serialize(it))) }
}
