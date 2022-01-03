/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import org.spongepowered.math.vector.Vector2d

/**
 * The area around a world that prevents players from venturing too far into
 * the wilderness.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface WorldBorder {

    /**
     * The size, or diameter, of the world border.
     */
    @get:JvmName("size")
    public val size: Double

    /**
     * The center position of the world border.
     *
     * Note: This position's Y will always be -1.
     */
    @get:JvmName("center")
    public val center: Vector2d

    /**
     * The damage multiplier for this border.
     */
    @get:JvmName("damageMultiplier")
    public val damageMultiplier: Double
}
