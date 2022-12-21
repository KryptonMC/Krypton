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
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.hit.BlockHitResult
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.world.KryptonWorld

open class BlockPlaceContext protected constructor(
    world: KryptonWorld,
    player: KryptonPlayer?,
    hand: Hand,
    item: KryptonItemStack,
    hitResult: BlockHitResult
) : UseOnContext(world, player, hand, item, hitResult) {

    private val relativePosition = hitResult.position.relative(hitResult.direction)
    protected var replaceClicked = world.getBlock(hitResult.position).canBeReplaced(this)

    override fun clickedPosition(): BlockPos = if (replaceClicked) super.clickedPosition() else relativePosition

    open fun canPlace(): Boolean = replaceClicked || world.getBlock(clickedPosition()).canBeReplaced(this)

    open fun replacingClickedOnBlock(): Boolean = replaceClicked
}
