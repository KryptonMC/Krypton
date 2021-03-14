package org.kryptonmc.krypton.api.event.events.login

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.CancellableEvent

/**
 * Called when a player logs in and a player object has been
 * constructed for them (after the state is switched to PLAY)
 *
 * @author Callum Seabrook
 */
data class JoinEvent(val player: Player) : CancellableEvent()