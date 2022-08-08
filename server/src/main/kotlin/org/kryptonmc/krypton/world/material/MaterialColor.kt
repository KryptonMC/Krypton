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
package org.kryptonmc.krypton.world.material

import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.item.data.DyeColors

@JvmRecord
data class MaterialColor(val id: Int, val color: Int) {

    init {
        if (id < 0 || id > 63) throw IndexOutOfBoundsException("Material colour ID must be between 0 and 63 inclusive!")
        VALUES[id] = this
    }

    companion object {

        private val VALUES = arrayOfNulls<MaterialColor>(64)
        private val BY_DYE_COLOR = mapOf(
            DyeColors.WHITE to MaterialColors.SNOW,
            DyeColors.ORANGE to MaterialColors.COLOR_ORANGE,
            DyeColors.MAGENTA to MaterialColors.COLOR_MAGENTA,
            DyeColors.LIGHT_BLUE to MaterialColors.COLOR_LIGHT_BLUE,
            DyeColors.YELLOW to MaterialColors.COLOR_YELLOW,
            DyeColors.LIME to MaterialColors.COLOR_LIGHT_GREEN,
            DyeColors.PINK to MaterialColors.COLOR_PINK,
            DyeColors.GRAY to MaterialColors.COLOR_GRAY,
            DyeColors.LIGHT_GRAY to MaterialColors.COLOR_LIGHT_GRAY,
            DyeColors.CYAN to MaterialColors.COLOR_CYAN,
            DyeColors.PURPLE to MaterialColors.COLOR_PURPLE,
            DyeColors.BLUE to MaterialColors.COLOR_BLUE,
            DyeColors.BROWN to MaterialColors.COLOR_BROWN,
            DyeColors.GREEN to MaterialColors.COLOR_GREEN,
            DyeColors.RED to MaterialColors.COLOR_RED,
            DyeColors.BLACK to MaterialColors.COLOR_BLACK
        )

        @JvmStatic
        fun fromId(id: Int): MaterialColor {
            if (id < 0 || id > VALUES.size) throw IndexOutOfBoundsException("Material colour ID must be between 0 and 63 inclusive!")
            return VALUES[id] ?: MaterialColors.NONE
        }

        @JvmStatic
        fun fromDyeColor(color: DyeColor): MaterialColor =
            checkNotNull(BY_DYE_COLOR[color]) { "Could not find material colour for dye colour $color! This is a bug!" }
    }
}
