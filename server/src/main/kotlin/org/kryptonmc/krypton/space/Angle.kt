package org.kryptonmc.krypton.space

/**
 * Represents a protocol angle, measured in 256ths of a full turn
 *
 * @author Callum Seabrook
 */
inline class Angle(val value: UByte) : Comparable<Angle> {

    override fun compareTo(other: Angle) = if (value == other.value) 0 else if (value < other.value) -1 else 1

    fun toDegrees() = ((value.toFloat() / 256.0f) * 360.0f)

    companion object {

        val ZERO = Angle(0u)
    }
}

/**
 * Convert a float value (in degrees) to a protocol [Angle] by dividing its value by 360 and multiplying
 * the result by 256, then rounding to an integer
 *
 * @author Callum Seabrook
 */
fun Float.toAngle() = Angle(((this / 360.0f) * 256.0f).toInt().toUByte())