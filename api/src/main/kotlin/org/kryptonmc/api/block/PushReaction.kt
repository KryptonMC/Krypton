/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

/**
 * The reaction something will have when it is pushed.
 */
public enum class PushReaction {

    /**
     * The block will be moved by the piston as normal, and will not change.
     */
    NORMAL,

    /**
     * The block being pushed is not strong enough for this cruel world, and
     * will be destroyed when pushed.
     */
    DESTROY,

    /**
     * The block being pushed is too strong for a mere piston to move it, and
     * it blocks all attempts made by pistons to push it.
     */
    BLOCK,

    /**
     * The block will ignore any attempts made by the piston to interact with
     * it.
     */
    IGNORE,

    /**
     * The block can be pushed as normal by pistons, but no piston wields the
     * strength required to pull it back towards itself.
     */
    PUSH_ONLY
}
