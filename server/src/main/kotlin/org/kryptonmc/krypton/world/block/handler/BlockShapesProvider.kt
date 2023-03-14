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
