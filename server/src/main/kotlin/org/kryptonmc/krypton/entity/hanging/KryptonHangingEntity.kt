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
package org.kryptonmc.krypton.entity.hanging

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.hanging.HangingEntity
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.util.KryptonBoundingBox
import org.kryptonmc.krypton.util.antiClockwise
import org.kryptonmc.krypton.util.data2D
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i

abstract class KryptonHangingEntity(
    world: KryptonWorld,
    type: EntityType<out HangingEntity>
) : KryptonEntity(world, type), HangingEntity {

    abstract val width: Int

    abstract val height: Int

    private var centerPosition = Vector3i(location.floorX(), location.floorY(), location.floorZ())
    final override var direction: Direction = Direction.SOUTH
        set(value) {
            require(value.axis.isHorizontal)
            field = value
            // Each horizontal direction is a half turn (90 degrees) away from each other.
            // The 2D data value is 0 = south, 1 = west, 2 = north, 3 = east.
            rotation = Vector2f(rotation.x(), value.data2D() * HALF_TURN_DEGREES)
            recalculateBoundingBox()
        }

    private fun recalculateBoundingBox() {
        var x = centerPosition.x() + BLOCK_CENTER_OFFSET
        var y = centerPosition.y() + BLOCK_CENTER_OFFSET
        var z = centerPosition.z() + BLOCK_CENTER_OFFSET
        // for EAST, the x becomes centerX + 1/32, and for WEST, the x becomes centerX + 31/32
        x -= direction.normalX * ALMOST_TWO_BLOCKS_PIXELS
        // for SOUTH, the z becomes centerZ + 1/32, and for NORTH, the z becomes centerZ + 31/32
        z -= direction.normalZ * ALMOST_TWO_BLOCKS_PIXELS
        if (height % TWO_BLOCKS_PICTURE_PIXELS == 0) y += BLOCK_CENTER_OFFSET
        val antiClockwise = direction.antiClockwise()
        if (width % TWO_BLOCKS_PICTURE_PIXELS == 0) {
            // for direction NORTH (anti clockwise WEST), the x becomes centerX
            // for direction SOUTH (anti clockwise EAST), the x becomes centerX + 1
            x += BLOCK_CENTER_OFFSET * antiClockwise.normalX
        }
        if (width % TWO_BLOCKS_PICTURE_PIXELS == 0) {
            // for direction EAST (anti clockwise NORTH), the z becomes centerZ
            // for direction WEST (anti clockwise SOUTH), the z becomes centerZ + 1
            z += BLOCK_CENTER_OFFSET * antiClockwise.normalZ
        }
        location = Vector3d(x, y, z)
        var xWidth = width.toDouble()
        var height = height.toDouble()
        var zWidth = width.toDouble()
        if (direction.axis == Direction.Axis.Z) zWidth = 1.0 else xWidth = 1.0
        xWidth /= TWO_BLOCKS_PICTURE_PIXELS
        height /= TWO_BLOCKS_PICTURE_PIXELS
        zWidth /= TWO_BLOCKS_PICTURE_PIXELS
        boundingBox = KryptonBoundingBox(x - xWidth, y - height, z - zWidth, x + xWidth, y + height, z + zWidth)
    }

    companion object {

        // The offset to add to a block coordinate to make it in the centre of the block.
        private const val BLOCK_CENTER_OFFSET = 0.5
        private const val TWO_BLOCKS_PICTURE_PIXELS = 16 * 2 // 16 pixels on a picture = 1 block.
        private const val ALMOST_TWO_BLOCKS_PIXELS = 31 / 32 // Wish I had a better name for this
        private const val HALF_TURN_DEGREES = 90F
    }
}
