/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.text.Component

/**
 * The score of a member of a team for a specific objective.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Score {

    /**
     * The name of the score.
     */
    @get:JvmName("name")
    public val name: Component

    /**
     * The objective this score is registered to, or null if this score is not
     * currently registered to an objective.
     */
    @get:JvmName("objective")
    public val objective: Objective?

    /**
     * The underlying value of this score.
     */
    @get:JvmName("score")
    public var score: Int

    /**
     * If this score is locked, meaning it cannot be changed.
     */
    public var isLocked: Boolean
}
