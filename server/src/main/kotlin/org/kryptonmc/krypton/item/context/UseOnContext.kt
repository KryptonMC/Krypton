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
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.hit.BlockHitResult
import org.kryptonmc.krypton.world.KryptonWorld

open class UseOnContext protected constructor(
    val world: KryptonWorld,
    val player: KryptonPlayer?,
    val hand: Hand,
    val item: KryptonItemStack,
    protected val hitResult: BlockHitResult
) {

    constructor(player: KryptonPlayer, hand: Hand, hitResult: BlockHitResult) : this(player.world, player, hand, player.getHeldItem(hand), hitResult)

    open fun clickedPosition(): Vec3i = hitResult.position

    fun clickedFace(): Direction = hitResult.direction

    fun clickLocation(): Vec3d = hitResult.location

    fun isInside(): Boolean = hitResult.isInside

    open fun horizontalDirection(): Direction = player?.facing ?: Direction.NORTH

    open fun isSneaking(): Boolean = player != null && player.isSneaking

    open fun rotation(): Float = player?.pitch ?: 0F
}
