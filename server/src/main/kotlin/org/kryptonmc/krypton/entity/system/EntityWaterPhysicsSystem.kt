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
package org.kryptonmc.krypton.entity.system

import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.tags.FluidTags
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.vehicle.KryptonBoat
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityVelocity
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import kotlin.math.abs
import kotlin.math.max

class EntityWaterPhysicsSystem(private val entity: KryptonEntity) {

    private val fluidHeights = Object2DoubleArrayMap<TagKey<Fluid>>(2)
    private val fluidOnEyes = HashSet<TagKey<Fluid>>()
    private var inWater = false
    private var inLava = false
    private var underwater = false

    fun isInWater(): Boolean = inWater

    fun isInLava(): Boolean = inLava

    fun isUnderwater(): Boolean = underwater

    fun isInBubbleColumn(): Boolean = entity.world.getBlock(entity.position.asVec3i()).eq(KryptonBlocks.BUBBLE_COLUMN)

    fun isInWaterOrBubbleColumn(): Boolean = inWater || isInBubbleColumn()

    fun isUnderFluid(fluid: TagKey<Fluid>): Boolean = fluidOnEyes.contains(fluid)

    fun tick() {
        updateInWaterState()
        updateUnderFluid()
        updateSwimming()
    }

    private fun updateInWaterState(): Boolean {
        fluidHeights.clear()
        updateWaterCurrent()
        val lavaScale = if (entity.world.dimensionType.isUltrawarm) FAST_LAVA_FLOW_SCALE else SLOW_LAVA_FLOW_SCALE
        val pushedFromLava = updateFluidHeightAndFlow(FluidTags.LAVA, lavaScale)
        return entity.isInWater() || pushedFromLava
    }

    private fun updateWaterCurrent() {
        if (entity.vehicleSystem.vehicle() is KryptonBoat || !updateFluidHeightAndFlow(FluidTags.WATER, WATER_FLOW_SCALE)) {
            underwater = false
            return
        }
        entity.fallDistance = 0F
        underwater = true
        entity.remainingFireTicks = 0
    }

    private fun updateFluidHeightAndFlow(tag: TagKey<Fluid>, flowScale: Double): Boolean {
        val minX = Maths.floor(entity.boundingBox.minX + BOUNDING_BOX_EPSILON)
        val minY = Maths.floor(entity.boundingBox.minY + BOUNDING_BOX_EPSILON)
        val minZ = Maths.floor(entity.boundingBox.minZ + BOUNDING_BOX_EPSILON)
        val maxX = Maths.ceil(entity.boundingBox.maxX - BOUNDING_BOX_EPSILON)
        val maxY = Maths.ceil(entity.boundingBox.maxY - BOUNDING_BOX_EPSILON)
        val maxZ = Maths.ceil(entity.boundingBox.maxZ - BOUNDING_BOX_EPSILON)

        var amount = 0.0
        val pushed = entity.isPushedByFluid()
        var shouldPush = false
        var offset = Vec3d.ZERO
        var pushes = 0

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val pos = Vec3i(x, y, z)
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
            entity.sendPacketToViewers(PacketOutSetEntityVelocity.fromEntity(entity))
        }

        fluidHeights.put(tag, amount)
        return shouldPush
    }

    private fun updateUnderFluid() {
        underwater = isUnderFluid(FluidTags.WATER)
        fluidOnEyes.clear()

        val y = entity.position.y + entity.eyeHeight - KryptonEntity.BREATHING_DISTANCE_BELOW_EYES
        val vehicle = entity.vehicleSystem.vehicle()
        if (vehicle is KryptonBoat && !vehicle.isUnderwater() && vehicle.boundingBox.maxY >= y && vehicle.boundingBox.minY <= y) return

        val pos = entity.position.withY(y).asVec3i()
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
            entity.isSwimming = entity.isSprinting && inWater && !entity.vehicleSystem.isPassenger()
        } else {
            entity.isSwimming = entity.isSprinting && underwater && !entity.vehicleSystem.isPassenger() && fluidAtFeet().eq(FluidTags.WATER)
        }
    }

    private fun fluidAtFeet(): KryptonFluidState = entity.world.getFluid(entity.position.asVec3i())

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
