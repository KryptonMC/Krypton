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
package org.kryptonmc.krypton.world.fluid

import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.state.property.KryptonProperties
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.util.Vec3dImpl
import org.kryptonmc.krypton.world.BlockAccessor
import org.kryptonmc.krypton.world.material.Materials
import kotlin.math.min

abstract class FlowingFluid : KryptonFluid() {

    private val shapes = HashMap<KryptonFluidState, VoxelShape>()

    abstract val flowing: KryptonFluid
    abstract val source: KryptonFluid

    fun flowing(level: Int, falling: Boolean): KryptonFluidState = flowing.defaultState.set(LEVEL, level).set(FALLING, falling)

    fun source(falling: Boolean): KryptonFluidState = source.defaultState.set(FALLING, falling)

    override fun createStateDefinition(builder: StateDefinition.Builder<KryptonFluid, KryptonFluidState>) {
        builder.add(FALLING)
    }

    override fun getFlow(world: BlockAccessor, pos: BlockPos, state: KryptonFluidState): Vec3d {
        var flowX = 0.0
        var flowZ = 0.0
        val offsetPos = BlockPos.Mutable()

        Directions.Plane.HORIZONTAL.forEach {
            offsetPos.setWithOffset(pos, it)
            val fluid = world.getFluid(offsetPos)
            if (!affectsFlow(fluid)) return@forEach

            var ownHeight = fluid.ownHeight
            var height = 0F
            if (ownHeight == 0F) {
                if (!world.getBlock(offsetPos).material.blocksMotion) {
                    val below = world.getFluid(offsetPos.below())
                    if (affectsFlow(below)) {
                        ownHeight = below.ownHeight
                        if (ownHeight > 0F) height = state.ownHeight - (ownHeight - OWN_HEIGHT_OFFSET)
                    }
                }
            } else if (ownHeight > 0F) {
                height = state.ownHeight - ownHeight
            }
            if (height != 0F) {
                flowX += (it.normalX * height).toDouble()
                flowZ += (it.normalZ * height).toDouble()
            }
        }

        var flow: Vec3d = Vec3dImpl(flowX, 0.0, flowZ)
        if (state.require(FALLING)) {
            for (direction in Directions.Plane.HORIZONTAL.iterator()) {
                offsetPos.setWithOffset(pos, direction)
                if (isSolidFace(world, offsetPos, direction) || isSolidFace(world, offsetPos.above(), direction)) {
                    flow = flow.normalize().add(0.0, -6.0, 0.0)
                    break
                }
            }
        }
        return flow.normalize()
    }

    override fun getHeight(state: KryptonFluidState, world: BlockAccessor, pos: BlockPos): Float =
        if (hasSameAbove(state, world, pos)) 1F else state.ownHeight

    override fun getOwnHeight(state: KryptonFluidState): Float = state.level / 9F

    override fun getShape(state: KryptonFluidState, world: BlockAccessor, pos: BlockPos): VoxelShape {
        if (state.level == 9 && hasSameAbove(state, world, pos)) return Shapes.block()
        return shapes.computeIfAbsent(state) { Shapes.box(0.0, 0.0, 0.0, 1.0, it.getHeight(world, pos).toDouble(), 1.0) }
    }

    private fun affectsFlow(state: KryptonFluidState): Boolean = state.isEmpty || state.fluid.isSame(this)

    protected fun isSolidFace(world: BlockAccessor, pos: BlockPos, side: Direction): Boolean {
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
            if (state.isSource) return 0
            val fallingBonus = if (state.require(FALLING)) MAX_LEVEL else 0
            return MAX_LEVEL - min(state.level, MAX_LEVEL) + fallingBonus
        }

        @JvmStatic
        private fun hasSameAbove(state: KryptonFluidState, world: BlockAccessor, pos: BlockPos): Boolean =
            state.fluid.isSame(world.getFluid(pos.above()).fluid)
    }
}
