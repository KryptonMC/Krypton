/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

/**
 * Represents an area effect cloud, such as that of the lingering effect
 * that appears when a lingering potion is thrown.
 */
public interface AreaEffectCloud : Entity {

    /**
     * The amount of ticks this cloud has lived for.
     */
    public val age: Int

    /**
     * The duration, in ticks, that this area effect cloud will exist for.
     */
    public val duration: Int

    /**
     * The radius of this area effect cloud.
     */
    public val radius: Float

    /**
     * The color of this area effect cloud.
     */
    public val color: Int
}
