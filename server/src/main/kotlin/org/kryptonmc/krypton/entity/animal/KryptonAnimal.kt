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
package org.kryptonmc.krypton.entity.animal

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.animal.Animal
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.KryptonAgeable
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID

abstract class KryptonAnimal(world: KryptonWorld, type: EntityType<out Animal>) : KryptonAgeable(world, type), Animal {

    final override var loveCause: UUID? = null
    final override var inLoveTime: Int = 0
    final override val isInLove: Boolean
        get() = inLoveTime > 0
    override val canFallInLove: Boolean
        get() = inLoveTime <= 0
    var loveCausePlayer: KryptonPlayer?
        get() {
            val cause = loveCause ?: return null
            return world.entityManager.get(cause) as? KryptonPlayer
        }
        set(value) {
            inLoveTime = DEFAULT_IN_LOVE_TIME
            loveCause = value?.uuid
        }

    override fun canMate(target: Animal): Boolean {
        if (target === this) return false
        if (target.javaClass != javaClass) return false
        return isInLove && target.isInLove
    }

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.WHEAT

    companion object {

        private const val DEFAULT_IN_LOVE_TIME = 30 * 20 // 30 seconds in ticks
    }
}
