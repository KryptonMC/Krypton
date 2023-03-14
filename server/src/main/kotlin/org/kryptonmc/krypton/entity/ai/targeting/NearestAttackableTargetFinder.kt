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

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import java.util.function.Predicate

open class NearestAttackableTargetFinder<T : KryptonLivingEntity>(
    mob: KryptonMob,
    private val targetType: Class<T>,
    randomInterval: Int,
    mustSee: Boolean,
    mustReach: Boolean,
    selector: Predicate<KryptonLivingEntity>?
) : AbstractTargetFinder(mob, mustSee, mustReach) {

    private val randomInterval = -Math.floorDiv(-randomInterval, 2)
    private val conditions = TargetingConditions.combat().range(getFollowRange()).selector(selector).build()

    constructor(mob: KryptonMob, targetType: Class<T>, mustSee: Boolean) : this(mob, targetType, 10, mustSee, false, null)

    constructor(mob: KryptonMob, targetType: Class<T>, mustSee: Boolean, mustReach: Boolean) : this(mob, targetType, 10, mustSee, mustReach, null)

    override fun canUse(): Boolean {
        return randomInterval <= 0 || mob.random.nextInt(randomInterval) == 0
    }

    override fun findTarget(): Entity? {
        val target = findNearestTarget() ?: return null
        mob.setTarget(target)
        return target
    }

    protected fun getSearchArea(distance: Double): BoundingBox = mob.boundingBox.inflate(distance, 4.0, distance)

    private fun findNearestTarget(): KryptonLivingEntity? {
        if (KryptonPlayer::class.java.isAssignableFrom(targetType)) {
            return findNearestPlayer(conditions, mob, mob.position)
        }
        val searchArea = getSearchArea(getFollowRange())
        val nearby = mob.world.getEntitiesOfType(targetType) { it.boundingBox.intersects(searchArea) }
        return findNearestEntity(nearby, conditions, mob, mob.position)
    }
}
