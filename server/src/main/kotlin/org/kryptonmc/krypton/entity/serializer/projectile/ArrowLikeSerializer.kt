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
import org.kryptonmc.api.entity.projectile.ArrowLike
import org.kryptonmc.krypton.entity.projectile.KryptonArrowLike
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.nbt.hasNumber
import org.kryptonmc.krypton.util.nbt.putNullable
import org.kryptonmc.krypton.util.nbt.putKeyed
import org.kryptonmc.krypton.world.block.BlockStateSerialization
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag

object ArrowLikeSerializer : EntitySerializer<KryptonArrowLike> {

    private const val CRIT_TAG = "crit"
    private const val DAMAGE_TAG = "damage"
    private const val IN_BLOCK_TAG = "inBlockState"
    private const val IN_GROUND_TAG = "inGround"
    private const val LIFE_TAG = "life"
    private const val PICKUP_TAG = "pickup"
    private const val PIERCE_LEVEL_TAG = "PierceLevel"
    private const val SHAKE_TAG = "shake"
    private const val SHOT_FROM_CROSSBOW_TAG = "ShotFromCrossbow"
    private const val SOUND_EVENT_TAG = "SoundEvent"

    private const val MAX_SHAKE_TIME = 255
    private val PICKUP_RULES = ArrowLike.PickupRule.values()

    override fun load(entity: KryptonArrowLike, data: CompoundTag) {
        ProjectileSerializer.load(entity, data)
        entity.isCritical = data.getBoolean(CRIT_TAG)
        if (data.hasNumber(DAMAGE_TAG)) entity.baseDamage = data.getDouble(DAMAGE_TAG)
        if (data.contains(IN_BLOCK_TAG, CompoundTag.ID)) entity.stuckInBlock = BlockStateSerialization.decode(data.getCompound(IN_BLOCK_TAG))
        entity.isInGround = data.getBoolean(IN_GROUND_TAG)
        entity.life = data.getShort(LIFE_TAG).toInt()

        val pickupOrdinal = data.getInt(PICKUP_TAG)
        val pickupIndex = if (pickupOrdinal in PICKUP_RULES.indices) pickupOrdinal else 0
        entity.pickupRule = PICKUP_RULES[pickupIndex]
        entity.piercingLevel = data.getByte(PIERCE_LEVEL_TAG).toInt()
        entity.shakeTime = data.getByte(SHAKE_TAG).toInt() and MAX_SHAKE_TIME
        entity.wasShotFromCrossbow = data.getBoolean(SHOT_FROM_CROSSBOW_TAG)

        if (data.contains(SOUND_EVENT_TAG, StringTag.ID)) {
            entity.sound = KryptonRegistries.SOUND_EVENT.get(Key.key(data.getString(SOUND_EVENT_TAG))) ?: entity.defaultHitGroundSound
        }
    }

    override fun save(entity: KryptonArrowLike): CompoundTag.Builder = ProjectileSerializer.save(entity).apply {
        putBoolean(CRIT_TAG, entity.isCritical)
        putDouble(DAMAGE_TAG, entity.baseDamage)
        putNullable(IN_BLOCK_TAG, entity.internalStuckInBlock, BlockStateSerialization::encode)
        putBoolean(IN_GROUND_TAG, entity.isInGround)
        putShort(LIFE_TAG, entity.life.toShort())
        putByte(PICKUP_TAG, entity.pickupRule.ordinal.toByte())
        putByte(PIERCE_LEVEL_TAG, entity.piercingLevel.toByte())
        putByte(SHAKE_TAG, entity.shakeTime.toByte())
        putBoolean(SHOT_FROM_CROSSBOW_TAG, entity.wasShotFromCrossbow)
        putKeyed(SOUND_EVENT_TAG, entity.sound)
    }
}
