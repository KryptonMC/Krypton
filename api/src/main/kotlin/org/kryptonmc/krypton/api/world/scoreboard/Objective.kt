/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api.world.scoreboard

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.world.scoreboard.criteria.Criteria

/**
 * Represents an objective for a [Scoreboard].
 *
 * @param name the name of the objective
 * @param displayName the display name of the objective (this is what gets displayed on the scoreboard)
 * @param criteria optional criteria for the scoreboard
 * @param renderType how this objective is rendered to the client.
 */
data class Objective @JvmOverloads constructor(
    val name: String,
    val displayName: Component,
    val criteria: Criteria? = null,
    val renderType: RenderType = RenderType.INTEGER
)

/**
 * Controls how an [Objective] is rendered to the client
 */
enum class RenderType {

    /**
     * Display an integer value
     */
    INTEGER,

    /**
     * Display a number of hearts corresponding to the value
     */
    HEARTS
}
