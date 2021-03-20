package org.kryptonmc.krypton.api.event

/**
 * Represents priorities of a listener.
 *
 * This is an object with const values as opposed to an enum
 * because BungeeCord.
 *
 * @author Callum Seabrook
 */
object ListenerPriority {

    const val MAXIMUM: Byte = 64
    const val HIGH: Byte = 32
    const val MEDIUM: Byte = 0
    const val LOW: Byte = -32
    const val NONE: Byte = -64
}