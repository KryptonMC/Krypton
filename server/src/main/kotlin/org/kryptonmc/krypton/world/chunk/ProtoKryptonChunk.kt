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
package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.world.Heightmap

class ProtoKryptonChunk(val wrapped: KryptonChunk) : ProtoChunk(wrapped.position, emptyArray(), wrapped) {

    override val position = wrapped.position
    override val sections = wrapped.sections
    override var isUnsaved = false
        set(_) = Unit
    override var status = wrapped.status
        set(_) = Unit
    override var isLightCorrect = wrapped.isLightCorrect
        set(_) = Unit

    override fun getBlock(x: Int, y: Int, z: Int) = wrapped.getBlock(x, y, z)

    override fun setBlock(x: Int, y: Int, z: Int, block: Block) = Unit

    override fun setHeightmap(type: Heightmap.Type, data: LongArray) = Unit
}
