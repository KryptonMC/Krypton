/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.server.gui.stats

import org.kryptonmc.krypton.util.square
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

object GraphColor {

    val lineColors = Array(101) {
        val colour = create(it)
        Color(colour.red / 2, colour.green / 2, colour.blue / 2, 255)
    }
    val fillColors = Array(101) { Color(lineColors[it].red, lineColors[it].green, lineColors[it].blue, 125) }

    private fun create(percent: Int): Color {
        if (percent <= 50) return Color(0x00FF00)

        val value = 510 - (min(max(0, ((percent - 50) / 50)), 1) * 510)

        val (red, green) = if (value < 255) {
            255 to (sqrt(value.toDouble()) * 16).toInt()
        } else {
            val newValue = value - 255
            (255 - (newValue.square() / 255)) to 255
        }
        return Color(red, green, 0)
    }
}
