package org.kryptonmc.krypton.api.event.events.ticking

import org.kryptonmc.krypton.api.event.Event

/**
 * Called when a tick begins.
 *
 * @param tickNumber the tick number
 * @author Callum Seabrook
 */
data class TickStartEvent(val tickNumber: Int) : Event