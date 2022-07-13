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
package org.kryptonmc.krypton.entity.serializer

import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.spongepowered.math.vector.Vector3i

object LivingEntitySerializer : EntitySerializer<KryptonLivingEntity> {

    private val LOGGER = logger<LivingEntitySerializer>()

    override fun load(entity: KryptonLivingEntity, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        // AI stuff
        if (data.contains("Attributes", ListTag.ID)) entity.attributes.load(data.getList("Attributes", CompoundTag.ID))
        if (data.contains("Brain", CompoundTag.ID)) entity.brain.load(data.getCompound("Brain"))

        // Values
        if (data.contains("Health", 99)) entity.health = data.getFloat("Health")
        if (data.getBoolean("FallFlying")) entity.isGliding = true
        entity.absorption = data.getFloat("AbsorptionAmount").coerceAtLeast(0F)
        entity.deathTime = data.getShort("DeathTime").toInt()
        entity.lastHurtTimestamp = data.getInt("HurtByTimestamp")
        entity.hurtTime = data.getShort("HurtTime").toInt()

        // Scoreboard
        if (data.contains("Team", StringTag.ID)) {
            val teamName = data.getString("Team")
            val team = entity.world.scoreboard.team(teamName)
            val wasAdded = team != null && entity.world.scoreboard.addMemberToTeam(entity.teamRepresentation, team)
            if (!wasAdded) LOGGER.warn("Unable to add living entity ${entity.name.toPlainText()} to team $teamName. This team may not exist.")
        }

        // Sleeping coordinates
        if (data.contains("SleepingX", 99) && data.contains("SleepingY", 99) && data.contains("SleepingZ", 99)) {
            entity.sleepingPosition = Vector3i(data.getInt("SleepingX"), data.getInt("SleepingY"), data.getInt("SleepingZ"))
            entity.pose = Pose.SLEEPING
        }
    }

    override fun save(entity: KryptonLivingEntity): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        float("AbsorptionAmount", entity.absorption)
        put("Attributes", entity.attributes.save())
        put("Brain", entity.brain.save())
        short("DeathTime", entity.deathTime.toShort())
        boolean("FallFlying", entity.isGliding)
        float("Health", entity.health)
        int("HurtByTimestamp", entity.lastHurtTimestamp)
        short("HurtTime", entity.hurtTime.toShort())
        val sleeping = entity.sleepingPosition
        if (sleeping != null) {
            int("SleepingX", sleeping.x())
            int("SleepingY", sleeping.y())
            int("SleepingZ", sleeping.z())
        }
    }
}
