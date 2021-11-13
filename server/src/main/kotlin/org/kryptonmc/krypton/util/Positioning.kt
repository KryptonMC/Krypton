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
package org.kryptonmc.krypton.util

object Positioning {

    /**
     * Calculates the change in position between the given [new] and [old] coordinates.
     * No idea why Mojang thought having player coordinates be absolute and entity
     * coordinates be relative.
     *
     * This calculation comes from https://wiki.vg/Protocol#Entity_Position
     */
    fun delta(new: Double, old: Double) = ((new * 32 - old * 32) * 128).toInt().toShort()

    /**
     * Encodes the given [velocity] in to the protocol's standard velocity
     * units, measured in 1/8000 of a block per server tick.
     */
    fun encodeVelocity(velocity: Double): Int = (velocity.clamp(-3.9, 3.9) * 8000.0).toInt()
}
