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
package org.kryptonmc.krypton.util

import io.netty.handler.codec.DecoderException
import kotlin.math.ceil

object ByteBufExtras {

    private val VARINT_EXACT_BYTE_LENGTHS = IntArray(33) { ceil((31.0 - (it - 1)) / 7.0).toInt() }.apply { this[32] = 1 }

    fun getVarIntBytes(value: Int): Int = VARINT_EXACT_BYTE_LENGTHS[value.countLeadingZeroBits()]

    @JvmStatic
    inline fun <T> limitValue(crossinline function: (Int) -> T, limit: Int): (Int) -> T = {
        if (it > limit) throw DecoderException("Value $it is larger than limit $limit!")
        function(it)
    }
}
