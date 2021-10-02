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
package org.kryptonmc.krypton.world.fluid.handler

import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidHandler
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i
import java.util.Random

object EmptyFluidHandler : FluidHandler {

    override fun canReplaceWith(
        fluid: Fluid,
        position: Vector3i,
        world: World,
        direction: Direction,
        replacement: Fluid
    ) = true

    override fun getFlow(fluid: Fluid, position: Vector3i, world: World): Vector3d = Vector3d.ZERO

    override fun getHeight(fluid: Fluid, position: Vector3i, world: World) = 0F

    override fun tick(fluid: Fluid, position: Vector3i, world: World) = Unit

    override fun randomTick(fluid: Fluid, position: Vector3i, world: World, random: Random) = Unit
}
