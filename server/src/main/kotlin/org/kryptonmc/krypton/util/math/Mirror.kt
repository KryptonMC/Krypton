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
        val CODEC: Codec<Mirror> = EnumCodecs.of { Mirror.values() }
    }
}
