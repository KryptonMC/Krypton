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
package org.kryptonmc.krypton.adventure

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap
import net.kyori.adventure.text.format.NamedTextColor

private val NAMED_TEXT_COLOR_ID_MAP = Object2IntArrayMap<NamedTextColor>(16).apply {
    put(NamedTextColor.BLACK, 0)
    put(NamedTextColor.DARK_BLUE, 1)
    put(NamedTextColor.DARK_GREEN, 2)
    put(NamedTextColor.DARK_AQUA, 3)
    put(NamedTextColor.DARK_RED, 4)
    put(NamedTextColor.DARK_PURPLE, 5)
    put(NamedTextColor.GOLD, 6)
    put(NamedTextColor.GRAY, 7)
    put(NamedTextColor.DARK_GRAY, 8)
    put(NamedTextColor.BLUE, 9)
    put(NamedTextColor.GREEN, 10)
    put(NamedTextColor.AQUA, 11)
    put(NamedTextColor.RED, 12)
    put(NamedTextColor.LIGHT_PURPLE, 13)
    put(NamedTextColor.YELLOW, 14)
    put(NamedTextColor.WHITE, 15)
}

fun NamedTextColor.ordinal(): Int = NAMED_TEXT_COLOR_ID_MAP.getInt(this)
