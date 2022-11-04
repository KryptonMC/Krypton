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
package org.kryptonmc.krypton.entity.serializer.player

import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.service.VanishService
import org.kryptonmc.api.service.provide
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.player.RespawnData
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.LivingEntitySerializer
import org.kryptonmc.krypton.service.KryptonVanishService
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound
import org.kryptonmc.serialization.nbt.NbtOps
import org.spongepowered.math.vector.Vector3i
import java.time.Instant

object PlayerSerializer : EntitySerializer<KryptonPlayer> {

    private val LOGGER = logger<KryptonPlayer>()

    override fun load(entity: KryptonPlayer, data: CompoundTag) {
        LivingEntitySerializer.load(entity, data)
        entity.inventory.load(data.getList("Inventory", CompoundTag.ID))
        entity.inventory.heldSlot = data.getInt("SelectedItemSlot")
        entity.data.set(MetadataKeys.Player.SCORE, data.getInt("Score"))
        entity.hungerSystem.load(data)

        entity.abilities.load(data)
        entity.getAttribute(AttributeTypes.MOVEMENT_SPEED)!!.baseValue = entity.walkingSpeed.toDouble()

        // NBT data for entities sitting on the player's shoulders, e.g. parrots
        if (data.contains("ShoulderEntityLeft", CompoundTag.ID)) {
            entity.data.set(MetadataKeys.Player.LEFT_SHOULDER, data.getCompound("ShoulderEntityLeft"))
        }
        if (data.contains("ShoulderEntityRight", CompoundTag.ID)) {
            entity.data.set(MetadataKeys.Player.RIGHT_SHOULDER, data.getCompound("ShoulderEntityRight"))
        }

        // Respawn data
        if (data.contains("SpawnX", 99) && data.contains("SpawnY", 99) && data.contains("SpawnZ", 99)) {
            val position = Vector3i(data.getInt("SpawnX"), data.getInt("SpawnY"), data.getInt("SpawnZ"))
            val dimension = if (data.contains("SpawnDimension", StringTag.ID)) {
                Codecs.DIMENSION.read(data.get("SpawnDimension"), NbtOps.INSTANCE).resultOrPartial(LOGGER::error).orElse(World.OVERWORLD)
            } else {
                World.OVERWORLD
            }
            entity.respawnData = RespawnData(position, dimension, data.getFloat("SpawnAngle"), data.getBoolean("SpawnForced"))
        }

        if (data.contains("krypton", CompoundTag.ID)) {
            entity.hasJoinedBefore = true
            val kryptonData = data.getCompound("krypton")
            val vanishService = entity.server.servicesManager.provide<VanishService>()!!
            if (vanishService is KryptonVanishService && kryptonData.getBoolean("vanished")) vanishService.vanish(entity)
            entity.firstJoined = Instant.ofEpochMilli(kryptonData.getLong("firstJoined"))
        }
    }

    override fun save(entity: KryptonPlayer): CompoundTag.Builder = LivingEntitySerializer.save(entity).apply {
        putInt("playerGameType", entity.gameMode.ordinal)
        if (entity.oldGameMode != null) putInt("previousPlayerGameType", entity.oldGameMode!!.ordinal)

        putInt("DataVersion", KryptonPlatform.worldVersion)
        put("Inventory", entity.inventory.save())
        putInt("SelectedItemSlot", entity.inventory.heldSlot)
        putInt("Score", entity.data.get(MetadataKeys.Player.SCORE))
        entity.hungerSystem.save(this)
        entity.abilities.save(this)

        val leftShoulder = entity.data.get(MetadataKeys.Player.LEFT_SHOULDER)
        val rightShoulder = entity.data.get(MetadataKeys.Player.RIGHT_SHOULDER)
        if (!leftShoulder.isEmpty) put("ShoulderEntityLeft", leftShoulder)
        if (!rightShoulder.isEmpty) put("ShoulderEntityRight", rightShoulder)

        putString("Dimension", entity.dimension.location.asString())
        entity.respawnData?.save(this, LOGGER)

        val rootVehicle = entity.vehicleSystem.rootVehicle()
        val vehicle = entity.vehicle
        if (vehicle != null && rootVehicle !== entity && rootVehicle.vehicleSystem.hasExactlyOnePlayerPassenger()) {
            compound("RootVehicle") {
                putUUID("Attach", vehicle.uuid)
                put("Entity", rootVehicle.saveWithPassengers().build())
            }
        }

        compound("krypton") {
            val vanishService = entity.server.servicesManager.provide<VanishService>()!!
            if (vanishService is KryptonVanishService) putBoolean("vanished", entity.isVanished)
            putLong("firstJoined", entity.firstJoined.toEpochMilli())
            putLong("lastJoined", entity.lastJoined.toEpochMilli())
        }
    }
}
