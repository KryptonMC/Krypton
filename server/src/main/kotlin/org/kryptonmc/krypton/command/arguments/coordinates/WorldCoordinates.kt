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

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.StringReader
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.util.Vec3dImpl

@JvmRecord
data class WorldCoordinates(val x: WorldCoordinate, val y: WorldCoordinate, val z: WorldCoordinate) : Coordinates {

    override val hasRelativeX: Boolean
        get() = x.isRelative
    override val hasRelativeY: Boolean
        get() = y.isRelative
    override val hasRelativeZ: Boolean
        get() = z.isRelative

    override fun position(source: CommandSourceStack): Vec3d =
        Vec3dImpl(x.get(source.position.x), y.get(source.position.y), z.get(source.position.z))

    companion object {

        @JvmStatic
        fun parse(reader: StringReader, correctCenter: Boolean): WorldCoordinates {
            val position = reader.cursor
            val x = WorldCoordinate.parse(reader, correctCenter)
            if (!reader.canRead() || reader.peek() != CommandDispatcher.ARGUMENT_SEPARATOR_CHAR) {
                reader.cursor = position
                throw CoordinateExceptions.POSITION_3D_INCOMPLETE.createWithContext(reader)
            }

            reader.skip()
            val y = WorldCoordinate.parse(reader, false)
            if (!reader.canRead() || reader.peek() != CommandDispatcher.ARGUMENT_SEPARATOR_CHAR) {
                reader.cursor = position
                throw CoordinateExceptions.POSITION_3D_INCOMPLETE.createWithContext(reader)
            }

            reader.skip()
            val z = WorldCoordinate.parse(reader, correctCenter)
            return WorldCoordinates(x, y, z)
        }
    }
}
