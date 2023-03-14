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
package org.kryptonmc.krypton.item.context

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.hit.BlockHitResult
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

    override fun clickedPosition(): Vec3i = if (replaceClicked) super.clickedPosition() else relativePosition

    open fun canPlace(): Boolean = replaceClicked || world.getBlock(clickedPosition()).canBeReplaced(this)

    open fun replacingClickedOnBlock(): Boolean = replaceClicked
}
