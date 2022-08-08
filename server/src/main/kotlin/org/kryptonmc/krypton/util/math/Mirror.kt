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
package org.kryptonmc.krypton.util.math

import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.serialization.EnumCodecs
import org.kryptonmc.serialization.Codec

enum class Mirror(val rotation: OctahedralGroup) {

    NONE(OctahedralGroup.IDENTITY),
    LEFT_RIGHT(OctahedralGroup.INVERT_Z),
    FRONT_BACK(OctahedralGroup.INVERT_X);

    fun mirror(rotation: Int, rotationCount: Int): Int {
        val midpoint = rotationCount / 2
        val relativeRotation = if (rotation > midpoint) rotation - rotationCount else rotation
        return when (this) {
            FRONT_BACK -> (rotationCount - relativeRotation) % rotationCount
            LEFT_RIGHT -> (midpoint - relativeRotation + rotationCount) % rotationCount
            else -> rotation
        }
    }

    fun rotation(direction: Direction): Rotation {
        val axis = direction.axis
        if ((this != LEFT_RIGHT || axis != Direction.Axis.Z) && (this != FRONT_BACK || axis != Direction.Axis.X)) return Rotation.NONE
        return Rotation.CLOCKWISE_180
    }

    fun mirror(direction: Direction): Direction = when {
        this == FRONT_BACK && direction.axis == Direction.Axis.X -> direction.opposite
        this == LEFT_RIGHT && direction.axis == Direction.Axis.Z -> direction.opposite
        else -> direction
    }

    companion object {

        @JvmField
        val CODEC: Codec<Mirror> = EnumCodecs.of(Mirror::values)
    }
}
