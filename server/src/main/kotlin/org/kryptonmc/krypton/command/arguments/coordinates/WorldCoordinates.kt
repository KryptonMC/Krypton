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
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.command.CommandSourceStack

@JvmRecord
data class WorldCoordinates(val x: WorldCoordinate, val y: WorldCoordinate, val z: WorldCoordinate) : Coordinates {

    override fun calculatePosition(source: CommandSourceStack): Vec3d =
        Vec3d(x.get(source.position.x), y.get(source.position.y), z.get(source.position.z))

    companion object {

        @JvmStatic
        fun parse(reader: StringReader, correctCenter: Boolean): WorldCoordinates {
            val position = reader.cursor
            val x = WorldCoordinate.parse(reader, correctCenter)
            CoordinateExceptions.checkPositionComplete(reader, position)
            val y = WorldCoordinate.parse(reader, false)
            CoordinateExceptions.checkPositionComplete(reader, position)
            val z = WorldCoordinate.parse(reader, correctCenter)
            return WorldCoordinates(x, y, z)
        }
    }
}
