/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api.world

/**
 * The area around a world that prevents players from venturing too far into the wilderness.
 */
interface WorldBorder {

    /**
     * The world that this border is bound to
     */
    val world: World

    /**
     * The size, or diameter, of the world border
     */
    val size: Double

    /**
     * The center position of the world border
     *
     * Note: This position's Y will always be -1
     */
    val center: Location

    /**
     * The damage multiplier for this border
     */
    val damageMultiplier: Double
}
