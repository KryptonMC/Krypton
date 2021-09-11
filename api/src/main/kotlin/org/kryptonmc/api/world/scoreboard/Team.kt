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
import org.kryptonmc.api.entity.player.Player

/**
 * Represents a [Scoreboard] team.
 *
 * Teams are groups of entities that have a name, prefix, suffix, colour,
 * and a specific set of rules they follow.
 *
 * @param name the name of this team
 * @param displayName the display name of this team
 * @param allowFriendlyFire if this team allows friendly fire
 * @param canSeeInvisibleMembers if team members can see other team members
 * that are invisible
 * @param options the options for this team
 * @param color the color of this team
 * @param prefix this team's prefix
 * @param suffix this team's suffix
 * @param members this team's members
 */
@JvmRecord
public data class Team(
    public val name: String,
    public val displayName: Component,
    @get:JvmName("allowFriendlyFire") public val allowFriendlyFire: Boolean,
    @get:JvmName("areInvisibleMembersVisible") public val canSeeInvisibleMembers: Boolean,
    public val options: Map<Option, OptionApplication>,
    public val color: TeamColor,
    public val prefix: Component,
    public val suffix: Component,
    public val members: List<Player>
)

/**
 * Represents options for teams.
 */
public enum class Option {

    /**
     * Whether or not name tags are visible.
     */
    NAMETAG_VISIBILITY,

    /**
     * Whether or not death messages are visible.
     */
    DEATH_MESSAGE_VISIBILITY,

    /**
     * Whether or not players will collide with each other.
     */
    COLLISION_RULE
}

/**
 * Represents how an [Option] is applied.
 */
public enum class OptionApplication {

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
 * legacy colours, and because they have special IDs.
 */
public enum class TeamColor {

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
