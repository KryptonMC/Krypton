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
