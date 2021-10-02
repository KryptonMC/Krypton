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
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.command.CommandExceptions
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector3d

class WorldCoordinates(val x: WorldCoordinate, val y: WorldCoordinate, val z: WorldCoordinate) : Coordinates {

    override val relativeX = x.isRelative
    override val relativeY = y.isRelative
    override val relativeZ = z.isRelative

    override fun position(player: Player) = Vector3d(
        x[player.location.x()],
        y[player.location.y()],
        z[player.location.z()]
    )

    override fun rotation(player: Player) = Vector2d(
        x[player.rotation.x().toDouble()],
        y[player.rotation.y().toDouble()]
    )

    companion object {

        fun parse(reader: StringReader, correctCenter: Boolean): WorldCoordinates {
            val position = reader.cursor
            val x = WorldCoordinate.parse(reader, correctCenter)
            if (!reader.canRead() || reader.peek() != ' ') {
                reader.cursor = position
                throw CommandExceptions.POSITION_3D_INCOMPLETE.createWithContext(reader)
            }

            reader.skip()
            val y = WorldCoordinate.parse(reader, false)
            if (!reader.canRead() || reader.peek() != ' ') {
                reader.cursor = position
                throw CommandExceptions.POSITION_3D_INCOMPLETE.createWithContext(reader)
            }

            reader.skip()
            val z = WorldCoordinate.parse(reader, correctCenter)
            return WorldCoordinates(x, y, z)
        }
    }
}
