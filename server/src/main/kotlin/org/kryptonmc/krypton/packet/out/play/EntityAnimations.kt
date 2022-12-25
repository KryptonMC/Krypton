package org.kryptonmc.krypton.packet.out.play

/**
 * An integer pseudo-enum holding all the entity animations.
 *
 * This is not an enum to avoid having to lookup from enum values, and also, because we can accept any value, the client should just ignore it.
 * Also, depending on the ordinal of an enum is not recommended.
 */
object EntityAnimations {

    const val SWING_MAIN_ARM: Int = 0
    const val TAKE_DAMAGE: Int = 1
    const val LEAVE_BED: Int = 2
    const val SWING_OFFHAND: Int = 3
    const val CRITICAL_EFFECT: Int = 4
    const val MAGIC_CRITICAL_EFFECT: Int = 5
}
