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
package org.kryptonmc.krypton.item.context

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.BlockHitResult
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i

open class UseOnContext protected constructor(
    val world: KryptonWorld,
    val player: KryptonPlayer?,
    val hand: Hand,
    val item: KryptonItemStack,
    protected val hitResult: BlockHitResult
) {

    open val clickedPosition: Vector3i
        get() = hitResult.position
    val clickedFace: Direction
        get() = hitResult.direction
    val clickLocation: Vector3d
        get() = hitResult.location
    val isInside: Boolean
        get() = hitResult.isInside
    open val horizontalDirection: Direction
        get() = player?.direction ?: Direction.NORTH
    open val isSneaking: Boolean
        get() = player != null && player.isSneaking
    open val rotation: Float
        get() = player?.rotation?.y() ?: 0F

    constructor(player: KryptonPlayer, hand: Hand, hitResult: BlockHitResult) : this(player.world, player, hand, player.heldItem(hand), hitResult)
}
