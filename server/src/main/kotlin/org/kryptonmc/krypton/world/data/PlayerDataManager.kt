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
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTDouble
import org.jglrxavpok.hephaistos.nbt.NBTFloat
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTReader
import org.jglrxavpok.hephaistos.nbt.NBTString
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.jglrxavpok.hephaistos.nbt.NBTWriter
import org.kryptonmc.api.entity.player.Abilities
import org.kryptonmc.api.inventory.item.ItemStack
import org.kryptonmc.api.inventory.item.Material
import org.kryptonmc.api.inventory.item.meta.ItemMeta
import org.kryptonmc.api.util.toKey
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.Location
import org.kryptonmc.krypton.ServerInfo
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.memory.EmptyBrain
import org.kryptonmc.krypton.util.serialize
import org.kryptonmc.krypton.util.createFile
import org.kryptonmc.krypton.util.createTempFile
import org.kryptonmc.krypton.util.datafix.DATA_FIXER
import org.kryptonmc.krypton.util.datafix.FixType
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.util.nbt.getBoolean
import org.kryptonmc.krypton.util.nbt.getByte
import org.kryptonmc.krypton.util.nbt.getInt
import org.kryptonmc.krypton.util.nbt.getList
import org.kryptonmc.krypton.util.nbt.getString
import org.kryptonmc.krypton.util.nbt.setBoolean
import org.kryptonmc.krypton.util.toLocation
import org.kryptonmc.krypton.world.KryptonWorld
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.copyTo
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.moveTo
import kotlin.io.path.outputStream

/**
 * Responsible for loading and saving player data files
 */
class PlayerDataManager(private val folder: Path) {

    fun load(world: KryptonWorld, player: KryptonPlayer) {
        player.world = world

        val playerFile = folder.resolve("${player.uuid}.dat")
        if (!playerFile.exists()) {
            playerFile.createFile()
            applyDefaults(world, player)
            return
        }

        val nbt = try {
            NBTReader(playerFile.inputStream()).read() as NBTCompound
        } catch (exception: IOException) {
            return
        }

        val version = nbt.getInt("DataVersion", -1)
        val data = DATA_FIXER.update(FixType.PLAYER.type, Dynamic(NBTOps, nbt), version, ServerInfo.WORLD_VERSION).value as NBTCompound

        player.attributes.load(data.getList("Attributes", NBTList(NBTTypes.TAG_Compound)))

        val inventoryItems = data.getList<NBTCompound>("Inventory", NBTList(NBTTypes.TAG_Compound)).associate {
            val type = Material.KEYS.value(it.getString("id", "").toKey())!!
            val slot = it.getByte("Slot", 0)
            val count = it.getByte("Count", 0)
            slot.toInt() to ItemStack(type, count.toInt())
        }
        player.inventory.populate(inventoryItems)

        val position = data.getList<NBTDouble>("Pos", NBTList(NBTTypes.TAG_Double)).map { it.value }
        val rotation = data.getList<NBTFloat>("Rotation", NBTList(NBTTypes.TAG_Float)).map { it.value }

        player.location = Location(position[0], position[1], position[2], rotation[0], rotation[1])
        player.oldGamemode = Gamemode.fromId(data.getInt("previousPlayerGameType", -1))
        player.gamemode = Gamemode.fromId(data.getInt("playerGameType", 0)) ?: Gamemode.SURVIVAL
        player.inventory.heldSlot = data.getInt("SelectedItemSlot", 0)
        player.dimension = data.getString("Dimension", "minecraft:overworld").toKey()
        player.isOnGround = data.getBoolean("OnGround", true)
    }

    fun save(player: KryptonPlayer) {
        val helmet = player.inventory.helmet
        val chestplate = player.inventory.chestplate
        val leggings = player.inventory.leggings
        val boots = player.inventory.boots
        val offHand = player.inventory.offHand

        val inventory = ((player.inventory.hotbar + player.inventory.main).mapIndexed { index, item ->
            if (item == null) return@mapIndexed null
            item.toNBT(index)
        }.filterNotNull() as MutableList<NBTCompound>).apply {
            if (helmet != null) add(helmet.toNBT(103))
            if (chestplate != null) add(chestplate.toNBT(102))
            if (leggings != null) add(leggings.toNBT(101))
            if (boots != null) add(boots.toNBT(100))
            if (offHand != null) add(offHand.toNBT(-106))
        }

        val data = NBTCompound()
            .apply { EmptyBrain.write().iterator().asSequence().forEach { set(it.first, it.second) } }
            .setShort("SleepTimer", 0)
            .setBoolean("SpawnForced", false)
            .set("Attributes", player.attributes.save())
            .setBoolean("Invulnerable", player.abilities.isInvulnerable)
            .setFloat("AbsorptionAmount", player.absorption)
            .set("abilities", NBTCompound()
                .setFloat("walkSpeed", player.abilities.walkSpeed)
                .setBoolean("instabuild", player.abilities.canInstantlyBuild)
                .setBoolean("mayfly", player.abilities.canFly)
                .setBoolean("invulnerable", player.abilities.isInvulnerable)
                .setBoolean("mayBuild", player.abilities.canBuild)
                .setBoolean("flying", player.abilities.isFlying)
                .setFloat("flySpeed", player.abilities.flyingSpeed)
            )
            .setFloat("FallDistance", 0F)
            .set("UUID", player.uuid.serialize())
            .setInt("SpawnX", 0)
            .setInt("SpawnY", 0)
            .setInt("SpawnZ", 0)
            .setFloat("SpawnAngle", 0F)
            .setShort("Air", player.airSupply.toShort())
            .setInt("Score", 0)
            .set("Pos", NBTList<NBTDouble>(NBTTypes.TAG_Double).apply {
                add(NBTDouble(player.location.x))
                add(NBTDouble(player.location.y))
                add(NBTDouble(player.location.z))
            })
            .setInt("previousPlayerGameType", player.oldGamemode?.ordinal ?: -1)
            .setInt("DataVersion", ServerInfo.WORLD_VERSION)
            .setInt("SelectedItemSlot", player.inventory.heldSlot)
            .setShort("HurtTime", 0)
            .set("Inventory", NBTList<NBTCompound>(NBTTypes.TAG_Compound).apply { inventory.forEach { add(it) } })
            .setBoolean("FallFlying", player.isFlying)
            .setInt("playerGameType", player.gamemode.ordinal)
            .setString("SpawnDimension", player.dimension.toString())
            .setFloat("Health", player.health)
            .setBoolean("OnGround", player.isOnGround)
            .setString("Dimension", player.dimension.toString())
            .set("Rotation", NBTList<NBTFloat>(NBTTypes.TAG_Float).apply {
                add(NBTFloat(player.location.yaw))
                add(NBTFloat(player.location.pitch))
            })

        val temp = folder.createTempFile(player.uuid.toString(), ".dat")
        NBTWriter(temp.outputStream()).use { it.writeNamed("", data) }
        val dataPath = folder.resolve("${player.uuid}.dat")
        if (!dataPath.exists()) {
            temp.copyTo(dataPath)
            return
        }
        val oldDataPath = folder.resolve("${player.uuid}.dat_old").apply { deleteIfExists() }
        dataPath.moveTo(oldDataPath)
        dataPath.deleteIfExists()
        temp.moveTo(dataPath)
    }

    // this ensures player data is always set, even when the player doesn't have any
    // persisted data
    private fun applyDefaults(world: KryptonWorld, player: KryptonPlayer) {
        player.location = world.spawnLocation.toLocation(world.spawnAngle)
        player.gamemode = world.gamemode
        player.dimension = OVERWORLD

        player.abilities = when (world.gamemode) {
            Gamemode.SURVIVAL, Gamemode.ADVENTURE -> Abilities()
            Gamemode.CREATIVE -> Abilities(isInvulnerable = true, isFlying = true, canInstantlyBuild = true)
            Gamemode.SPECTATOR -> Abilities(isInvulnerable = true, canFly = true, isFlying = true)
        }
    }

    companion object {

        private val OVERWORLD = key("overworld")
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
