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

    open fun rotation(): Float {
        val position = player?.position ?: return 0F
        return position.pitch
    }
}
