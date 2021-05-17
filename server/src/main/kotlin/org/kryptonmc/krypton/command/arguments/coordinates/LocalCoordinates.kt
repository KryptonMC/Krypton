package org.kryptonmc.krypton.command.arguments.coordinates

import com.mojang.brigadier.StringReader
import org.kryptonmc.api.entity.entities.Player
import org.kryptonmc.api.space.Vector
import org.kryptonmc.krypton.command.arguments.ERROR_MIXED_TYPE
import org.kryptonmc.krypton.command.arguments.ERROR_NOT_COMPLETE
import org.spongepowered.math.vector.Vector2d
import kotlin.math.cos
import kotlin.math.sin

class LocalCoordinates(
    private val left: Double,
    private val up: Double,
    private val forwards: Double
) : Coordinates {

    override fun position(player: Player): Vector {
        val location = player.location
        val dividedPi = Math.PI / 180
        val pitch1 = cos((location.pitch + 90F) * dividedPi).toFloat()
        val pitch2 = sin((location.pitch + 90F) * dividedPi).toFloat()
        val yaw1 = cos(-location.yaw * dividedPi).toFloat()
        val yaw2 = sin(-location.yaw * dividedPi).toFloat()
        val yaw3 = cos((-location.yaw + 90F) * dividedPi).toFloat()
        val yaw4 = sin((-location.yaw + 90F) * dividedPi).toFloat()

        val someVector = Vector(pitch1 * yaw1, yaw2, pitch2 * yaw1)
        val someOtherVector = Vector(pitch1 * yaw3, yaw4, pitch2 * yaw3)
        val scaled = someVector.cross(someOtherVector) * -1.0

        val x = someVector.x * forwards + someOtherVector.x * up + scaled.x * left
        val y = someVector.y * forwards + someOtherVector.y * up + scaled.y * left
        val z = someVector.z * forwards + someOtherVector.z * up + scaled.z * left
        return Vector(location.x + x, location.y + y, location.z + z)
    }

    override fun rotation(player: Player): Vector2d = Vector2d.ZERO

    companion object {

        fun parse(reader: StringReader): LocalCoordinates {
            val position = reader.cursor
            val left = reader.readDouble(position)
            if (!reader.canRead() || reader.peek() != ' ') {
                reader.cursor = position
                throw ERROR_NOT_COMPLETE.createWithContext(reader)
            }
            reader.skip()
            val up = reader.readDouble(position)
            if (!reader.canRead() || reader.peek() != ' ') {
                reader.cursor = position
                throw ERROR_NOT_COMPLETE.createWithContext(reader)
            }
            reader.skip()
            val forwards = reader.readDouble(position)
            return LocalCoordinates(left, up, forwards)
        }
    }
}

private fun StringReader.readDouble(position: Int): Double {
    if (!canRead()) throw ERROR_EXPECTED_DOUBLE.createWithContext(this)
    if (peek() != '^') {
        cursor = position
        throw ERROR_MIXED_TYPE.createWithContext(this)
    }
    skip()
    return if (canRead() && peek() != ' ') readDouble() else 0.0
}
