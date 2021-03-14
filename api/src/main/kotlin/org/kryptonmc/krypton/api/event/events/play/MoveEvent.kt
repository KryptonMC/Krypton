package org.kryptonmc.krypton.api.event.events.play

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.CancellableEvent
import org.kryptonmc.krypton.api.world.Location

/**
 * Called when a player moves.
 *
 * @author Callum Seabrook
 */
class MoveEvent(
    val player: Player,
    val oldLocation: Location,
    val newLocation: Location
) : CancellableEvent()