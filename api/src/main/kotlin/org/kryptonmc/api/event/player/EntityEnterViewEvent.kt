package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.event.type.PlayerEvent

/**
 * Called when an entity comes in to view of a player.
 */
public interface EntityEnterViewEvent : PlayerEvent {

    /**
     * The entity that came in to view.
     */
    public val entity: Entity
}
