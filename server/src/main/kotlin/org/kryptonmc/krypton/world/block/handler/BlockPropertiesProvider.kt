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

import org.kryptonmc.api.block.PushReaction
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.context.BlockPlaceContext
import org.kryptonmc.krypton.world.block.data.BlockOffsetType
import org.kryptonmc.krypton.world.block.data.RenderShape
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter
import org.kryptonmc.krypton.world.fluid.KryptonFluid
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.fluid.KryptonFluids
import org.kryptonmc.krypton.world.material.MaterialColor
import org.kryptonmc.krypton.world.material.MaterialColors

interface BlockPropertiesProvider {

    fun isApplicableBlockType(name: String): Boolean

    fun getRenderShape(state: KryptonBlockState): RenderShape {
        return RenderShape.MODEL
    }

    fun getPushReaction(state: KryptonBlockState): PushReaction {
        return state.material.pushReaction
    }

    fun getFluidState(state: KryptonBlockState): KryptonFluidState = KryptonFluids.EMPTY.defaultState

    fun getItemStack(world: BlockGetter, pos: Vec3i, state: KryptonBlockState): KryptonItemStack {
        return KryptonItemStack.EMPTY
    }

    fun useShapeForLightOcclusion(state: KryptonBlockState): Boolean {
        return false
    }

    fun getLightBlock(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): Int {
        if (state.isSolidRender(world, pos)) return world.maximumLightLevel()
        return if (state.propagatesSkylightDown(world, pos)) 0 else 1
    }

    fun canBeReplaced(state: KryptonBlockState, context: BlockPlaceContext): Boolean {
        return state.material.replaceable && (context.item.isEmpty() || !context.item.eq(state.block.asItem()))
    }

    fun canBeReplaced(state: KryptonBlockState, fluid: KryptonFluid): Boolean {
        return state.material.replaceable || !state.material.solid
    }

    fun canSurvive(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): Boolean {
        return true
    }

    fun shouldSkipRendering(state: KryptonBlockState, adjacent: KryptonBlockState, face: Direction): Boolean {
        return false
    }

    fun getMaterialColor(state: KryptonBlockState): MaterialColor {
        return MaterialColors.NONE
    }

    fun getLightEmission(state: KryptonBlockState): Int {
        return 0
    }

    fun getOffsetType(state: KryptonBlockState): BlockOffsetType {
        return BlockOffsetType.NONE
    }

    fun isValidSpawn(state: KryptonBlockState, world: BlockGetter, pos: Vec3i, type: KryptonEntityType<*>): Boolean {
        return state.isFaceSturdy(world, pos, Direction.UP) && getLightEmission(state) < 14
    }

    fun isRedstoneConductor(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): Boolean {
        return state.material.solidBlocking && state.isCollisionShapeFullBlock(world, pos)
    }

    fun hasPostProcess(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): Boolean {
        return false
    }

    fun isSuffocating(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): Boolean {
        return state.material.blocksMotion && state.isCollisionShapeFullBlock(world, pos)
    }

    fun hasGravity(): Boolean {
        return false
    }

    fun hasBlockEntity(): Boolean {
        return false
    }

    fun maximumHorizontalOffset(): Float {
        return 0.25F
    }

    fun maximumVerticalOffset(): Float {
        return 0.25F
    }

    fun randomlyTicks(state: KryptonBlockState): Boolean {
        return false
    }

    fun propagatesSkylightDown(state: KryptonBlockState, world: BlockGetter, pos: Vec3i): Boolean {
        return false
    }

    fun getPlacementState(context: BlockPlaceContext): KryptonBlockState? {
        return null
    }
}
