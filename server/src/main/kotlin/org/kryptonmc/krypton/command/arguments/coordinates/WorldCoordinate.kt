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
package org.kryptonmc.krypton.command.arguments.coordinates

import com.mojang.brigadier.StringReader
import org.kryptonmc.krypton.command.arguments.StringReading

@JvmRecord
data class WorldCoordinate(val value: Double, val isRelative: Boolean) {

    fun get(relativeTo: Double): Double = if (isRelative) value + relativeTo else value

    companion object {

        @JvmStatic
        fun parse(reader: StringReader, correctCenter: Boolean): WorldCoordinate {
            // Got a caret (local coordinate) when we expected to get relative coordinates
            if (reader.canRead() && reader.peek() == TextCoordinates.LOCAL_MODIFIER) {
                throw CoordinateExceptions.POSITION_MIXED_TYPE.createWithContext(reader)
            }
            if (!reader.canRead()) throw CoordinateExceptions.POSITION_EXPECTED_DOUBLE.createWithContext(reader)

            // Check for relative positioning
            val relative = reader.peek() == TextCoordinates.RELATIVE_MODIFIER
            if (relative) reader.skip()

            val position = reader.cursor
            var value = StringReading.readOptionalDouble(reader)
            val remaining = reader.string.substring(position, reader.cursor)
            if (relative && remaining.isEmpty()) return WorldCoordinate(0.0, true)
            if (!remaining.contains('.') && !relative && correctCenter) value += 0.5
            return WorldCoordinate(value, relative)
        }
    }
}
