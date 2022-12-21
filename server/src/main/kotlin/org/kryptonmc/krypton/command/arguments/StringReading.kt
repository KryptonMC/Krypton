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
package org.kryptonmc.krypton.command.arguments

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.StringReader
import org.kryptonmc.krypton.util.Keys

/**
 * Some common utilities for StringReader.
 */
object StringReading {

    @JvmStatic
    fun readKeyString(reader: StringReader): String = readStringUntil(reader) { Keys.isValidCharacter(it) }

    @JvmStatic
    fun readNonSpaceString(reader: StringReader): String = readStringUntil(reader) { it != CommandDispatcher.ARGUMENT_SEPARATOR_CHAR }

    @JvmStatic
    private inline fun readStringUntil(reader: StringReader, condition: (Char) -> Boolean): String {
        val startPosition = reader.cursor
        while (reader.canRead() && condition(reader.peek())) {
            reader.skip()
        }
        return reader.string.substring(startPosition, reader.cursor)
    }

    @JvmStatic
    fun readOptionalDouble(reader: StringReader): Double {
        if (!reader.canRead() || reader.peek() == CommandDispatcher.ARGUMENT_SEPARATOR_CHAR) return 0.0
        return reader.readDouble()
    }
}
