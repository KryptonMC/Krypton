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
package org.kryptonmc.krypton.coordinate

import org.kryptonmc.krypton.util.math.Maths
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.max

object Positioning {

    /**
     * Calculates the change in position between the given [new] and [old] coordinates.
     * No idea why Mojang thought having player coordinates be absolute and entity
     * coordinates be relative.
     *
     * See [here](https://wiki.vg/Protocol#Entity_Position)
     */
    @JvmStatic
    fun calculateDelta(new: Double, old: Double): Short = ((new * 32 - old * 32) * 128).toInt().toShort()

    /**
     * Encodes the given [velocity] in to the protocol's standard velocity
     * units, measured in 1/8000 of a block per server tick.
     */
    @JvmStatic
    fun encodeVelocity(velocity: Double): Short {
        val clamped = Maths.clamp(velocity, -3.9, 3.9)
        return (clamped * 8000.0).toInt().toShort()
    }

    @JvmStatic
    fun encodeRotation(rotation: Float): Byte = Maths.floor(rotation * 256F / 360F).toByte()

    @JvmStatic
    fun calculateLookYaw(dx: Double, dz: Double): Float {
        val radians = atan2(dz, dx)
        val degrees = Math.toDegrees(radians).toFloat() - 90
        if (degrees < -180) return degrees + 360
        if (degrees > 180) return degrees - 360
        return degrees
    }

    @JvmStatic
    fun calculateLookPitch(dx: Double, dy: Double, dz: Double): Float {
        val radians = -atan2(dy, max(abs(dx), abs(dz)))
        return Math.toDegrees(radians).toFloat()
    }
}
