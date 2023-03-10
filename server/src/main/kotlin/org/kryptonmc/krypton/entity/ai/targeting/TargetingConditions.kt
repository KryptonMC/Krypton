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

import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.krypton.entity.KryptonArmorStand
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import java.util.function.Predicate
import kotlin.math.max

@Suppress("UnusedPrivateMember")
class TargetingConditions(
    private val isCombat: Boolean,
    private val range: Double,
    private val checkLineOfSight: Boolean,
    private val testInvisible: Boolean,
    private val selector: Predicate<KryptonLivingEntity>?
) {

    fun canTarget(attacker: KryptonLivingEntity?, target: KryptonLivingEntity): Boolean {
        if (attacker === target) return false // Can't attack ourselves
        if (!target.canBeSeenByAnyone()) return false // Target cannot be seen at all by anyone
        if (selector != null && !selector.test(target)) return false // Target doesn't match the selector

        if (attacker == null) {
            if (!isCombat) return true
            // Combat targeting isn't allowed if the target cannot be seen as an enemy or the difficulty is peaceful
            return target.canBeSeenAsEnemy() && target.world.difficulty != Difficulty.PEACEFUL
        }

        if (isCombat && !attacker.canAttack(target)) return false

        if (range > 0.0) {
            val visiblePercent = if (testInvisible) getVisibilityPercent(target, attacker) else 1.0
            val newRange = max(range * visiblePercent, 2.0)
            val distance = attacker.position.distanceSquared(target.position)
            if (distance > newRange * newRange) return false // Out of range
        }

        // TODO: Check mob has line of sight if required
        return true
    }

    private fun getVisibilityPercent(entity: KryptonLivingEntity, viewer: KryptonLivingEntity): Double {
        var result = 1.0

        // Hostile mobs are less likely to see targets when they are sneaking
        if (entity.isSneaking) result *= 0.8

        // Hostile mobs can only see visible players when they are wearing enough armour
        if (entity.isInvisible) {
            var armorCoverPercentage = getArmorCoverPercentage(entity)
            if (armorCoverPercentage < 0.1F) armorCoverPercentage = 0.1F
            result *= 0.7 * armorCoverPercentage
        }

        // The detection range of hostile mobs is halved when the target is wearing a head of the mob's type
        val head = entity.getEquipment(EquipmentSlot.HEAD)
        if (hasHeadForType(viewer.type, head)) result *= 0.5

        return result
    }

    private fun getArmorCoverPercentage(entity: KryptonLivingEntity): Float {
        val armor = when (entity) {
            is KryptonArmorStand -> entity.armorItems
            is KryptonMob -> entity.armorItems
            is KryptonPlayer -> entity.inventory.armor
            else -> return 0F
        }

        var count = 0
        var nonEmpty = 0
        for (item in armor) {
            if (!item.isEmpty()) nonEmpty++
            count++
        }

        return if (count > 0) nonEmpty.toFloat() / count.toFloat() else 0F
    }

    private fun hasHeadForType(type: KryptonEntityType<*>, head: KryptonItemStack): Boolean {
        return type === KryptonEntityTypes.SKELETON && head.eq(ItemTypes.SKELETON_SKULL.get()) ||
                type === KryptonEntityTypes.ZOMBIE && head.eq(ItemTypes.ZOMBIE_HEAD.get()) ||
//                type === KryptonEntityTypes.PIGLIN && head.eq(ItemTypes.PIGLIN_HEAD.get()) ||
//                type === KryptonEntityTypes.PIGLIN_BRUTE && head.eq(ItemTypes.PIGLIN_HEAD.get()) ||
                type === KryptonEntityTypes.CREEPER && head.eq(ItemTypes.CREEPER_HEAD.get())
    }

    class Builder(private val combat: Boolean) {

        private var range = -1.0
        private var lineOfSight = true
        private var invisibles = true
        private var selector: Predicate<KryptonLivingEntity>? = null

        fun range(amount: Double): Builder = apply { range = amount }

        fun ignoreLineOfSight(): Builder = apply { lineOfSight = false }

        fun ignoreInvisibles(): Builder = apply { invisibles = false }

        fun selector(value: Predicate<KryptonLivingEntity>?): Builder = apply { selector = value }

        fun build(): TargetingConditions = TargetingConditions(combat, range, lineOfSight, invisibles, selector)
    }

    companion object {

        @JvmStatic
        fun combat(): Builder = Builder(true)

        @JvmStatic
        fun nonCombat(): Builder = Builder(false)
    }
}
