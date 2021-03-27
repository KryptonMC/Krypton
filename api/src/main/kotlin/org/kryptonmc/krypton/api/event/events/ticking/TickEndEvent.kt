package org.kryptonmc.krypton.api.event.events.ticking

import org.kryptonmc.krypton.api.event.Event

/**
 * Called when a tick ends.
 *
 * @param tickNumber the tick number
 * @param tickDuration the duration of the tick
 * @param timeEnd the time this tick ended in nanoseconds
 * @author Callum Seabrook
 */
data class TickEndEvent(
    val tickNumber: Int,
    val tickDuration: Long,
    val timeEnd: Long
) : Event