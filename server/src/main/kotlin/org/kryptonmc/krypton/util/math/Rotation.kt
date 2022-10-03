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
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.util.serialization.EnumCodecs
import org.kryptonmc.serialization.Codec

enum class Rotation(val id: String, val rotation: OctahedralGroup) {

    NONE("none", OctahedralGroup.IDENTITY),
    CLOCKWISE_90("clockwise_90", OctahedralGroup.ROT_90_Y_NEG),
    CLOCKWISE_180("180", OctahedralGroup.ROT_180_FACE_XZ),
    ANTICLOCKWISE_90("counterclockwise_90", OctahedralGroup.ROT_90_Y_POS);

    fun getRotated(rotation: Rotation): Rotation = when (rotation) {
        CLOCKWISE_180 -> when (this) {
            NONE -> CLOCKWISE_180
            CLOCKWISE_90 -> ANTICLOCKWISE_90
            CLOCKWISE_180 -> NONE
            ANTICLOCKWISE_90 -> CLOCKWISE_90
        }
        ANTICLOCKWISE_90 -> when (this) {
            NONE -> ANTICLOCKWISE_90
            CLOCKWISE_90 -> NONE
            CLOCKWISE_180 -> CLOCKWISE_90
            ANTICLOCKWISE_90 -> CLOCKWISE_180
        }
        CLOCKWISE_90 -> when (this) {
            NONE -> CLOCKWISE_90
            CLOCKWISE_90 -> CLOCKWISE_180
            CLOCKWISE_180 -> ANTICLOCKWISE_90
            ANTICLOCKWISE_90 -> NONE
        }
        else -> this
    }

    fun rotate(facing: Direction): Direction {
        if (facing.axis == Direction.Axis.Y) return facing
        return when (this) {
            CLOCKWISE_90 -> Directions.clockwise(facing)
            CLOCKWISE_180 -> facing.opposite
            ANTICLOCKWISE_90 -> Directions.antiClockwise(facing)
            else -> facing
        }
    }

    fun rotate(rotation: Int, positionCount: Int): Int = when (this) {
        CLOCKWISE_90 -> (rotation + positionCount / 4) % positionCount
        CLOCKWISE_180 -> (rotation + positionCount / 2) % positionCount
        ANTICLOCKWISE_90 -> (rotation + positionCount * 3 / 4) % positionCount
        else -> rotation
    }

    companion object {

        @JvmField
        val CODEC: Codec<Rotation> = EnumCodecs.of(Rotation::values, Rotation::id)
    }
}
