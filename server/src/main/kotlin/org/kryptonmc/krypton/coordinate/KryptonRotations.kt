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
package org.kryptonmc.krypton.coordinate

import org.kryptonmc.api.util.Rotations

class KryptonRotations(override val yaw: Float, override val pitch: Float, override val roll: Float) : Rotations {

    object Factory : Rotations.Factory {

        override fun of(yaw: Float, pitch: Float, roll: Float): Rotations {
            if (yaw == 0F && pitch == 0F && roll == 0F) return ZERO
            return KryptonRotations(yaw, pitch, roll)
        }
    }

    companion object {

        @JvmField
        val ZERO: KryptonRotations = KryptonRotations(0F, 0F, 0F)
    }
}
