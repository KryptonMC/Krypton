/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.text.format.NamedTextColor

/**
 * The slot that a scoreboard may be displayed in.
 *
 * @param teamColor the team colour associated with this slot, or null if this
 * slot is not associated with a team colour.
 */
public enum class DisplaySlot(public val teamColor: NamedTextColor?) {

    LIST(null),
    SIDEBAR(null),
    BELOW_NAME(null),
    SIDEBAR_TEAM_BLACK(NamedTextColor.BLACK),
    SIDEBAR_TEAM_DARK_BLUE(NamedTextColor.DARK_BLUE),
    SIDEBAR_TEAM_DARK_GREEN(NamedTextColor.DARK_GREEN),
    SIDEBAR_TEAM_DARK_AQUA(NamedTextColor.DARK_AQUA),
    SIDEBAR_TEAM_DARK_RED(NamedTextColor.DARK_RED),
    SIDEBAR_TEAM_DARK_PURPLE(NamedTextColor.DARK_PURPLE),
    SIDEBAR_TEAM_GOLD(NamedTextColor.GOLD),
    SIDEBAR_TEAM_GRAY(NamedTextColor.GRAY),
    SIDEBAR_TEAM_DARK_GRAY(NamedTextColor.DARK_GRAY),
    SIDEBAR_TEAM_BLUE(NamedTextColor.BLUE),
    SIDEBAR_TEAM_GREEN(NamedTextColor.GREEN),
    SIDEBAR_TEAM_AQUA(NamedTextColor.AQUA),
    SIDEBAR_TEAM_RED(NamedTextColor.RED),
    SIDEBAR_TEAM_LIGHT_PURPLE(NamedTextColor.LIGHT_PURPLE),
    SIDEBAR_TEAM_YELLOW(NamedTextColor.YELLOW),
    SIDEBAR_TEAM_WHITE(NamedTextColor.WHITE);

    public companion object {

        private val BY_COLOR = DisplaySlot.values().filter { it.teamColor != null }.associateBy { it.teamColor }

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
