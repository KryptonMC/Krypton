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
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.command.arguments.StringReading
import org.kryptonmc.krypton.util.math.Maths

/**
 * Coordinates that are local to not only a player's position, but also their
 * rotation.
 *
 * In contrast to relative coordinates, local coordinates use a "left", "up",
 * and "forward" component to calculate where the player should be moved.
 */
@JvmRecord
data class LocalCoordinates(private val left: Double, private val up: Double, private val forwards: Double) : Coordinates {

    override fun calculatePosition(source: CommandSourceStack): Position {
        // All of this is some slightly complicated linear algebra that I don't really understand.
        // What this does is determine absolute coordinates from the local forwards, up, and left components, which are relative
        // to the direction that a player is facing.
        // TODO: Document all this in detail
        val pitch = source.position.pitch
        val yaw = source.position.yaw
        val yaw1 = Maths.cos(Maths.toRadians(yaw + 90F))
        val yaw2 = Maths.sin(Maths.toRadians(yaw + 90F))
        val pitch1 = Maths.cos(Maths.toRadians(-pitch))
        val pitch2 = Maths.sin(Maths.toRadians(-pitch))
        val pitch3 = Maths.cos(Maths.toRadians(-pitch + 90F))
        val pitch4 = Maths.sin(Maths.toRadians(-pitch + 90F))

        val someVector = Vec3d((yaw1 * pitch1).toDouble(), pitch2.toDouble(), (yaw2 * pitch1).toDouble())
        val someOtherVector = Vec3d((yaw1 * pitch3).toDouble(), pitch4.toDouble(), (yaw2 * pitch3).toDouble())
        val crossed = someVector.cross(someOtherVector).negate()

        val offsetX = someVector.x * forwards + someOtherVector.x * up + crossed.x * left
        val offsetY = someVector.y * forwards + someOtherVector.y * up + crossed.y * left
        val offsetZ = someVector.z * forwards + someOtherVector.z * up + crossed.z * left
        return source.position.add(offsetX, offsetY, offsetZ)
    }

    companion object {

        @JvmStatic
        fun parse(reader: StringReader): LocalCoordinates {
            val resetPosition = reader.cursor
            val left = readPositionalDouble(reader, resetPosition)
            CoordinateExceptions.checkPositionComplete(reader, resetPosition)
            val up = readPositionalDouble(reader, resetPosition)
            CoordinateExceptions.checkPositionComplete(reader, resetPosition)
            val forwards = readPositionalDouble(reader, resetPosition)
            return LocalCoordinates(left, up, forwards)
        }

        @JvmStatic
        private fun readPositionalDouble(reader: StringReader, resetPosition: Int): Double {
            if (!reader.canRead()) throw CoordinateExceptions.POSITION_EXPECTED_DOUBLE.createWithContext(reader)
            if (reader.peek() != TextCoordinates.LOCAL_MODIFIER) {
                CommandExceptions.resetAndThrow(reader, resetPosition, CoordinateExceptions.POSITION_MIXED_TYPE.createWithContext(reader))
            }
            reader.skip()
            return StringReading.readOptionalDouble(reader)
        }
    }
}
