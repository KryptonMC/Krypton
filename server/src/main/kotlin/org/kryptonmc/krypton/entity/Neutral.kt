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
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.KryptonWorld
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

    fun forgetCurrentTarget() {
        stopBeingAngry()
        startAngerTimer()
    }

    fun stopBeingAngry() {
        lastHurtByMob = null
        angerTarget = null
        target = null
        remainingAngerTime = 0
    }

    fun canAttack(target: KryptonLivingEntity): Boolean

    fun isAngryAt(target: KryptonLivingEntity): Boolean {
        if (!canAttack(target)) return false
        if (target.type === EntityTypes.PLAYER && isAngryAtAllPlayers(target.world)) return true
        return target.uuid == angerTarget
    }

    fun isAngryAtAllPlayers(world: KryptonWorld): Boolean = world.gameRules[GameRules.UNIVERSAL_ANGER] && isAngry && angerTarget == null

    fun loadAngerData(world: KryptonWorld, tag: CompoundTag) {
        remainingAngerTime = tag.getInt("AngerTime")
        if (!tag.hasUUID("AngryAt")) {
            angerTarget = null
            return
        }
        val targetId = tag.getUUID("AngryAt")
        angerTarget = targetId
        if (targetId == null) return
        val target = world.entityManager[targetId] ?: return
        if (target is KryptonMob) lastHurtByMob = target
        if (target.type === EntityTypes.PLAYER) lastHurtByPlayer = target as KryptonPlayer
    }

    fun saveAngerData(tag: CompoundTag.Builder) {
        tag.int("AngerTime", remainingAngerTime)
        if (angerTarget != null) tag.uuid("AngryAt", angerTarget!!)
    }

    fun updateAnger(world: KryptonWorld, isNotBee: Boolean) {
        val target = target
        val angerTarget = angerTarget
        if ((target == null || target.health <= 0F) && angerTarget != null && world.entityManager[angerTarget] is KryptonMob) {
            stopBeingAngry()
            return
        }
        if (target != null && target.uuid != angerTarget) {
            this.angerTarget = target.uuid
            startAngerTimer()
        }
        if (remainingAngerTime > 0 && (target == null || target.type !== EntityTypes.PLAYER || !isNotBee)) {
            remainingAngerTime -= 1
            if (remainingAngerTime == 0) stopBeingAngry()
        }
    }
}
