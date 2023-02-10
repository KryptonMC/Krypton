/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

import org.kryptonmc.internal.annotations.ImmutableType

/**
 * The parts of the skin that are shown on a player.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface SkinParts {

    /**
     * Whether the cape is shown.
     *
     * @return true if the cape is shown
     */
    public fun hasCape(): Boolean

    /**
     * Whether the jacket is shown.
     *
     * @return true if the jacket is shown
     */
    public fun hasJacket(): Boolean

    /**
     * Whether the left sleeve is shown.
     *
     * @return true if the left sleeve is shown
     */
    public fun hasLeftSleeve(): Boolean

    /**
     * Whether the right sleeve is shown.
     *
     * @return true if the right sleeve is shown
     */
    public fun hasRightSleeve(): Boolean

    /**
     * Whether the left half of the pants is shown.
     *
     * @return true if the left pants are shown
     */
    public fun hasLeftPants(): Boolean

    /**
     * Whether the right half of the pants is shown.
     *
     * @return true if the right pants are shown
     */
    public fun hasRightPants(): Boolean

    /**
     * Whether the hat is shown.
     *
     * @return true if the hat is shown
     */
    public fun hasHat(): Boolean
}
