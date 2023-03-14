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
package org.kryptonmc.krypton.shapes.collision

import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.util.math.Maths
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
