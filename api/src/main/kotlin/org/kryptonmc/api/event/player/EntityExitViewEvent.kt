package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.event.type.PlayerEvent

/**
 * Called when an entity goes out of view of a player.
 */
public interface EntityExitViewEvent : PlayerEvent {

    /**
     * The entity that went out of view.
     */
    public val entity: Entity
}
