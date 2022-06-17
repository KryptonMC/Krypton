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
package org.kryptonmc.krypton.item.data

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.TextColor
import org.kryptonmc.api.item.data.DyeColor
import java.awt.Color

@JvmRecord
data class KryptonDyeColor(
    private val key: Key,
    override val color: Color,
    override val fireworkColor: Color,
    override val textColor: TextColor
) : DyeColor {

    override fun key(): Key = key

    object Factory : DyeColor.Factory {

        override fun of(key: Key, color: Color, fireworkColor: Color, textColor: TextColor): DyeColor =
            KryptonDyeColor(key, color, fireworkColor, textColor)
    }
}
