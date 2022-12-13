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
package org.kryptonmc.krypton.shapes

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.item.KryptonItemStack
import java.util.function.Predicate

interface CollisionContext {

    val isDescending: Boolean

    fun isAbove(shape: VoxelShape, x: Int, y: Int, z: Int, canAscend: Boolean): Boolean

    fun isHoldingItem(item: ItemType): Boolean

    fun canStandOnFluid(fluid1: Fluid, fluid2: Fluid): Boolean

    companion object {

        @JvmStatic
        fun empty(): CollisionContext = EntityCollisionContext.EMPTY

        @JvmStatic
        fun of(entity: KryptonEntity): CollisionContext {
            val heldItem = if (entity is KryptonLivingEntity) entity.getHeldItem(Hand.MAIN) else KryptonItemStack.EMPTY
            val canStandOnFluid: Predicate<Fluid> = if (entity is KryptonLivingEntity) {
                Predicate { entity.canStandOnFluid(it) }
            } else {
                Predicate { false }
            }
            return EntityCollisionContext(entity.isSneaking, entity.position.y, heldItem, canStandOnFluid, entity)
        }
    }
}
