/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.api.space.Direction
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.space.antiClockwise
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3i

abstract class KryptonHangingEntity(world: KryptonWorld, type: EntityType<out HangingEntity>) : KryptonEntity(world, type), HangingEntity {

    abstract val width: Int

    abstract val height: Int

    var centerPosition = Vector3i(location.blockX, location.blockY, location.blockZ)
        private set
    final override var direction = Direction.SOUTH
        set(value) {
            require(value.axis.isHorizontal)
            field = value
            location = location.copy(pitch = (direction.data2D * 90).toFloat())
            recalculateBoundingBox()
        }

    private fun recalculateBoundingBox() {
        var x = centerPosition.x() + 0.5
        var y = centerPosition.y() + 0.5
        var z = centerPosition.z() + 0.5
        val offWidth = offset(width)
        val offHeight = offset(height)
        x -= direction.normalX * 0.46875
        z -= direction.normalZ * 0.46875
        y += offHeight
        val antiClockwise = direction.antiClockwise()
        x += offWidth * antiClockwise.normalX
        z += offWidth * antiClockwise.normalZ
        location = location.copy(x, y, z)
        var width1 = width.toDouble()
        var height = height.toDouble()
        var width2 = width.toDouble()
        if (direction.axis == Direction.Axis.Z) width2 = 1.0 else width1 = 1.0
        width1 /= 32.0
        height /= 32.0
        width2 /= 32.0
        // TODO: Set bounding box when it exists for entities
    }

    private fun offset(value: Int) = if (value % 32 == 0) 0.5 else 0.0
}
