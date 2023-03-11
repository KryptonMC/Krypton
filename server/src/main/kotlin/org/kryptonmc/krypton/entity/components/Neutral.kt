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
package org.kryptonmc.krypton.entity.components

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.KryptonMob
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

    val isAngry: Boolean
        get() = remainingAngerTime > 0

    fun startAngerTimer()

    fun stopBeingAngry() {
        lastHurtByMob = null
        angerTarget = null
        setTarget(null)
        remainingAngerTime = 0
    }

    fun setTarget(target: KryptonLivingEntity?)

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
            val targetId = data.getUUID(ANGRY_AT_TAG)
            entity.angerTarget = targetId
            val target = entity.world.entityManager.getByUUID(targetId)
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
