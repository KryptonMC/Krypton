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
package org.kryptonmc.krypton.util.math

import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.enumhelper.Directions
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
        val CODEC: Codec<Rotation> = EnumCodecs.of({ Rotation.values() }, { it.id })
    }
}
