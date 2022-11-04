/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.projectile

import org.kryptonmc.api.entity.Entity

/**
 * A fishing hook on the end of a fishing rod.
 */
public interface FishingHook : Projectile {

    /**
     * The entity that is currently hooked by this hook, or null if this hook
     * is not hooking an entity.
     */
    public val hooked: Entity?

    /**
     * If the hooked entity is biting the hook, meaning it can be caught.
     */
    public val isBiting: Boolean

    /**
     * The current state of this hook.
     */
    public val state: State

    /**
     * The current state of a fishing hook.
     */
    public enum class State {

        /**
         * The rod has been cast and the hook is flying in the air across the
         * water.
         */
        FLYING,

        /**
         * The hook has hooked an entity.
         */
        HOOKED,

        /**
         * The hook is bobbing up and down waiting for an entity.
         */
        BOBBING;
    }
}
