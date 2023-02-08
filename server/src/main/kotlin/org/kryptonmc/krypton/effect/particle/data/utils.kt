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
import org.kryptonmc.api.util.Color

// This constant is used to transform an integer colour value (between 0 and 255 because it's RGB) to a floating point value (between 0.0 and 1.0)
// and vice versa.
private const val COLOR_ENCODING_VALUE = 255F

internal fun ByteBuf.readParticleColor(): Color = Color(readColorValue(), readColorValue(), readColorValue())

private fun ByteBuf.readColorValue(): Int = (readFloat() * COLOR_ENCODING_VALUE).toInt()

internal fun ByteBuf.writeParticleColor(color: Color) {
    writeFloat(if (color.red == 0) Float.MIN_VALUE else color.red.toFloat() / COLOR_ENCODING_VALUE)
    writeFloat(color.green.toFloat() / COLOR_ENCODING_VALUE)
    writeFloat(color.blue.toFloat() / COLOR_ENCODING_VALUE)
}
