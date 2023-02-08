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
package org.kryptonmc.krypton.world.block.handler

import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.shapes.collision.CollisionContext
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter

interface BlockShapesProvider {

    fun isApplicableBlockType(name: String): Boolean

    fun getShape(state: KryptonBlockState, world: BlockGetter, pos: Vec3i, context: CollisionContext): VoxelShape {
        return Shapes.block()
    }

    fun getOcclusionShape(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): VoxelShape {
        return state.getShape(world, pos)
    }

    fun getCollisionShape(state: KryptonBlockState, world: BlockGetter, pos: Vec3i, context: CollisionContext): VoxelShape {
        if (state.block.properties.hasCollision) return state.getShape(world, pos)
        return Shapes.empty()
    }

    fun getVisualShape(state: KryptonBlockState, world: BlockGetter, pos: Vec3i, context: CollisionContext): VoxelShape {
        return getCollisionShape(state, world, pos, context)
    }

    fun getInteractionShape(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): VoxelShape {
        return Shapes.empty()
    }

    fun getBlockSupportShape(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): VoxelShape {
        return getCollisionShape(state, world, pos, CollisionContext.empty())
    }

    fun isCollisionShapeFullBlock(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): Boolean {
        return KryptonBlock.isShapeFullBlock(state.getCollisionShape(world, pos))
    }

    fun isOcclusionShapeFullBlock(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): Boolean {
        return KryptonBlock.isShapeFullBlock(state.getOcclusionShape(world, pos))
    }
}
