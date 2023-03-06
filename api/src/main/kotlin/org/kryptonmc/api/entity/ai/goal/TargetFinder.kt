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
