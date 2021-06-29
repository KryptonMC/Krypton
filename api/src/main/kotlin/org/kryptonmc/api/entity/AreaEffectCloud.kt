/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import java.awt.Color

/**
 * Represents an area effect cloud, such as that of the lingering effect
 * that appears when a lingering potion is thrown.
 */
interface AreaEffectCloud : Entity {

    /**
     * The duration, in ticks, that this area effect cloud will exist for.
     */
    val duration: Int

    /**
     * The radius of this area effect cloud.
     */
    val radius: Float

    /**
     * The color of this area effect cloud.
     */
    val color: Color
}
