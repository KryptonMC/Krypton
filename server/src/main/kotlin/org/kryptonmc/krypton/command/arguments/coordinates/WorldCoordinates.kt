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
import org.kryptonmc.api.entity.entities.Player
import org.kryptonmc.api.space.Vector
import org.kryptonmc.krypton.command.arguments.ERROR_MIXED_TYPE
import org.kryptonmc.krypton.command.arguments.ERROR_NOT_COMPLETE
import org.spongepowered.math.vector.Vector2d

class WorldCoordinates(val x: WorldCoordinate, val y: WorldCoordinate, val z: WorldCoordinate) : Coordinates {

    override val relativeX = x.isRelative
    override val relativeY = y.isRelative
    override val relativeZ = z.isRelative

    override fun position(player: Player) = Vector(x[player.location.x], y[player.location.y], z[player.location.z])

    override fun rotation(player: Player) = Vector2d(x[player.location.yaw.toDouble()], y[player.location.pitch.toDouble()])

    companion object {

        fun parseDouble(reader: StringReader, correctCenter: Boolean): WorldCoordinates {
            val position = reader.cursor
            val x = WorldCoordinate.parseDouble(reader, correctCenter)
            if (!reader.canRead() || reader.peek() != ' ') {
                reader.cursor = position
                throw ERROR_NOT_COMPLETE.createWithContext(reader)
            }
            reader.skip()
            val y = WorldCoordinate.parseDouble(reader, false)
            if (!reader.canRead() || reader.peek() != ' ') {
                reader.cursor = position
                throw ERROR_NOT_COMPLETE.createWithContext(reader)
            }
            reader.skip()
            val z = WorldCoordinate.parseDouble(reader, correctCenter)
            return WorldCoordinates(x, y, z)
        }

        fun parseInt(reader: StringReader, correctCenter: Boolean): WorldCoordinates {
            val position = reader.cursor
            val x = WorldCoordinate.parseInt(reader)
            if (!reader.canRead() || reader.peek() != ' ') {
                reader.cursor = position
                throw ERROR_NOT_COMPLETE.createWithContext(reader)
            }
            reader.skip()
            val y = WorldCoordinate.parseInt(reader)
            if (!reader.canRead() || reader.peek() != ' ') {
                reader.cursor = position
                throw ERROR_NOT_COMPLETE.createWithContext(reader)
            }
            reader.skip()
            val z = WorldCoordinate.parseInt(reader)
            return WorldCoordinates(x, y, z)
        }
    }
}

data class WorldCoordinate(
    val value: Double,
    val isRelative: Boolean
) {

    operator fun get(relativeTo: Double) = if (isRelative) value + relativeTo else value

    companion object {


        fun parseDouble(reader: StringReader, correctCenter: Boolean): WorldCoordinate {
            if (reader.canRead() && reader.peek() == '^') throw ERROR_MIXED_TYPE.createWithContext(reader)
            if (!reader.canRead()) throw ERROR_EXPECTED_DOUBLE.createWithContext(reader)

            val relative = reader.isRelative()
            val position = reader.cursor
            var value = if (reader.canRead() && reader.peek() != ' ') reader.readDouble() else 0.0
            val string = reader.string.substring(position, reader.cursor)
            if (relative && string.isEmpty()) return WorldCoordinate(0.0, true)
            if ('.' !in string && !relative && correctCenter) value += 0.5
            return WorldCoordinate(value, relative)
        }

        fun parseInt(reader: StringReader): WorldCoordinate {
            if (reader.canRead() && reader.peek() == '^') throw ERROR_MIXED_TYPE.createWithContext(reader)
            if (!reader.canRead()) throw ERROR_EXPECTED_INTEGER.createWithContext(reader)
            val relative = reader.isRelative()
            val value = if (reader.canRead() && reader.peek() != ' ') {
                if (relative) reader.readDouble() else reader.readInt().toDouble()
            } else 0.0
            return WorldCoordinate(value, relative)
        }

        private fun StringReader.isRelative() = if (peek() == '~') {
            skip()
            true
        } else false
    }
}
