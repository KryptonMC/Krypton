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
