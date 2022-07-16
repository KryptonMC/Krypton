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
package org.kryptonmc.krypton.entity.serializer.projectile

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.projectile.ArrowLike
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.projectile.KryptonArrowLike
import org.kryptonmc.krypton.entity.projectile.KryptonTrident
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.world.block.toBlock
import org.kryptonmc.krypton.world.block.toNBT
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag

object ArrowLikeSerializer : EntitySerializer<KryptonArrowLike> {

    private val PICKUP_RULES = ArrowLike.PickupRule.values()

    override fun load(entity: KryptonArrowLike, data: CompoundTag) {
        ProjectileSerializer.load(entity, data)
        entity.isCritical = data.getBoolean("crit")
        if (data.contains("damage", 99)) entity.baseDamage = data.getDouble("damage")
        if (data.contains("inBlockState", CompoundTag.ID)) entity.stuckInBlock = data.getCompound("inBlockState").toBlock()
        entity.isInGround = data.getBoolean("inGround")
        entity.life = data.getShort("life").toInt()

        val pickupOrdinal = data.getInt("pickup")
        val pickupIndex = if (pickupOrdinal in PICKUP_RULES.indices) pickupOrdinal else 0
        entity.pickupRule = PICKUP_RULES[pickupIndex]
        entity.piercingLevel = data.getByte("PierceLevel").toInt()
        entity.shakeTime = data.getByte("shake").toInt() and 255
        entity.wasShotFromCrossbow = data.getBoolean("ShotFromCrossbow")

        if (data.contains("SoundEvent", StringTag.ID)) {
            val defaultHitGroundSound = if (entity is KryptonTrident) SoundEvents.TRIDENT_HIT_GROUND else SoundEvents.ARROW_HIT
            entity.sound = Registries.SOUND_EVENT[Key.key(data.getString("SoundEvent"))] ?: defaultHitGroundSound
        }
    }

    override fun save(entity: KryptonArrowLike): CompoundTag.Builder = ProjectileSerializer.save(entity).apply {
        boolean("crit", entity.isCritical)
        double("damage", entity.baseDamage)
        if (entity.stuckInBlock != null) put("inBlockState", entity.stuckInBlock!!.toNBT())
        boolean("inGround", entity.isInGround)
        short("life", entity.life.toShort())
        byte("pickup", entity.pickupRule.ordinal.toByte())
        byte("PierceLevel", entity.piercingLevel.toByte())
        byte("shake", entity.shakeTime.toByte())
        boolean("ShotFromCrossbow", entity.wasShotFromCrossbow)
        string("SoundEvent", entity.sound.key().asString())
    }
}
