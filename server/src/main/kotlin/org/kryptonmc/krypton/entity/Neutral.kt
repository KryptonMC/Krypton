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
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.hasUUID
import org.kryptonmc.krypton.util.nbt.putNullable
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag
import java.util.UUID

interface Neutral {

    var remainingAngerTime: Int
    var angerTarget: UUID?
    var lastHurtByMob: KryptonLivingEntity?
    var lastHurtByPlayer: KryptonPlayer?
    var target: KryptonLivingEntity?

    val isAngry: Boolean
        get() = remainingAngerTime > 0

    fun startAngerTimer()

    fun stopBeingAngry() {
        lastHurtByMob = null
        angerTarget = null
        target = null
        remainingAngerTime = 0
    }

    companion object {

        private const val ANGER_TIME_TAG = "AngerTime"
        private const val ANGRY_AT_TAG = "AngryAt"

        @JvmStatic
        fun <E> loadAngerData(entity: E, data: CompoundTag) where E : KryptonEntity, E : Neutral {
            entity.remainingAngerTime = data.getInt(ANGER_TIME_TAG)
            if (!data.hasUUID(ANGRY_AT_TAG)) {
                entity.angerTarget = null
                return
            }
            val targetId = data.getUUID(ANGRY_AT_TAG)!!
            entity.angerTarget = targetId
            val target = entity.world.entityManager.get(targetId)
            if (target != null) {
                if (target is KryptonMob) entity.lastHurtByMob = target
                if (target.type === EntityTypes.PLAYER) entity.lastHurtByPlayer = target as KryptonPlayer
            }
        }

        @JvmStatic
        fun saveAngerData(entity: Neutral, data: CompoundTag.Builder): CompoundTag.Builder = data.apply {
            putInt(ANGER_TIME_TAG, entity.remainingAngerTime)
            putNullable(ANGRY_AT_TAG, entity.angerTarget, CompoundTag.Builder::putUUID)
        }
    }
}
