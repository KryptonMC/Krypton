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

import org.kryptonmc.api.state.Properties
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.state.property.downcast
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.world.BlockAccessor
import org.kryptonmc.krypton.world.material.Materials
import org.spongepowered.math.vector.Vector3d
import kotlin.math.min

abstract class FlowingFluid : KryptonFluid() {

    private val shapes = HashMap<KryptonFluidState, VoxelShape>()

    abstract val flowing: KryptonFluid
    abstract val source: KryptonFluid

    fun flowing(level: Int, falling: Boolean): KryptonFluidState = flowing.defaultState.set(LEVEL, level).set(FALLING, falling)

    fun source(falling: Boolean): KryptonFluidState = source.defaultState.set(FALLING, falling)

    override fun createStateDefinition(builder: StateDefinition.Builder<KryptonFluid, KryptonFluidState>) {
        builder.add(Properties.FALLING.downcast())
    }

    override fun getFlow(world: BlockAccessor, x: Int, y: Int, z: Int, state: KryptonFluidState): Vector3d {
        var flowX = 0.0
        var flowZ = 0.0
        var tempX = 0
        var tempY = 0
        var tempZ = 0
        Directions.Plane.HORIZONTAL.forEach {
            tempX = x + it.normalX
            tempY = y + it.normalY
            tempZ = z + it.normalZ
            val fluid = world.getFluid(tempX, tempY, tempZ)
            if (!affectsFlow(fluid)) return@forEach
            var ownHeight = fluid.ownHeight
            var height = 0F
            if (ownHeight == 0F) {
                if (!world.getBlock(tempX, tempY, tempZ).material.blocksMotion) {
                    val below = world.getFluid(tempX, tempY - 1, tempZ)
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
        var flow = Vector3d(flowX, 0.0, flowZ)
        if (state.require(FALLING)) {
            for (direction in Directions.Plane.HORIZONTAL.iterator()) {
                tempX = x + direction.normalX
                tempY = y + direction.normalY
                tempZ = z + direction.normalZ
                if (isSolidFace(world, tempX, tempY, tempZ, direction) || isSolidFace(world, tempX, tempY + 1, tempZ, direction)) {
                    flow = flow.normalize().add(0.0, -6.0, 0.0)
                    break
                }
            }
        }
        return flow.normalize()
    }

    override fun getHeight(state: KryptonFluidState, world: BlockAccessor, x: Int, y: Int, z: Int): Float =
        if (hasSameAbove(state, world, x, y, z)) 1F else state.ownHeight

    override fun getOwnHeight(state: KryptonFluidState): Float = state.level / 9F

    override fun getShape(state: KryptonFluidState, world: BlockAccessor, x: Int, y: Int, z: Int): VoxelShape {
        if (state.level == 9 && hasSameAbove(state, world, x, y, z)) return Shapes.block()
        return shapes.computeIfAbsent(state) { Shapes.box(0.0, 0.0, 0.0, 1.0, it.getHeight(world, x, y, z).toDouble(), 1.0) }
    }

    private fun affectsFlow(state: KryptonFluidState): Boolean = state.isEmpty || state.fluid.isSame(this)

    protected fun isSolidFace(world: BlockAccessor, x: Int, y: Int, z: Int, side: Direction): Boolean {
        val block = world.getBlock(x, y, z)
        val fluid = world.getFluid(x, y, z)
        if (fluid.fluid.isSame(this)) return false
        if (side == Direction.UP) return true
        if (block.material == Materials.ICE) return false
        return block.isFaceSturdy(world, x, y, z, side)
    }

    companion object {

        private const val OWN_HEIGHT_OFFSET = 1F - KryptonEntity.BREATHING_DISTANCE_BELOW_EYES
        const val MAX_LEVEL: Int = 8
        @JvmField
        val LEVEL: KryptonProperty<Int> = Properties.LIQUID_LEVEL.downcast()
        @JvmField
        val FALLING: KryptonProperty<Boolean> = Properties.FALLING.downcast()

        @JvmStatic
        protected fun calculateBlockLevel(state: KryptonFluidState): Int {
            if (state.isSource) return 0
            val fallingBonus = if (state.require(Properties.FALLING.downcast())) MAX_LEVEL else 0
            return MAX_LEVEL - min(state.level, MAX_LEVEL) + fallingBonus
        }

        @JvmStatic
        private fun hasSameAbove(state: KryptonFluidState, world: BlockAccessor, x: Int, y: Int, z: Int): Boolean =
            state.fluid.isSame(world.getFluid(x, y + 1, z).fluid)
    }
}
