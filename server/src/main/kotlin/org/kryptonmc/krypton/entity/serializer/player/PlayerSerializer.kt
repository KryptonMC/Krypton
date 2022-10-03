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
import org.kryptonmc.api.event.player.ChangeGameModeEvent
import org.kryptonmc.api.service.VanishService
import org.kryptonmc.api.service.provide
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.LivingEntitySerializer
import org.kryptonmc.krypton.service.KryptonVanishService
import org.kryptonmc.krypton.util.GameModes
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.serialization.nbt.NbtOps
import org.spongepowered.math.vector.Vector3i
import java.time.Instant

object PlayerSerializer : EntitySerializer<KryptonPlayer> {

    private val LOGGER = logger<KryptonPlayer>()

    override fun load(entity: KryptonPlayer, data: CompoundTag) {
        LivingEntitySerializer.load(entity, data)
        entity.updateGameMode(GameModes.fromId(data.getInt("playerGameType")) ?: GameMode.SURVIVAL, ChangeGameModeEvent.Cause.LOAD)
        if (data.contains("previousPlayerGameType", IntTag.ID)) entity.oldGameMode = GameModes.fromId(data.getInt("previousPlayerGameType"))
        entity.inventory.load(data.getList("Inventory", CompoundTag.ID))
        entity.inventory.heldSlot = data.getInt("SelectedItemSlot")
        entity.data.set(MetadataKeys.Player.SCORE, data.getInt("Score"))
        entity.foodLevel = data.getInt("foodLevel")
        entity.foodTickTimer = data.getInt("foodTickTimer")
        entity.foodExhaustionLevel = data.getFloat("foodExhaustionLevel")
        entity.foodSaturationLevel = data.getFloat("foodSaturationLevel")

        entity.abilities.load(data)
        entity.attribute(AttributeTypes.MOVEMENT_SPEED)!!.baseValue = entity.walkingSpeed.toDouble()

        // NBT data for entities sitting on the player's shoulders, e.g. parrots
        if (data.contains("ShoulderEntityLeft", CompoundTag.ID)) {
            entity.data.set(MetadataKeys.Player.LEFT_SHOULDER, data.getCompound("ShoulderEntityLeft"))
        }
        if (data.contains("ShoulderEntityRight", CompoundTag.ID)) {
            entity.data.set(MetadataKeys.Player.RIGHT_SHOULDER, data.getCompound("ShoulderEntityRight"))
        }

        // Respawn data
        if (data.contains("SpawnX", 99) && data.contains("SpawnY", 99) && data.contains("SpawnZ", 99)) {
            entity.respawnPosition = Vector3i(data.getInt("SpawnX"), data.getInt("SpawnY"), data.getInt("SpawnZ"))
            entity.respawnForced = data.getBoolean("SpawnForced")
            entity.respawnAngle = data.getFloat("SpawnAngle")
            if (data.contains("SpawnDimension", StringTag.ID)) {
                entity.respawnDimension = Codecs.DIMENSION.read(data.get("SpawnDimension"), NbtOps.INSTANCE)
                    .resultOrPartial(LOGGER::error)
                    .orElse(World.OVERWORLD)
            }
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
        int("playerGameType", entity.gameMode.ordinal)
        if (entity.oldGameMode != null) int("previousPlayerGameType", entity.oldGameMode!!.ordinal)

        int("DataVersion", KryptonPlatform.worldVersion)
        put("Inventory", entity.inventory.save())
        int("SelectedItemSlot", entity.inventory.heldSlot)
        int("Score", entity.data.get(MetadataKeys.Player.SCORE))
        int("foodLevel", entity.foodLevel)
        int("foodTickTimer", entity.foodTickTimer)
        float("foodExhaustionLevel", entity.foodExhaustionLevel)
        float("foodSaturationLevel", entity.foodSaturationLevel)
        entity.abilities.save(this)

        val leftShoulder = entity.data.get(MetadataKeys.Player.LEFT_SHOULDER)
        val rightShoulder = entity.data.get(MetadataKeys.Player.RIGHT_SHOULDER)
        if (!leftShoulder.isEmpty()) put("ShoulderEntityLeft", leftShoulder)
        if (!rightShoulder.isEmpty()) put("ShoulderEntityRight", rightShoulder)

        string("Dimension", entity.dimension.location.asString())
        entity.respawnPosition?.let { position ->
            int("SpawnX", position.x())
            int("SpawnY", position.y())
            int("SpawnZ", position.z())
            float("SpawnAngle", entity.respawnAngle)
            boolean("SpawnForced", entity.respawnForced)
            Codecs.KEY.encodeStart(entity.respawnDimension.location, NbtOps.INSTANCE)
                .resultOrPartial(LOGGER::error)
                .ifPresent { put("SpawnDimension", it) }
        }

        val rootVehicle = entity.rootVehicle
        val vehicle = entity.vehicle
        if (vehicle != null && rootVehicle !== entity && rootVehicle.hasExactlyOnePlayerPassenger) {
            compound("RootVehicle") {
                uuid("Attach", vehicle.uuid)
                put("Entity", EntityFactory.serializer(rootVehicle.type).save(rootVehicle).build())
            }
        }

        compound("krypton") {
            val vanishService = entity.server.servicesManager.provide<VanishService>()!!
            if (vanishService is KryptonVanishService) boolean("vanished", entity.isVanished)
            long("firstJoined", entity.firstJoined.toEpochMilli())
            long("lastJoined", entity.lastJoined.toEpochMilli())
        }
    }
}
