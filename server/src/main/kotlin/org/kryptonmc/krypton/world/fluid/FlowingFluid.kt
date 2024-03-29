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
package org.kryptonmc.krypton.world.fluid

import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.state.property.KryptonProperties
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.util.enumhelper.Directions
import org.kryptonmc.krypton.world.components.BlockGetter
import org.kryptonmc.krypton.world.material.Materials
import kotlin.math.min

abstract class FlowingFluid : KryptonFluid() {

    private val shapes = HashMap<KryptonFluidState, VoxelShape>()

    abstract fun flowing(): KryptonFluid

    abstract fun source(): KryptonFluid

    fun getFlowing(level: Int, falling: Boolean): KryptonFluidState = flowing().defaultState.setProperty(LEVEL, level).setProperty(FALLING, falling)

    fun getSource(falling: Boolean): KryptonFluidState = source().defaultState.setProperty(FALLING, falling)

    override fun createStateDefinition(builder: StateDefinition.Builder<KryptonFluid, KryptonFluidState>) {
        builder.add(FALLING)
    }

    override fun getFlow(world: BlockGetter, pos: Vec3i, state: KryptonFluidState): Vec3d {
        var flowX = 0.0
        var flowZ = 0.0

        Directions.Plane.HORIZONTAL.forEach {
            val offsetPos = Vec3i(pos.x + it.normalX, pos.y + it.normalY, pos.z + it.normalZ)
            val fluid = world.getFluid(offsetPos)
            if (!affectsFlow(fluid)) return@forEach

            var ownHeight = fluid.ownHeight()
            var height = 0F
            if (ownHeight == 0F) {
                if (!world.getBlock(offsetPos).material.blocksMotion) {
                    val below = world.getFluid(offsetPos.relative(Direction.DOWN))
                    if (affectsFlow(below)) {
                        ownHeight = below.ownHeight()
                        if (ownHeight > 0F) height = state.ownHeight() - (ownHeight - OWN_HEIGHT_OFFSET)
                    }
                }
            } else if (ownHeight > 0F) {
                height = state.ownHeight() - ownHeight
            }
            if (height != 0F) {
                flowX += (it.normalX * height).toDouble()
                flowZ += (it.normalZ * height).toDouble()
            }
        }

        var flow = Vec3d(flowX, 0.0, flowZ)
        if (state.requireProperty(FALLING)) {
            for (direction in Directions.Plane.HORIZONTAL.iterator()) {
                val offsetPos = Vec3i(pos.x + direction.normalX, pos.y + direction.normalY, pos.z + direction.normalZ)
                if (isSolidFace(world, offsetPos, direction) || isSolidFace(world, offsetPos.relative(Direction.UP), direction)) {
                    flow = flow.normalize().add(0.0, -6.0, 0.0)
                    break
                }
            }
        }
        return flow.normalize()
    }

    override fun getHeight(state: KryptonFluidState, world: BlockGetter, pos: Vec3i): Float =
        if (hasSameAbove(state, world, pos)) 1F else state.ownHeight()

    override fun getOwnHeight(state: KryptonFluidState): Float = state.level / 9F

    override fun getShape(state: KryptonFluidState, world: BlockGetter, pos: Vec3i): VoxelShape {
        if (state.level == 9 && hasSameAbove(state, world, pos)) return Shapes.block()
        return shapes.computeIfAbsent(state) { Shapes.box(0.0, 0.0, 0.0, 1.0, it.getHeight(world, pos).toDouble(), 1.0) }
    }

    private fun affectsFlow(state: KryptonFluidState): Boolean = state.isEmpty() || state.fluid.isSame(this)

    protected fun isSolidFace(world: BlockGetter, pos: Vec3i, side: Direction): Boolean {
        val block = world.getBlock(pos)
        val fluid = world.getFluid(pos)
        if (fluid.fluid.isSame(this)) return false
        if (side == Direction.UP) return true
        if (block.material == Materials.ICE) return false
        return block.isFaceSturdy(world, pos, side)
    }

    companion object {

        private const val OWN_HEIGHT_OFFSET = 1F - KryptonEntity.BREATHING_DISTANCE_BELOW_EYES
        const val MAX_LEVEL: Int = 8
        @JvmField
        val LEVEL: KryptonProperty<Int> = KryptonProperties.LIQUID_LEVEL
        @JvmField
        val FALLING: KryptonProperty<Boolean> = KryptonProperties.FALLING

        @JvmStatic
        protected fun calculateBlockLevel(state: KryptonFluidState): Int {
            if (state.isSource()) return 0
            val fallingBonus = if (state.requireProperty(FALLING)) MAX_LEVEL else 0
            return MAX_LEVEL - min(state.level, MAX_LEVEL) + fallingBonus
        }

        @JvmStatic
        private fun hasSameAbove(state: KryptonFluidState, world: BlockGetter, pos: Vec3i): Boolean =
            state.fluid.isSame(world.getFluid(pos.relative(Direction.UP)).fluid)
    }
}
