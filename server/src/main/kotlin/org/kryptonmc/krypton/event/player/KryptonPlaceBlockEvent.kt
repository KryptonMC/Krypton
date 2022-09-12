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
package org.kryptonmc.krypton.event.player

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.GenericResult
import org.kryptonmc.api.event.player.PlaceBlockEvent
import org.kryptonmc.api.util.Direction

data class KryptonPlaceBlockEvent(
    override val player: Player,
    override val block: Block,
    override val hand: Hand,
    override val x: Int,
    override val y: Int,
    override val z: Int,
    override val face: Direction,
    override val isInsideBlock: Boolean
) : PlaceBlockEvent {

    override var result: GenericResult = GenericResult.allowed()
}
