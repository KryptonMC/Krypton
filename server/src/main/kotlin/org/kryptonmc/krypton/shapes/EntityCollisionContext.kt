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

import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.Maths
import java.util.function.Predicate

open class EntityCollisionContext(
    override val isDescending: Boolean,
    private val entityBottom: Double,
    private val heldItem: KryptonItemStack,
    private val canStandOnFluid: Predicate<Fluid>,
    val entity: KryptonEntity?
) : CollisionContext {

    override fun isAbove(shape: VoxelShape, x: Int, y: Int, z: Int, canAscend: Boolean): Boolean =
        entityBottom > y.toDouble() + shape.max(Direction.Axis.Y) - Maths.EPSILON

    override fun isHoldingItem(item: ItemType): Boolean = item === heldItem.type

    override fun canStandOnFluid(fluid1: Fluid, fluid2: Fluid): Boolean = canStandOnFluid.test(fluid1) && fluid1 !== fluid2

    companion object {

        @JvmField
        val EMPTY: EntityCollisionContext = object : EntityCollisionContext(false, -Double.MAX_VALUE, KryptonItemStack.EMPTY, { false }, null) {

            override fun isAbove(shape: VoxelShape, x: Int, y: Int, z: Int, canAscend: Boolean): Boolean = canAscend
        }
    }
}
