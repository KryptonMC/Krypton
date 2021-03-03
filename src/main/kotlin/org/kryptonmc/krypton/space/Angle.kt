package org.kryptonmc.krypton.space

/**
 * Represents a protocol angle, measured in 256ths of a full turn
 */
inline class Angle(val value: UByte) : Comparable<Angle> {

    override operator fun compareTo(other: Angle) = if (value == other.value) 0 else if (value < other.value) -1 else 1

    fun toDegrees() = ((value.toFloat() / 256.0f) * 360.0f)

    companion object {

        val ZERO = Angle(0u)
    }
}

fun Float.toAngle() = Angle(((this / 360.0f) * 256.0f).toInt().toUByte())