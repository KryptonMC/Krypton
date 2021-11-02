/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla scoreboard display slots.
 */
@Catalogue(DisplaySlot::class)
public object DisplaySlots {

    // @formatter:off
    @JvmField public val LIST: DisplaySlot = register("list")
    @JvmField public val SIDEBAR: DisplaySlot = register("sidebar")
    @JvmField public val BELOW_NAME: DisplaySlot = register("below_name")
    @JvmField public val SIDEBAR_TEAM_BLACK: DisplaySlot = register("sidebar_team_black", NamedTextColor.BLACK)
    @JvmField public val SIDEBAR_TEAM_DARK_BLUE: DisplaySlot = register("sidebar_team_dark_blue", NamedTextColor.DARK_BLUE)
    @JvmField public val SIDEBAR_TEAM_DARK_GREEN: DisplaySlot = register("sidebar_team_dark_green", NamedTextColor.DARK_GREEN)
    @JvmField public val SIDEBAR_TEAM_DARK_AQUA: DisplaySlot = register("sidebar_team_dark_aqua", NamedTextColor.DARK_AQUA)
    @JvmField public val SIDEBAR_TEAM_DARK_RED: DisplaySlot = register("sidebar_team_dark_red", NamedTextColor.DARK_RED)
    @JvmField public val SIDEBAR_TEAM_DARK_PURPLE: DisplaySlot = register("sidebar_team_dark_purple", NamedTextColor.DARK_PURPLE)
    @JvmField public val SIDEBAR_TEAM_GOLD: DisplaySlot = register("sidebar_team_gold", NamedTextColor.GOLD)
    @JvmField public val SIDEBAR_TEAM_GRAY: DisplaySlot = register("sidebar_team_gray", NamedTextColor.GRAY)
    @JvmField public val SIDEBAR_TEAM_DARK_GRAY: DisplaySlot = register("sidebar_team_dark_gray", NamedTextColor.DARK_GRAY)
    @JvmField public val SIDEBAR_TEAM_BLUE: DisplaySlot = register("sidebar_team_blue", NamedTextColor.BLUE)
    @JvmField public val SIDEBAR_TEAM_GREEN: DisplaySlot = register("sidebar_team_green", NamedTextColor.GREEN)
    @JvmField public val SIDEBAR_TEAM_AQUA: DisplaySlot = register("sidebar_team_aqua", NamedTextColor.AQUA)
    @JvmField public val SIDEBAR_TEAM_RED: DisplaySlot = register("sidebar_team_red", NamedTextColor.RED)
    @JvmField public val SIDEBAR_TEAM_LIGHT_PURPLE: DisplaySlot = register("sidebar_team_light_purple", NamedTextColor.LIGHT_PURPLE)
    @JvmField public val SIDEBAR_TEAM_YELLOW: DisplaySlot = register("sidebar_team_yellow", NamedTextColor.YELLOW)
    @JvmField public val SIDEBAR_TEAM_WHITE: DisplaySlot = register("sidebar_team_white", NamedTextColor.WHITE)

    // @formatter:on
    @JvmStatic
    private fun register(name: String, color: NamedTextColor? = null): DisplaySlot {
        val key = Key.key("krypton", name)
        return Registries.DISPLAY_SLOTS.register(key, DisplaySlot.of(key, color))
    }
}
