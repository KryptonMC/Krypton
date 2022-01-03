/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.spongepowered.math.vector.Vector3i

/**
 * A turtle.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Turtle : Animal {

    /**
     * If this turtle currently has an egg.
     */
    @get:JvmName("hasEgg")
    public var hasEgg: Boolean

    /**
     * If this turtle is currently laying an egg.
     */
    public var isLayingEgg: Boolean

    /**
     * If this turtle has called it a day and is heading back [home].
     */
    public var isGoingHome: Boolean

    /**
     * If this turtle is currently travelling to its [destination].
     */
    public var isTravelling: Boolean

    /**
     * The location of this turtle's home.
     */
    @get:JvmName("home")
    public var home: Vector3i

    /**
     * The current destination of this turtle.
     */
    @get:JvmName("destination")
    public var destination: Vector3i
}
