/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.ai.goal

import org.kryptonmc.api.entity.Entity

/**
 * A finder for targets for an entity.
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

    /**
     * If this finder is valid and can be used to find targets.
     *
     * @return true if this finder can be used
     */
    public fun canUse(): Boolean {
        return true
    }

    /**
     * Called when this finder starts being used to find targets.
     *
     * This allows the finder to set up any state it may need before it is
     * asked to find targets.
     */
    public fun onStartUsing() {
        // Do nothing by default
    }

    /**
     * If this finder should be removed from the finders that can be used to
     * find targets, indicating it is no longer valid and should not be used.
     *
     * @return true if this finder should be removed
     */
    public fun shouldRemove(): Boolean {
        return false
    }

    /**
     * Called when this finder stops being used to find targets.
     *
     * This allows the finder to clean up any state it may have.
     */
    public fun onRemove() {
        // Do nothing by default
    }
}
