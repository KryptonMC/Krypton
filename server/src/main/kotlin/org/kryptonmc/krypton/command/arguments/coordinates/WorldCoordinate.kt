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
package org.kryptonmc.krypton.command.arguments.coordinates

import com.mojang.brigadier.StringReader
import org.kryptonmc.krypton.command.CommandExceptions

class WorldCoordinate(
    val value: Double,
    val isRelative: Boolean
) {

    operator fun get(relativeTo: Double) = if (isRelative) value + relativeTo else value

    companion object {

        fun parse(reader: StringReader, correctCenter: Boolean): WorldCoordinate {
            // Got a caret (local coordinate) when we expected to get relative coordinates
            if (reader.canRead() && reader.peek() == '^') throw CommandExceptions.POSITION_MIXED_TYPE.createWithContext(reader)
            if (!reader.canRead()) throw CommandExceptions.POSITION_EXPECTED_DOUBLE.createWithContext(reader)

            // Check for relative positioning
            val relative = reader.peek() == '~'
            if (relative) reader.skip()

            val position = reader.cursor
            var value = if (reader.canRead() && reader.peek() != ' ') reader.readDouble() else 0.0
            val string = reader.string.substring(position, reader.cursor)
            if (relative && string.isEmpty()) return WorldCoordinate(0.0, true)
            if ('.' !in string && !relative && correctCenter) value += 0.5
            return WorldCoordinate(value, relative)
        }
    }
}
