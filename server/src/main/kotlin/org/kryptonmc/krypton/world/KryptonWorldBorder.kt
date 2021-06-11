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
package org.kryptonmc.krypton.world

import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.WorldBorder
import org.spongepowered.math.vector.Vector2d

data class KryptonWorldBorder(
    override val world: World,
    override val size: Double,
    override val center: Vector2d,
    override val damageMultiplier: Double,
    val safeZone: Double,
    val sizeLerpTarget: Double,
    val sizeLerpTime: Long,
    val warningBlocks: Int,
    val warningTime: Int
) : WorldBorder

/**
 * This is to avoid cyclic dependencies by allowing a world to build its own border
 */
data class BorderBuilder(
    val centerX: Double,
    val centerZ: Double,
    val damagePerBlock: Double,
    val size: Double,
    val safeZone: Double,
    val sizeLerpTarget: Double,
    val sizeLerpTime: Long,
    val warningBlocks: Int,
    val warningTime: Int
)
