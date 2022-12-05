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
package org.kryptonmc.krypton.adventure

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.kryptonmc.krypton.util.resultOrError
import org.kryptonmc.serialization.DataResult
import java.util.Locale

object TextColorSerialization {

    @JvmStatic
    fun encode(color: TextColor): String {
        if (color is NamedTextColor) return NamedTextColor.NAMES.key(color)!!
        return String.format(Locale.ROOT, "#%06X", color.value())
    }

    @JvmStatic
    fun decode(input: String): TextColor? = if (input.startsWith("#")) TextColor.fromHexString(input) else NamedTextColor.NAMES.value(input)

    @JvmStatic
    fun decodeResult(input: String): DataResult<TextColor> =
        decode(input).resultOrError { "Input string $input is not a valid named colour or hex colour!" }
}
