/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.entity.serializer

import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.util.nbt.getBlockPos
import org.kryptonmc.krypton.util.nbt.hasBlockPos
import org.kryptonmc.krypton.util.nbt.hasNumber
import org.kryptonmc.krypton.util.nbt.putBlockPosParts
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag

object LivingEntitySerializer : EntitySerializer<KryptonLivingEntity> {

    private val LOGGER = LogManager.getLogger()
    private const val ATTRIBUTES_TAG = "Attributes"
    private const val BRAIN_TAG = "Brain"
    private const val HEALTH_TAG = "Health"
    private const val GLIDING_TAG = "FallFlying"
    private const val ABSORPTION_TAG = "AbsorptionAmount"
    private const val DEATH_TIME = "DeathTime"
    private const val HURT_BY_TAG = "HurtByTimestamp"
    private const val HURT_TIME_TAG = "HurtTime"
    private const val TEAM_TAG = "Team"
    private const val SLEEPING_PREFIX = "Sleeping"

    override fun load(entity: KryptonLivingEntity, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        // AI stuff
        if (data.contains(ATTRIBUTES_TAG, ListTag.ID)) entity.attributes.load(data.getList(ATTRIBUTES_TAG, CompoundTag.ID))
        if (data.contains(BRAIN_TAG, CompoundTag.ID)) entity.brain.load(data.getCompound(BRAIN_TAG))

        // Values
        if (data.hasNumber(HEALTH_TAG)) entity.data.set(MetadataKeys.LivingEntity.HEALTH, data.getFloat(HEALTH_TAG))
        if (data.getBoolean(GLIDING_TAG)) entity.isGliding = true
        entity.absorption = data.getFloat(ABSORPTION_TAG).coerceAtLeast(0F)
        entity.deathTime = data.getShort(DEATH_TIME).toInt()
        entity.lastHurtTimestamp = data.getInt(HURT_BY_TAG)
        entity.hurtTime = data.getShort(HURT_TIME_TAG).toInt()

        // Scoreboard
        if (data.contains(TEAM_TAG, StringTag.ID)) {
            val teamName = data.getString(TEAM_TAG)
            val team = entity.world.scoreboard.getTeam(teamName)
            val wasAdded = team != null && entity.world.scoreboard.addMemberToTeam(entity.teamRepresentation, team)
            if (!wasAdded) {
                LOGGER.warn("Unable to add living entity ${entity.name} to team $teamName. This team may not exist.")
            }
        }

        // Sleeping coordinates
        if (data.hasBlockPos(SLEEPING_PREFIX)) {
            entity.sleepingPosition = data.getBlockPos(SLEEPING_PREFIX)
            entity.pose = Pose.SLEEPING
        }
    }

    override fun save(entity: KryptonLivingEntity): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putFloat(ABSORPTION_TAG, entity.absorption)
        put(ATTRIBUTES_TAG, entity.attributes.save())
        put(BRAIN_TAG, entity.brain.save())
        putShort(DEATH_TIME, entity.deathTime.toShort())
        putBoolean(GLIDING_TAG, entity.isGliding)
        putFloat(HEALTH_TAG, entity.health)
        putInt(HURT_BY_TAG, entity.lastHurtTimestamp)
        putShort(HURT_TIME_TAG, entity.hurtTime.toShort())
        entity.sleepingPosition?.let { putBlockPosParts(it, SLEEPING_PREFIX) }
    }
}
