/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.scoreboard

import net.kyori.adventure.text.Component
import org.kryptonmc.api.util.StringSerializable
import org.kryptonmc.api.world.scoreboard.criteria.Criterion

/**
 * Represents an objective for a [Scoreboard].
 *
 * @param name the name of the objective
 * @param displayName the display name of the objective (this is what gets displayed on the scoreboard)
 * @param criterion optional criteria for the scoreboard
 * @param renderType how this objective is rendered to the client
 */
data class Objective @JvmOverloads constructor(
    val name: String,
    val displayName: Component,
    val criterion: Criterion? = null,
    val renderType: RenderType = RenderType.INTEGER
)

/**
 * Controls how an [Objective] is rendered to the client.
 */
enum class RenderType(override val serialized: String) : StringSerializable {

    /**
     * Display an integer value.
     */
    INTEGER("integer"),

    /**
     * Display a number of hearts corresponding to the value.
     */
    HEARTS("hearts")
}
