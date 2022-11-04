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
import org.kryptonmc.api.tags.Tag
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.vehicle.KryptonBoat
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityVelocity
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.tags.KryptonTagTypes
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.spongepowered.math.vector.Vector3d
import kotlin.math.abs
import kotlin.math.max

class EntityWaterPhysicsSystem(private val entity: KryptonEntity) {

    private val fluidHeights = Object2DoubleArrayMap<Tag<Fluid>>(2)
    private val fluidOnEyes = HashSet<Tag<Fluid>>()
    var isInWater: Boolean = false
    var isInLava: Boolean = false
    var isUnderwater: Boolean = false

    fun isInBubbleColumn(): Boolean =
        entity.world.getBlock(entity.location.floorX(), entity.location.floorY(), entity.location.floorZ()).eq(Blocks.BUBBLE_COLUMN)

    fun isInWaterOrBubbleColumn(): Boolean = isInWater || isInBubbleColumn()

    fun isUnderFluid(fluid: Tag<Fluid>): Boolean = fluidOnEyes.contains(fluid)

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
        entity.fireTicks = 0
    }

    private fun updateFluidHeightAndFlow(tag: Tag<Fluid>, flowScale: Double): Boolean {
        val minX = Maths.floor(entity.boundingBox.minimumX + 0.001)
        val minY = Maths.floor(entity.boundingBox.minimumY + 0.001)
        val minZ = Maths.floor(entity.boundingBox.minimumZ + 0.001)
        val maxX = Maths.ceil(entity.boundingBox.maximumX - 0.001)
        val maxY = Maths.ceil(entity.boundingBox.maximumY - 0.001)
        val maxZ = Maths.ceil(entity.boundingBox.maximumZ - 0.001)
        var amount = 0.0
        val pushed = entity.isPushedByFluid
        var shouldPush = false
        var offset = Vector3d.ZERO
        var pushes = 0

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val fluid = entity.world.getFluid(x, y, z)
                    if (!fluid.eq(tag)) continue
                    val height = (y.toFloat() + fluid.getHeight(entity.world, x, y, z)).toDouble()
                    if (height < minY) continue
                    shouldPush = true
                    amount = max(height - minY, amount)
                    if (!pushed) continue
                    var flow = fluid.getFlow(entity.world, x, y, z)
                    if (amount < 0.4) flow = flow.mul(amount)
                    offset = offset.add(flow)
                    ++pushes
                }
            }
        }

        if (offset.length() > 0.0) {
            if (pushes > 0) offset = offset.mul(1.0 / pushes)
            if (entity !is KryptonPlayer) offset = offset.normalize()

            val velocity = entity.velocity
            offset = offset.mul(flowScale * 1.0)
            if (abs(velocity.x()) < FLUID_VECTOR_EPSILON && abs(velocity.z()) < FLUID_VECTOR_EPSILON && offset.length() < FLUID_VECTOR_MAGIC) {
                offset = offset.normalize().mul(FLUID_VECTOR_MAGIC)
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

        val y = (entity.location.y() + entity.eyeHeight) - KryptonEntity.BREATHING_DISTANCE_BELOW_EYES
        val vehicle = entity.vehicleSystem.vehicle
        if (vehicle is KryptonBoat && !vehicle.isUnderwater && vehicle.boundingBox.maximumY >= y && vehicle.boundingBox.minimumY <= y) return

        val x = entity.location.floorX()
        val blockY = Maths.floor(y)
        val z = entity.location.floorZ()
        val fluid = entity.world.getFluid(x, blockY, z)

        val height = (blockY.toFloat() + fluid.getHeight(entity.world, x, blockY, z))
        if (height <= y) return
        KryptonTagManager.get(KryptonTagTypes.FLUIDS).forEach(fluidOnEyes::add)
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
        entity.world.getFluid(entity.location.floorX(), entity.location.floorY(), entity.location.floorZ())

    companion object {

        private const val FLUID_VECTOR_EPSILON = 0.003
        private const val FLUID_VECTOR_MAGIC = 0.0045000000000000005
        private const val WATER_FLOW_SCALE = 0.014
        private const val FAST_LAVA_FLOW_SCALE = 0.007
        private const val SLOW_LAVA_FLOW_SCALE = 0.0023333333333333335
    }
}
