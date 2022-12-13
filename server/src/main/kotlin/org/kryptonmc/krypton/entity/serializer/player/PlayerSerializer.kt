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

import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.service.VanishService
import org.kryptonmc.api.service.provide
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.player.RespawnData
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.LivingEntitySerializer
import org.kryptonmc.krypton.service.KryptonVanishService
import org.kryptonmc.krypton.util.nbt.putDataVersion
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound
import java.time.Instant

object PlayerSerializer : EntitySerializer<KryptonPlayer> {

    private val LOGGER = LogManager.getLogger()

    // Vanilla tags
    private const val GAME_TYPE_TAG = "playerGameType"
    private const val PREVIOUS_GAME_TYPE_TAG = "previousPlayerGameType"
    private const val INVENTORY_TAG = "Inventory"
    private const val SELECTED_SLOT_TAG = "SelectedItemSlot"
    private const val SCORE_TAG = "Score"
    private const val LEFT_SHOULDER_TAG = "ShoulderEntityLeft"
    private const val RIGHT_SHOULDER_TAG = "ShoulderEntityRight"
    private const val DIMENSION_TAG = "Dimension"
    private const val ROOT_VEHICLE_TAG = "RootVehicle"
    private const val ATTACH_TAG = "Attach"
    private const val ENTITY_TAG = "Entity"

    // Custom krypton-only tags
    private const val KRYPTON_TAG = "krypton"
    private const val VANISHED_TAG = "vanished"
    private const val FIRST_JOINED_TAG = "firstJoined"
    private const val LAST_JOINED_TAG = "lastJoined"

    override fun load(entity: KryptonPlayer, data: CompoundTag) {
        LivingEntitySerializer.load(entity, data)
        entity.inventory.load(data.getList(INVENTORY_TAG, CompoundTag.ID))
        entity.inventory.heldSlot = data.getInt(SELECTED_SLOT_TAG)
        entity.data.set(MetadataKeys.Player.SCORE, data.getInt(SCORE_TAG))
        entity.hungerSystem.load(data)

        entity.abilities.load(data)
        entity.getAttribute(AttributeTypes.MOVEMENT_SPEED)!!.baseValue = entity.walkingSpeed.toDouble()

        // NBT data for entities sitting on the player's shoulders, e.g. parrots
        if (data.contains(LEFT_SHOULDER_TAG, CompoundTag.ID)) {
            entity.data.set(MetadataKeys.Player.LEFT_SHOULDER, data.getCompound(LEFT_SHOULDER_TAG))
        }
        if (data.contains(RIGHT_SHOULDER_TAG, CompoundTag.ID)) {
            entity.data.set(MetadataKeys.Player.RIGHT_SHOULDER, data.getCompound(RIGHT_SHOULDER_TAG))
        }

        // Respawn data
        entity.respawnData = RespawnData.load(data, LOGGER)

        if (data.contains(KRYPTON_TAG, CompoundTag.ID)) {
            entity.hasJoinedBefore = true
            val kryptonData = data.getCompound(KRYPTON_TAG)
            val vanishService = entity.server.servicesManager.provide<VanishService>()!!
            if (vanishService is KryptonVanishService && kryptonData.getBoolean(VANISHED_TAG)) vanishService.vanish(entity)
            entity.firstJoined = Instant.ofEpochMilli(kryptonData.getLong(FIRST_JOINED_TAG))
            entity.lastJoined = Instant.ofEpochMilli(kryptonData.getLong(LAST_JOINED_TAG))
        }
    }

    override fun save(entity: KryptonPlayer): CompoundTag.Builder = LivingEntitySerializer.save(entity).apply {
        putInt(GAME_TYPE_TAG, entity.gameMode.ordinal)
        entity.gameModeSystem.previousGameMode?.let { putInt(PREVIOUS_GAME_TYPE_TAG, it.ordinal) }

        putDataVersion()
        put(INVENTORY_TAG, entity.inventory.save())
        putInt(SELECTED_SLOT_TAG, entity.inventory.heldSlot)
        putInt(SCORE_TAG, entity.data.get(MetadataKeys.Player.SCORE))
        entity.hungerSystem.save(this)
        entity.abilities.save(this)

        val leftShoulder = entity.data.get(MetadataKeys.Player.LEFT_SHOULDER)
        val rightShoulder = entity.data.get(MetadataKeys.Player.RIGHT_SHOULDER)
        if (!leftShoulder.isEmpty) put(LEFT_SHOULDER_TAG, leftShoulder)
        if (!rightShoulder.isEmpty) put(RIGHT_SHOULDER_TAG, rightShoulder)

        putString(DIMENSION_TAG, entity.dimension.location.asString())
        entity.respawnData?.save(this, LOGGER)

        val rootVehicle = entity.vehicleSystem.rootVehicle()
        val vehicle = entity.vehicle
        if (vehicle != null && rootVehicle !== entity && rootVehicle.vehicleSystem.hasExactlyOnePlayerPassenger()) {
            compound(ROOT_VEHICLE_TAG) {
                putUUID(ATTACH_TAG, vehicle.uuid)
                put(ENTITY_TAG, rootVehicle.saveWithPassengers().build())
            }
        }

        compound(KRYPTON_TAG) {
            val vanishService = entity.server.servicesManager.provide<VanishService>()!!
            if (vanishService is KryptonVanishService) putBoolean(VANISHED_TAG, entity.isVanished)
            putLong(FIRST_JOINED_TAG, entity.firstJoined.toEpochMilli())
            putLong(LAST_JOINED_TAG, entity.lastJoined.toEpochMilli())
        }
    }
}
