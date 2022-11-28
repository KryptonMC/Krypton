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
package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.api.world.chunk.BlockChangeFlags

@JvmRecord
data class KryptonBlockChangeFlags(override val raw: Int) : BlockChangeFlags {

    override val updateNeighbours: Boolean
        get() = raw and SetBlockFlag.UPDATE_NEIGHBOURS != 0
    override val notifyClients: Boolean
        get() = raw and SetBlockFlag.NOTIFY_CLIENTS != 0
    override val updateNeighbourShapes: Boolean
        get() = raw and SetBlockFlag.UPDATE_NEIGHBOUR_SHAPES != 0
    override val neighbourDrops: Boolean
        get() = raw and SetBlockFlag.NEIGHBOUR_DROPS != 0
    override val blockMoving: Boolean
        get() = raw and SetBlockFlag.BLOCK_MOVING != 0
    override val lighting: Boolean
        get() = raw and SetBlockFlag.LIGHTING != 0

    override fun withUpdateNeighbours(updateNeighbours: Boolean): BlockChangeFlags {
        if (this.updateNeighbours == updateNeighbours) return this
        return KryptonBlockChangeFlags(raw or SetBlockFlag.UPDATE_NEIGHBOURS)
    }

    override fun withNotifyClients(notifyClients: Boolean): BlockChangeFlags {
        if (this.notifyClients == notifyClients) return this
        return KryptonBlockChangeFlags(raw or SetBlockFlag.NOTIFY_CLIENTS)
    }

    override fun withUpdateNeighbourShapes(updateNeighbourShapes: Boolean): BlockChangeFlags {
        if (this.updateNeighbourShapes == updateNeighbourShapes) return this
        return KryptonBlockChangeFlags(raw or SetBlockFlag.UPDATE_NEIGHBOUR_SHAPES)
    }

    override fun withNeighbourDrops(neighbourDrops: Boolean): BlockChangeFlags {
        if (this.neighbourDrops == neighbourDrops) return this
        return KryptonBlockChangeFlags(raw or SetBlockFlag.NEIGHBOUR_DROPS)
    }

    override fun withBlockMoving(blockMoving: Boolean): BlockChangeFlags {
        if (this.blockMoving == blockMoving) return this
        return KryptonBlockChangeFlags(raw or SetBlockFlag.BLOCK_MOVING)
    }

    override fun withLighting(lighting: Boolean): BlockChangeFlags {
        if (this.lighting == lighting) return this
        return KryptonBlockChangeFlags(raw or SetBlockFlag.LIGHTING)
    }

    override fun not(): BlockChangeFlags = KryptonBlockChangeFlags(raw.inv())

    override fun and(other: BlockChangeFlags): BlockChangeFlags = KryptonBlockChangeFlags(raw and other.raw)

    override fun or(other: BlockChangeFlags): BlockChangeFlags = KryptonBlockChangeFlags(raw or other.raw)
}
