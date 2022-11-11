/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

/**
 * The area around a world that prevents players from venturing too far into
 * the wilderness.
 */
public interface WorldBorder {

    /**
     * The size, or diameter, of the world border.
     */
    public val size: Double

    /**
     * The X coordinate of the center position of this world border.
     */
    public val centerX: Double

    /**
     * The Z coordinate of the center position of this world border.
     */
    public val centerZ: Double

    /**
     * The damage multiplier for this border.
     */
    public val damageMultiplier: Double
}
