/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.util.StringSerializable

/**
 * The slot that a scoreboard may be displayed in.
 *
 * @param teamColor the team colour associated with this slot, or null if this
 * slot is not associated with a team colour.
 */
public enum class DisplaySlot(
    @get:JvmName("serialized") override val serialized: String,
    public val teamColor: NamedTextColor?
) : StringSerializable {

    LIST("list", null),
    SIDEBAR("sidebar", null),
    BELOW_NAME("below_name", null),
    SIDEBAR_TEAM_BLACK("sidebar_team_black", NamedTextColor.BLACK),
    SIDEBAR_TEAM_DARK_BLUE("sidebar_team_dark_blue", NamedTextColor.DARK_BLUE),
    SIDEBAR_TEAM_DARK_GREEN("sidebar_team_dark_green", NamedTextColor.DARK_GREEN),
    SIDEBAR_TEAM_DARK_AQUA("sidebar_team_dark_aqua", NamedTextColor.DARK_AQUA),
    SIDEBAR_TEAM_DARK_RED("sidebar_team_dark_red", NamedTextColor.DARK_RED),
    SIDEBAR_TEAM_DARK_PURPLE("sidebar_team_dark_purple", NamedTextColor.DARK_PURPLE),
    SIDEBAR_TEAM_GOLD("sidebar_team_gold", NamedTextColor.GOLD),
    SIDEBAR_TEAM_GRAY("sidebar_team_gray", NamedTextColor.GRAY),
    SIDEBAR_TEAM_DARK_GRAY("sidebar_team_dark_gray", NamedTextColor.DARK_GRAY),
    SIDEBAR_TEAM_BLUE("sidebar_team_blue", NamedTextColor.BLUE),
    SIDEBAR_TEAM_GREEN("sidebar_team_green", NamedTextColor.GREEN),
    SIDEBAR_TEAM_AQUA("sidebar_team_aqua", NamedTextColor.AQUA),
    SIDEBAR_TEAM_RED("sidebar_team_red", NamedTextColor.RED),
    SIDEBAR_TEAM_LIGHT_PURPLE("sidebar_team_light_purple", NamedTextColor.LIGHT_PURPLE),
    SIDEBAR_TEAM_YELLOW("sidebar_team_yellow", NamedTextColor.YELLOW),
    SIDEBAR_TEAM_WHITE("sidebar_team_white", NamedTextColor.WHITE);

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }
        private val BY_COLOR = VALUES.filter { it.teamColor != null }.associateBy { it.teamColor }

        /**
         * Gets the display slot with the given [name], or returns null if
         * there is no display slot with the given [name].
         *
         * @param name the name
         * @return the display slot with the name, or null if not present
         */
        @JvmStatic
        public fun fromName(name: String): DisplaySlot? = BY_NAME[name]

        /**
         * Gets the display slot with the given [id], or returns null if there
         * is no display slot with the given [id].
         *
         * @param id the ID
         * @return the display slot with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): DisplaySlot? = VALUES.getOrNull(id)

        /**
         * Gets the display slot with the given [color].
         *
         * @param color the colour
         * @return the display slot with the colour
         */
        @JvmStatic
        public fun fromColor(color: NamedTextColor): DisplaySlot = BY_COLOR.getValue(color)
    }
}
