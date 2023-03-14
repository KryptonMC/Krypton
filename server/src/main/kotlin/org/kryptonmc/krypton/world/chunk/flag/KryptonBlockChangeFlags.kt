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
package org.kryptonmc.krypton.world.chunk.flag

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

    object Factory : BlockChangeFlags.Factory {

        override fun none(): BlockChangeFlags = KryptonBlockChangeFlags(0)

        override fun all(): BlockChangeFlags = KryptonBlockChangeFlags(SetBlockFlag.ALL)
    }
}
