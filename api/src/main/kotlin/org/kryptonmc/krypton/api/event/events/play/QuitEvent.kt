package org.kryptonmc.krypton.api.event.events.play

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.Event

/**
 * Called when the connection to a player in the PLAY state is lost.
 *
 * @param player the player who quit
 * @author Callum Seabrook
 */
data class QuitEvent(val player: Player) : Event