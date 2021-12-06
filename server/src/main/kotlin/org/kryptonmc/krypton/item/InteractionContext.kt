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
package org.kryptonmc.krypton.item

import org.kryptonmc.krypton.world.block.BlockHitResult
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class InteractionContext(
    val player: KryptonPlayer,
    val world: KryptonWorld,
    val heldItem: KryptonItemStack,
    val hand: Hand,
    val hitResult: BlockHitResult
) {

    val position: Vector3i
        get() = hitResult.position
    val clickedFace: Direction
        get() = hitResult.direction
    val clickLocation: Vector3d
        get() = hitResult.clickLocation
    val isInside: Boolean
        get() = hitResult.isInside
    val pitch: Float
        get() = player.rotation.y()
}
