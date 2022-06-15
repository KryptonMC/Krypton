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
package org.kryptonmc.krypton.world.fluid.handler

import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i
import java.util.Random

/**
 * A handler for one (or multiple) [Fluid]s.
 */
interface FluidHandler {

    /**
     * Gets the movement vector for the current flow of the given [fluid] at
     * the given [x], [y], and [z] coordinates, in the given [world].
     */
    fun flow(fluid: Fluid, x: Int, y: Int, z: Int, world: KryptonWorld): Vector3d = Vector3d.ZERO

    /**
     * Gets the height of the given [fluid] at the given [x], [y], and [z]
     * coordinates in the given [world].
     */
    fun height(fluid: Fluid, x: Int, y: Int, z: Int, world: KryptonWorld): Float = 0F

    /**
     * Called when the given [fluid] is ticked at the given [position] in the
     * given [world].
     */
    fun tick(fluid: Fluid, position: Vector3i, world: KryptonWorld) {
        // fluids don't tick by default
    }
}
