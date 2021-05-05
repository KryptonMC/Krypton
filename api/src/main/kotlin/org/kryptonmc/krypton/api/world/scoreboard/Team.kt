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
import org.kryptonmc.krypton.api.entity.entities.Player

/**
 * Represents a [Scoreboard] team.
 *
 * Teams are groups of entities that have a name, prefix,
 * suffix, colour, and a specific set of rules they follow.
 */
data class Team(
    val name: String,
    val displayName: Component,
    val allowFriendlyFire: Boolean,
    val areInvisibleMembersVisible: Boolean,
    val options: Map<Option, OptionApplication>,
    val color: TeamColor,
    val prefix: Component,
    val suffix: Component,
    val members: List<Player>
)

/**
 * Represents options for teams.
 */
enum class Option {

    /**
     * Whether or not nametags are visible
     */
    NAMETAG_VISIBILITY,

    /**
     * Whether or not death messages are visible
     */
    DEATH_MESSAGE_VISIBILITY,

    /**
     * Whether or not players will collide with each other
     */
    COLLISION_RULE
}

/**
 * Represents how an [Option] is applied.
 */
enum class OptionApplication {

    /**
     * Always apply this option.
     */
    ALWAYS,

    /**
     * Never apply this option.
     */
    NEVER,

    /**
     * Only apply this option to other teams.
     */
    OTHER_TEAMS,

    /**
     * Only apply this option to our own team.
     */
    OWN_TEAM
}

/**
 * Represents the colour of a team. This is used because team colours are
 * legacy colours, and because they have special IDs
 */
enum class TeamColor {

    /**
     * Colours
     */
    BLACK,
    DARK_BLUE,
    DARK_GREEN,
    DARK_CYAN,
    DARK_RED,
    PURPLE,
    GOLD,
    GRAY,
    DARK_GRAY,
    BLUE,
    BRIGHT_GREEN,
    CYAN,
    RED,
    PINK,
    YELLOW,
    WHITE,

    /**
     * Decoration
     */
    OBFUSCATED,
    BOLD,
    STRIKETHROUGH,
    UNDERLINED,
    ITALIC,

    /**
     * Other
     */
    RESET
}
