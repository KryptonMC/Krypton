package org.kryptonmc.api.entity.ai.goal

import org.kryptonmc.api.entity.Entity

/**
 * A function that finds a target for an entity.
 */
public fun interface TargetFinder {

    /**
     * Finds a target for the entity.
     *
     * This must always return null if no target was found.
     *
     * @return the target, or null if no target was found
     */
    public fun findTarget(): Entity?
}
