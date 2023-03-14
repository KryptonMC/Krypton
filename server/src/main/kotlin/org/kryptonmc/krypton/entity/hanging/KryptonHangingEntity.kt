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
package org.kryptonmc.krypton.entity.hanging

import org.kryptonmc.api.entity.hanging.HangingEntity
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.hanging.HangingEntitySerializer
import org.kryptonmc.krypton.util.enumhelper.Directions
import org.kryptonmc.krypton.world.KryptonWorld

abstract class KryptonHangingEntity(world: KryptonWorld) : KryptonEntity(world), HangingEntity {

    override val serializer: EntitySerializer<out KryptonHangingEntity>
        get() = HangingEntitySerializer

    internal var centerPosition: Vec3i? = null
    final override var direction: Direction = Direction.SOUTH
        set(value) {
            require(value.axis.isHorizontal())
            field = value
            // Each horizontal direction is a half turn (90 degrees) away from each other.
            // The 2D data value is 0 = south, 1 = west, 2 = north, 3 = east.
            teleport(position.withPitch(Directions.data2D(value) * HALF_TURN_DEGREES))
            recalculateBoundingBox()
        }

    abstract fun width(): Int

    abstract fun height(): Int

    private fun recalculateBoundingBox() {
        var x = centerPosition!!.x + BLOCK_CENTER_OFFSET
        var y = centerPosition!!.y + BLOCK_CENTER_OFFSET
        var z = centerPosition!!.z + BLOCK_CENTER_OFFSET

        // for EAST, the x becomes centerX + 1/32, and for WEST, the x becomes centerX + 31/32
        x -= direction.normalX * ONE_BLOCK_PIXELS
        // for SOUTH, the z becomes centerZ + 1/32, and for NORTH, the z becomes centerZ + 31/32
        z -= direction.normalZ * ONE_BLOCK_PIXELS
        if (height() % TWO_BLOCKS_PICTURE_PIXELS == 0) y += BLOCK_CENTER_OFFSET

        val antiClockwise = Directions.antiClockwise(direction)
        if (width() % TWO_BLOCKS_PICTURE_PIXELS == 0) {
            // for direction NORTH (anti clockwise WEST), the x becomes centerX
            // for direction SOUTH (anti clockwise EAST), the x becomes centerX + 1
            x += BLOCK_CENTER_OFFSET * antiClockwise.normalX
            // for direction EAST (anti clockwise NORTH), the z becomes centerZ
            // for direction WEST (anti clockwise SOUTH), the z becomes centerZ + 1
            z += BLOCK_CENTER_OFFSET * antiClockwise.normalZ
        }
        teleport(position.withCoordinates(x, y, z))

        var widthX = width().toDouble()
        var height = height().toDouble()
        var widthZ = width().toDouble()
        if (direction.axis == Direction.Axis.Z) widthZ = 1.0 else widthX = 1.0
        widthX /= TWO_BLOCKS_PICTURE_PIXELS
        height /= TWO_BLOCKS_PICTURE_PIXELS
        widthZ /= TWO_BLOCKS_PICTURE_PIXELS
        boundingBox = BoundingBox(x - widthX, y - height, z - widthZ, x + widthX, y + height, z + widthZ)
    }

    companion object {

        // The offset to add to a block coordinate to make it in the centre of the block.
        private const val BLOCK_CENTER_OFFSET = 0.5
        private const val TWO_BLOCKS_PICTURE_PIXELS = 16 * 2 // 16 pixels on a picture = 1 block.
        private const val ONE_BLOCK_PIXELS = 15.0 / 32.0
        private const val HALF_TURN_DEGREES = 90F
    }
}
