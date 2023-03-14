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
package org.kryptonmc.krypton.entity.ai.targeting

import org.kryptonmc.api.entity.ai.goal.TargetFinder
import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.player.KryptonPlayer

@Suppress("UnusedPrivateMember")
abstract class AbstractTargetFinder(
    protected val mob: KryptonMob,
    protected val mustSee: Boolean,
    private val mustReach: Boolean
) : TargetFinder {

    protected var targetMob: KryptonMob? = null

    override fun shouldRemove(): Boolean {
        var target = mob.target()
        if (target == null) target = targetMob
        if (target == null) return true
        if (!mob.canAttack(target)) return true

        // Mobs on the same team won't target each other
        val team = mob.team
        val targetTeam = target.team
        if (team != null && targetTeam == team) return true

        val followRange = getFollowRange()
        if (mob.position.distanceSquared(target.position) > followRange * followRange) return true

        // TODO: Check line of sight if must see
        mob.setTarget(target)
        return false
    }

    override fun onRemove() {
        mob.setTarget(null)
    }

    protected open fun getFollowRange(): Double = mob.attributes.getValue(KryptonAttributeTypes.FOLLOW_RANGE)

    protected fun <T : KryptonLivingEntity> findNearestEntity(nearby: Collection<T>, conditions: TargetingConditions, attacker: KryptonLivingEntity?,
                                                              position: Position): T? {
        var nearestDistance = Double.MAX_VALUE
        var result: T? = null

        for (entity in nearby) {
            if (!conditions.canTarget(attacker, entity)) continue
            val distance = entity.position.distanceSquared(position)
            if (distance < nearestDistance) {
                nearestDistance = distance
                result = entity
            }
        }
        return result
    }

    protected fun findNearestPlayer(conditions: TargetingConditions, attacker: KryptonLivingEntity?, position: Position): KryptonPlayer? {
        return findNearestEntity(mob.world.players, conditions, attacker, position)
    }
}
