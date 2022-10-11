/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

/**
 * The parts of the skin that are shown on a player.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface SkinParts {

    /**
     * If the cape is shown.
     */
    @get:JvmName("hasCape")
    public val hasCape: Boolean

    /**
     * If the jacket is shown.
     */
    @get:JvmName("hasJacket")
    public val hasJacket: Boolean

    /**
     * If the left sleeve is shown.
     */
    @get:JvmName("hasLeftSleeve")
    public val hasLeftSleeve: Boolean

    /**
     * If the right sleeve is shown.
     */
    @get:JvmName("hasRightSleeve")
    public val hasRightSleeve: Boolean

    /**
     * If the left half of the pants is shown.
     */
    @get:JvmName("hasLeftPants")
    public val hasLeftPants: Boolean

    /**
     * If the right half of the pants is shown.
     */
    @get:JvmName("hasRightPants")
    public val hasRightPants: Boolean

    /**
     * If the hat is shown.
     */
    @get:JvmName("hasHat")
    public val hasHat: Boolean
}
