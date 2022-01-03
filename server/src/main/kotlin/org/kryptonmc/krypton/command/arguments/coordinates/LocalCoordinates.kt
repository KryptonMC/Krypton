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
import org.kryptonmc.api.entity.player.Player
import org.spongepowered.math.TrigMath
import org.spongepowered.math.vector.Vector3d

/**
 * Coordinates that are local to not only a player's position, but also their
 * rotation.
 *
 * In contrast to relative coordinates, local coordinates use a "left", "up",
 * and "forward" component to calculate where the player should be moved.
 */
@JvmRecord
data class LocalCoordinates(
    private val left: Double,
    private val up: Double,
    private val forwards: Double
) : Coordinates {

    override fun position(player: Player): Vector3d {
        // All of this is some slightly complicated linear algebra that I don't really understand.
        // What this does is determine absolute coordinates from the local forwards, up, and left components, which are relative
        // to the direction that a player is facing.
        val rotation = player.rotation
        val pitch1 = TrigMath.cos(Math.toRadians(rotation.y() + 90.0))
        val pitch2 = TrigMath.sin(Math.toRadians(rotation.y() + 90.0))
        val yaw1 = TrigMath.cos(Math.toRadians(-rotation.x().toDouble()))
        val yaw2 = TrigMath.sin(Math.toRadians(-rotation.x().toDouble()))
        val yaw3 = TrigMath.cos(Math.toRadians(-rotation.x() + 90.0))
        val yaw4 = TrigMath.sin(Math.toRadians(-rotation.x() + 90.0))

        val someVector = Vector3d(pitch1 * yaw1, yaw2, pitch2 * yaw1)
        val someOtherVector = Vector3d(pitch1 * yaw3, yaw4, pitch2 * yaw3)
        val scaled = someVector.cross(someOtherVector).negate()

        val offsetX = someVector.x() * forwards + someOtherVector.x() * up + scaled.x() * left
        val offsetY = someVector.y() * forwards + someOtherVector.y() * up + scaled.y() * left
        val offsetZ = someVector.z() * forwards + someOtherVector.z() * up + scaled.z() * left
        return Vector3d(player.location.x() + offsetX, player.location.y() + offsetY, player.location.z() + offsetZ)
    }

    companion object {

        @JvmStatic
        fun parse(reader: StringReader): LocalCoordinates {
            val resetPosition = reader.cursor
            val left = reader.readPositionalDouble(resetPosition)
            if (!reader.canRead() || reader.peek() != CommandDispatcher.ARGUMENT_SEPARATOR_CHAR) {
                reader.cursor = resetPosition
                throw CoordinateExceptions.POSITION_3D_INCOMPLETE.createWithContext(reader)
            }
            reader.skip()

            val up = reader.readPositionalDouble(resetPosition)
            if (!reader.canRead() || reader.peek() != CommandDispatcher.ARGUMENT_SEPARATOR_CHAR) {
                reader.cursor = resetPosition
                throw CoordinateExceptions.POSITION_3D_INCOMPLETE.createWithContext(reader)
            }
            reader.skip()

            val forwards = reader.readPositionalDouble(resetPosition)
            return LocalCoordinates(left, up, forwards)
        }

        @JvmStatic
        private fun StringReader.readPositionalDouble(resetPosition: Int): Double {
            if (!canRead()) throw CoordinateExceptions.POSITION_EXPECTED_DOUBLE.createWithContext(this)
            if (peek() != TextCoordinates.LOCAL_MODIFIER) {
                cursor = resetPosition
                throw CoordinateExceptions.POSITION_MIXED_TYPE.createWithContext(this)
            }
            skip()
            return if (canRead() && peek() != CommandDispatcher.ARGUMENT_SEPARATOR_CHAR) readDouble() else 0.0
        }
    }
}
