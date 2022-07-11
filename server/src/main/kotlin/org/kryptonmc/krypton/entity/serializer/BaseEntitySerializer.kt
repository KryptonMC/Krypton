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

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.adventure.toJson
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.buildCompound
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d
import kotlin.math.abs

object BaseEntitySerializer : EntitySerializer<KryptonEntity> {

    override fun load(entity: KryptonEntity, data: CompoundTag) {
        entity.air = data.getShort("Air").toInt()
        if (data.contains("CustomName", StringTag.ID)) {
            entity.customName = GsonComponentSerializer.gson().deserialize(data.getString("CustomName"))
        }
        entity.isCustomNameVisible = data.getBoolean("CustomNameVisible")
        entity.fallDistance = data.getFloat("FallDistance")
        entity.fireTicks = data.getShort("Fire").toInt()
        entity.isGlowing = data.getBoolean("Glowing")
        entity.isInvulnerable = data.getBoolean("Invulnerable")

        val motion = data.getList("Motion", DoubleTag.ID)
        entity.velocity = Vector3d(motion.getMotionValue(0), motion.getMotionValue(1), motion.getMotionValue(2))

        entity.hasGravity = !data.getBoolean("NoGravity")
        entity.isOnGround = data.getBoolean("OnGround")

        val location = data.getList("Pos", DoubleTag.ID)
        val rotation = data.getList("Rotation", FloatTag.ID)
        entity.location = Vector3d(location.getDouble(0), location.getDouble(1), location.getDouble(2))
        entity.rotation = Vector2f(rotation.getFloat(0), rotation.getFloat(1))

        entity.isSilent = data.getBoolean("Silent")
        entity.frozenTicks = data.getInt("TicksFrozen")
        if (data.hasUUID("UUID")) entity.uuid = data.getUUID("UUID")!!
    }

    override fun save(entity: KryptonEntity): CompoundTag.Builder = buildCompound {
        // Display name
        if (entity.isCustomNameVisible) boolean("CustomNameVisible", true)
        val customName = entity.customName
        if (customName != null) string("CustomName", customName.toJson())

        // Flags
        if (entity.isGlowing) boolean("Glowing", true)
        if (entity.isInvulnerable) boolean("Invulnerable", true)
        if (!entity.hasGravity) boolean("NoGravity", true)
        boolean("OnGround", entity.isOnGround)
        if (entity.isSilent) boolean("Silent", true)

        // Positioning
        list("Motion", DoubleTag.ID, DoubleTag.of(entity.velocity.x()), DoubleTag.of(entity.velocity.y()), DoubleTag.of(entity.velocity.z()))
        list("Pos", DoubleTag.ID, DoubleTag.of(entity.location.x()), DoubleTag.of(entity.location.y()), DoubleTag.of(entity.location.z()))
        list("Rotation", FloatTag.ID, FloatTag.of(entity.rotation.x()), FloatTag.of(entity.rotation.y()))

        // Identification
        if (entity !is KryptonPlayer) string("id", entity.type.key().asString())
        uuid("UUID", entity.uuid)

        // Miscellaneous
        short("Air", entity.air.toShort())
        short("Fire", entity.fireTicks.toShort())
        int("TicksFrozen", entity.frozenTicks)
        float("FallDistance", entity.fallDistance)
    }
}

private fun ListTag.getMotionValue(index: Int): Double {
    val value = getDouble(index)
    if (abs(value) <= 10) return value
    return 0.0
}
