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

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.world.block.palette.GlobalPalette
import org.kryptonmc.krypton.world.block.palette.PaletteHolder

/**
 * A section of a chunk. These are 16x16x16 areas that hold the actual block
 * states and palette information.
 */
class ChunkSection(
    y: Int,
    val blockLight: ByteArray = ByteArray(2048),
    val skyLight: ByteArray = ByteArray(2048)
) {

    val bottomBlockY = y shl 4
    val palette = PaletteHolder(GlobalPalette)
    private var nonEmptyBlockCount = 0
    val serializedSize: Int
        get() = 2 + palette.serializedSize

    operator fun get(x: Int, y: Int, z: Int) = palette[x, y, z]

    operator fun set(x: Int, y: Int, z: Int, block: Block): Block {
        val oldBlock = palette.getAndSet(x, y, z, block)
        if (!oldBlock.isAir) nonEmptyBlockCount--
        if (!block.isAir) nonEmptyBlockCount++
        return oldBlock
    }

    fun isEmpty() = nonEmptyBlockCount == 0

    fun recount() {
        nonEmptyBlockCount = 0
        palette.forEachLocation { block, _ ->
            val fluid = block.asFluid()
            if (!block.isAir) nonEmptyBlockCount++
            if (!fluid.isEmpty) nonEmptyBlockCount++
        }
    }

    fun write(buf: ByteBuf) {
        buf.writeShort(nonEmptyBlockCount)
        palette.write(buf)
    }
}
