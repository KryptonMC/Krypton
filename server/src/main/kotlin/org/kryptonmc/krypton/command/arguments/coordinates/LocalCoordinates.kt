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
import org.spongepowered.math.vector.Vector2d
import org.spongepowered.math.vector.Vector3d
import kotlin.math.cos
import kotlin.math.sin

class LocalCoordinates(
    private val left: Double,
    private val up: Double,
    private val forwards: Double
) : Coordinates {

    override fun position(player: Player): Vector3d {
        val rotation = player.rotation
        val dividedPi = Math.PI / 180
        val pitch1 = cos((rotation.x() + 90F) * dividedPi).toFloat()
        val pitch2 = sin((rotation.y() + 90F) * dividedPi).toFloat()
        val yaw1 = cos(-rotation.x() * dividedPi).toFloat()
        val yaw2 = sin(-rotation.x() * dividedPi).toFloat()
        val yaw3 = cos((-rotation.x() + 90F) * dividedPi).toFloat()
        val yaw4 = sin((-rotation.x() + 90F) * dividedPi).toFloat()

        val someVector = Vector3d(pitch1 * yaw1, yaw2, pitch2 * yaw1)
        val someOtherVector = Vector3d(pitch1 * yaw3, yaw4, pitch2 * yaw3)
        val scaled = someVector.cross(someOtherVector).negate()

        val x = someVector.x() * forwards + someOtherVector.x() * up + scaled.x() * left
        val y = someVector.y() * forwards + someOtherVector.y() * up + scaled.y() * left
        val z = someVector.z() * forwards + someOtherVector.z() * up + scaled.z() * left
        return Vector3d(player.location.x() + x, player.location.y() + y, player.location.z() + z)
    }

    override fun rotation(player: Player): Vector2d = Vector2d.ZERO

    companion object {

        fun parse(reader: StringReader): LocalCoordinates {
            val resetPosition = reader.cursor
            val left = reader.readPositionalDouble(resetPosition)
            if (!reader.canRead() || reader.peek() != ' ') {
                reader.cursor = resetPosition
                throw CoordinateExceptions.POSITION_3D_INCOMPLETE.createWithContext(reader)
            }
            reader.skip()

            val up = reader.readPositionalDouble(resetPosition)
            if (!reader.canRead() || reader.peek() != ' ') {
                reader.cursor = resetPosition
                throw CoordinateExceptions.POSITION_3D_INCOMPLETE.createWithContext(reader)
            }
            reader.skip()

            val forwards = reader.readPositionalDouble(resetPosition)
            return LocalCoordinates(left, up, forwards)
        }

        private fun StringReader.readPositionalDouble(resetPosition: Int): Double {
            if (!canRead()) throw CoordinateExceptions.POSITION_EXPECTED_DOUBLE.createWithContext(this)
            if (peek() != '^') {
                cursor = resetPosition
                throw CoordinateExceptions.POSITION_MIXED_TYPE.createWithContext(this)
            }
            skip()
            return if (canRead() && peek() != ' ') readDouble() else 0.0
        }
    }
}
