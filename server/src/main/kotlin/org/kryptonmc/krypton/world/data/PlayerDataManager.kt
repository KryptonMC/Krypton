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
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.BinaryTagIO.Compression.GZIP
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.DoubleBinaryTag
import net.kyori.adventure.nbt.FloatBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
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
import org.kryptonmc.krypton.util.datafix.DATA_FIXER
import org.kryptonmc.krypton.util.datafix.FixType
import org.kryptonmc.krypton.util.nbt.BinaryTagOps
import org.kryptonmc.krypton.util.toLocation
import org.kryptonmc.krypton.world.KryptonWorld
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.exists

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
            BinaryTagIO.unlimitedReader().read(playerFile, GZIP)
        } catch (exception: IOException) {
            return
        }

        val version = nbt.getInt("DataVersion", -1)
        val data = DATA_FIXER.update(FixType.PLAYER.type, Dynamic(BinaryTagOps, nbt), version, ServerInfo.WORLD_VERSION).value as CompoundBinaryTag

        player.attributes.load(data.getList("Attributes"))

        val inventoryItems = data.getList("Inventory").associate { item ->
            (item as CompoundBinaryTag).let {
                val type = Material.KEYS.value(it.getString("id").toKey())!!
                val slot = it.getByte("Slot")
                val count = it.getByte("Count")
                slot.toInt() to ItemStack(type, count.toInt())
            }
        }
        player.inventory.populate(inventoryItems)

        val position = data.getList("Pos").map { (it as DoubleBinaryTag).value() }
        val rotation = data.getList("Rotation").map { (it as FloatBinaryTag).value() }

        player.location = Location(position[0], position[1], position[2], rotation[0], rotation[1])
        player.oldGamemode = Gamemode.fromId(data.getInt("previousPlayerGameType", -1))
        player.gamemode = Gamemode.fromId(data.getInt("playerGameType", 0)) ?: Gamemode.SURVIVAL
        player.inventory.heldSlot = data.getInt("SelectedItemSlot")
        player.dimension = data.getString("Dimension").toKey()
        player.isOnGround = data.getBoolean("OnGround")
    }

    fun save(player: KryptonPlayer) {
        val playerFile = folder.resolve("${player.uuid}.dat").createFile()

        val helmet = player.inventory.helmet
        val chestplate = player.inventory.chestplate
        val leggings = player.inventory.leggings
        val boots = player.inventory.boots
        val offHand = player.inventory.offHand

        val inventory = ((player.inventory.hotbar + player.inventory.main).mapIndexed { index, item ->
            if (item == null) return@mapIndexed null
            item.toNBT(index)
        }.filterNotNull() as MutableList<CompoundBinaryTag>).apply {
            if (helmet != null) add(helmet.toNBT(103))
            if (chestplate != null) add(chestplate.toNBT(102))
            if (leggings != null) add(leggings.toNBT(101))
            if (boots != null) add(boots.toNBT(100))
            if (offHand != null) add(offHand.toNBT(-106))
        }

        BinaryTagIO.writer().write(CompoundBinaryTag.builder()
            .put(EmptyBrain.write())
            .putShort("SleepTimer", 0)
            .putBoolean("SpawnForced", false)
            .put("Attributes", player.attributes.save())
            .putBoolean("Invulnerable", player.abilities.isInvulnerable)
            .putFloat("AbsorptionAmount", 0F)
            .put("abilities", CompoundBinaryTag.builder()
                .putFloat("walkSpeed", player.abilities.walkSpeed)
                .putBoolean("instabuild", player.abilities.canInstantlyBuild)
                .putBoolean("mayfly", player.abilities.canFly)
                .putBoolean("invulnerable", player.abilities.isInvulnerable)
                .putBoolean("mayBuild", player.abilities.canBuild)
                .putBoolean("flying", player.abilities.isFlying)
                .putFloat("flySpeed", player.abilities.flyingSpeed)
                .build())
            .putFloat("FallDistance", 0F)
            .put("UUID", player.uuid.serialize())
            .putInt("SpawnX", 0)
            .putInt("SpawnY", 0)
            .putInt("SpawnZ", 0)
            .putFloat("SpawnAngle", 0F)
            .putShort("Air", player.airSupply.toShort())
            .putInt("Score", 0)
            .put("Pos", ListBinaryTag.builder()
                .add(DoubleBinaryTag.of(player.location.x))
                .add(DoubleBinaryTag.of(player.location.y))
                .add(DoubleBinaryTag.of(player.location.z))
                .build())
            .putInt("previousPlayerGameType", player.oldGamemode?.ordinal ?: -1)
            .putInt("DataVersion", ServerInfo.WORLD_VERSION)
            .putInt("SelectedItemSlot", player.inventory.heldSlot)
            .putShort("HurtTime", 0)
            .put("Inventory", ListBinaryTag.from(inventory))
            .putBoolean("FallFlying", player.isFlying)
            .putInt("playerGameType", player.gamemode.ordinal)
            .putString("SpawnDimension", player.dimension.toString())
            .putFloat("Health", player.health)
            .putBoolean("OnGround", player.isOnGround)
            .putString("Dimension", player.dimension.toString())
            .put("Rotation", ListBinaryTag.builder()
                .add(FloatBinaryTag.of(player.location.yaw))
                .add(FloatBinaryTag.of(player.location.pitch))
                .build())
            .build(), playerFile, GZIP)
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

private fun ItemStack.toNBT(slot: Int) = CompoundBinaryTag.builder()
    .putString("id", type.key().toString())
    .putByte("Slot", slot.toByte())
    .putByte("Count", amount.toByte())
    .apply { if (meta != null) put("display", meta!!.toNBT()) }
    .build()

private fun ItemMeta.toNBT() = CompoundBinaryTag.builder()
    .apply { if (displayName != null) putString("Name", GsonComponentSerializer.gson().serialize(displayName!!)) }
    .apply { if (lore.isNotEmpty()) put("Lore", lore.toNBT()) }
    .build()

private fun List<Component>.toNBT() =
    ListBinaryTag.of(BinaryTagTypes.STRING, map { StringBinaryTag.of(GsonComponentSerializer.gson().serialize(it)) })
