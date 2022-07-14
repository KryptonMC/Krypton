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
package org.kryptonmc.krypton.effect.particle.data

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.effect.particle.data.DustParticleData
import org.kryptonmc.api.util.Color
import org.kryptonmc.krypton.network.Writable

@JvmRecord
data class KryptonDustParticleData(override val color: Color, override val scale: Float) : DustParticleData, Writable {

    constructor(buf: ByteBuf) : this(buf.readColor(), buf.readFloat())

    override fun write(buf: ByteBuf) {
        buf.writeFloat(if (red == 0) Float.MIN_VALUE else red.toFloat() / 255F)
        buf.writeFloat(green.toFloat() / 255F)
        buf.writeFloat(blue.toFloat() / 255F)
        buf.writeFloat(scale)
    }
}


private fun ByteBuf.readColor(): Color = Color.of(readColorValue(), readColorValue(), readColorValue())

private fun ByteBuf.readColorValue(): Int = (readFloat() * 255F).toInt()
