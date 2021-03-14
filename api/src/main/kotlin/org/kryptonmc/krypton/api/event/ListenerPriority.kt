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

    const val MAXIMUM = 64
    const val HIGH = 32
    const val MEDIUM = 0
    const val LOW = -32
    const val NONE = -64
}