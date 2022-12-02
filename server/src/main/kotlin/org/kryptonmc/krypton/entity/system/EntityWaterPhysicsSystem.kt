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
package org.kryptonmc.krypton.entity.system

import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.tags.FluidTags
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.vehicle.KryptonBoat
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityVelocity
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.Vec3dImpl
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import kotlin.math.abs
import kotlin.math.max

class EntityWaterPhysicsSystem(private val entity: KryptonEntity) {

    private val fluidHeights = Object2DoubleArrayMap<TagKey<Fluid>>(2)
    private val fluidOnEyes = HashSet<TagKey<Fluid>>()
    var isInWater: Boolean = false
    var isInLava: Boolean = false
    var isUnderwater: Boolean = false

    fun isInBubbleColumn(): Boolean =
        entity.world.getBlock(entity.position.floorX(), entity.position.floorY(), entity.position.floorZ()).eq(Blocks.BUBBLE_COLUMN)

    fun isInWaterOrBubbleColumn(): Boolean = isInWater || isInBubbleColumn()

    fun isUnderFluid(fluid: TagKey<Fluid>): Boolean = fluidOnEyes.contains(fluid)

    fun tick() {
        updateInWaterState()
        updateUnderFluid()
        updateSwimming()
    }

    fun updateInWaterState(): Boolean {
        fluidHeights.clear()
        updateWaterCurrent()
        val lavaScale = if (entity.world.dimensionType.isUltrawarm) FAST_LAVA_FLOW_SCALE else SLOW_LAVA_FLOW_SCALE
        val pushedFromLava = updateFluidHeightAndFlow(FluidTags.LAVA, lavaScale)
        return entity.isInWater || pushedFromLava
    }

    fun updateWaterCurrent() {
        if (entity.vehicleSystem.vehicle is KryptonBoat || !updateFluidHeightAndFlow(FluidTags.WATER, WATER_FLOW_SCALE)) {
            isUnderwater = false
            return
        }
        entity.fallDistance = 0F
        isUnderwater = true
        entity.remainingFireTicks = 0
    }

    private fun updateFluidHeightAndFlow(tag: TagKey<Fluid>, flowScale: Double): Boolean {
        val minX = Maths.floor(entity.boundingBox.minimumX + BOUNDING_BOX_EPSILON)
        val minY = Maths.floor(entity.boundingBox.minimumY + BOUNDING_BOX_EPSILON)
        val minZ = Maths.floor(entity.boundingBox.minimumZ + BOUNDING_BOX_EPSILON)
        val maxX = Maths.ceil(entity.boundingBox.maximumX - BOUNDING_BOX_EPSILON)
        val maxY = Maths.ceil(entity.boundingBox.maximumY - BOUNDING_BOX_EPSILON)
        val maxZ = Maths.ceil(entity.boundingBox.maximumZ - BOUNDING_BOX_EPSILON)

        var amount = 0.0
        val pushed = entity.isPushedByFluid
        var shouldPush = false
        var offset = Vec3dImpl.ZERO
        var pushes = 0
        val pos = BlockPos.Mutable()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    pos.set(x, y, z)
                    val fluid = entity.world.getFluid(pos)
                    if (!fluid.eq(tag)) continue
                    val height = (y.toFloat() + fluid.getHeight(entity.world, pos)).toDouble()
                    if (height < minY) continue

                    shouldPush = true
                    amount = max(height - minY, amount)
                    if (!pushed) continue
                    var flow = fluid.getFlow(entity.world, pos)
                    if (amount < MAX_FLOW_MULTIPLIER) flow = flow.multiply(amount)
                    offset = offset.add(flow)
                    ++pushes
                }
            }
        }

        if (offset.length() > 0.0) {
            if (pushes > 0) offset = offset.multiply(1.0 / pushes)
            if (entity !is KryptonPlayer) offset = offset.normalize()

            val velocity = entity.velocity
            offset = offset.multiply(flowScale)
            if (abs(velocity.x) < FLUID_VECTOR_EPSILON && abs(velocity.z) < FLUID_VECTOR_EPSILON && offset.length() < FLUID_VECTOR_MAGIC) {
                offset = offset.normalize().multiply(FLUID_VECTOR_MAGIC)
            }
            entity.velocity = entity.velocity.add(offset)
            entity.viewingSystem.sendToViewers(PacketOutSetEntityVelocity(entity))
        }

        fluidHeights.put(tag, amount)
        return shouldPush
    }

    private fun updateUnderFluid() {
        isUnderwater = isUnderFluid(FluidTags.WATER)
        fluidOnEyes.clear()

        val y = entity.position.y + entity.eyeHeight - KryptonEntity.BREATHING_DISTANCE_BELOW_EYES
        val vehicle = entity.vehicleSystem.vehicle
        if (vehicle is KryptonBoat && !vehicle.isUnderwater && vehicle.boundingBox.maximumY >= y && vehicle.boundingBox.minimumY <= y) return

        val pos = BlockPos(entity.position.x, y, entity.position.z)
        val fluid = entity.world.getFluid(pos)
        val height = (pos.y.toFloat() + fluid.getHeight(entity.world, pos)).toDouble()
        if (height > y) {
            @Suppress("UNCHECKED_CAST")
            fluid.tags().forEach { fluidOnEyes.add(it as TagKey<Fluid>) }
        }
    }

    private fun updateSwimming() {
        if (entity is KryptonPlayer) {
            if (entity.abilities.flying) {
                entity.isSwimming = false
            } else {
                defaultUpdateSwimming()
            }
            return
        }
        defaultUpdateSwimming()
    }

    private fun defaultUpdateSwimming() {
        if (entity.isSwimming) {
            entity.isSwimming = entity.isSprinting && isInWater && !entity.vehicleSystem.isPassenger()
        } else {
            entity.isSwimming = entity.isSprinting && isUnderwater && !entity.vehicleSystem.isPassenger() && fluidAtFeet().eq(FluidTags.WATER)
        }
    }

    private fun fluidAtFeet(): KryptonFluidState =
        entity.world.getFluid(entity.position.floorX(), entity.position.floorY(), entity.position.floorZ())

    companion object {

        private const val FLUID_VECTOR_EPSILON = 0.003
        private const val FLUID_VECTOR_MAGIC = 0.0045000000000000005
        private const val WATER_FLOW_SCALE = 0.014
        private const val FAST_LAVA_FLOW_SCALE = 0.007
        private const val SLOW_LAVA_FLOW_SCALE = 0.0023333333333333335
        private const val MAX_FLOW_MULTIPLIER = 0.4
        private const val BOUNDING_BOX_EPSILON = 0.001
    }
}
