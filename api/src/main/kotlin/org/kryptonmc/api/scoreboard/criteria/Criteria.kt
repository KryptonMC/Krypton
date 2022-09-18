/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard.criteria

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in criteria for scoreboards.
 */
@Catalogue(Criterion::class)
public object Criteria {

    // @formatter:off
    @JvmField
    public val DUMMY: Criterion = get("dummy")
    @JvmField
    public val TRIGGER: Criterion = get("trigger")
    @JvmField
    public val DEATH_COUNT: Criterion = get("death_count")
    @JvmField
    public val PLAYER_KILL_COUNT: Criterion = get("player_kill_count")
    @JvmField
    public val TOTAL_KILL_COUNT: Criterion = get("kill_count")
    @JvmField
    public val HEALTH: Criterion = get("health")
    @JvmField
    public val FOOD: Criterion = get("food")
    @JvmField
    public val AIR: Criterion = get("air")
    @JvmField
    public val ARMOR: Criterion = get("armor")
    @JvmField
    public val EXPERIENCE: Criterion = get("experience")
    @JvmField
    public val LEVEL: Criterion = get("level")
    @JvmField
    public val TEAM_KILL_BLACK: Criterion = get("team_kill_black")
    @JvmField
    public val TEAM_KILL_DARK_BLUE: Criterion = get("team_kill_dark_blue")
    @JvmField
    public val TEAM_KILL_DARK_GREEN: Criterion = get("team_kill_dark_green")
    @JvmField
    public val TEAM_KILL_DARK_AQUA: Criterion = get("team_kill_dark_aqua")
    @JvmField
    public val TEAM_KILL_DARK_RED: Criterion = get("team_kill_dark_red")
    @JvmField
    public val TEAM_KILL_DARK_PURPLE: Criterion = get("team_kill_dark_purple")
    @JvmField
    public val TEAM_KILL_GOLD: Criterion = get("team_kill_gold")
    @JvmField
    public val TEAM_KILL_GRAY: Criterion = get("team_kill_gray")
    @JvmField
    public val TEAM_KILL_DARK_GRAY: Criterion = get("team_kill_dark_gray")
    @JvmField
    public val TEAM_KILL_BLUE: Criterion = get("team_kill_blue")
    @JvmField
    public val TEAM_KILL_GREEN: Criterion = get("team_kill_green")
    @JvmField
    public val TEAM_KILL_AQUA: Criterion = get("team_kill_aqua")
    @JvmField
    public val TEAM_KILL_RED: Criterion = get("team_kill_red")
    @JvmField
    public val TEAM_KILL_LIGHT_PURPLE: Criterion = get("team_kill_light_purple")
    @JvmField
    public val TEAM_KILL_YELLOW: Criterion = get("team_kill_yellow")
    @JvmField
    public val TEAM_KILL_WHITE: Criterion = get("team_kill_white")
    @JvmField
    public val KILLED_BY_TEAM_BLACK: Criterion = get("killed_by_team_black")
    @JvmField
    public val KILLED_BY_TEAM_DARK_BLUE: Criterion = get("killed_by_team_dark_blue")
    @JvmField
    public val KILLED_BY_TEAM_DARK_GREEN: Criterion = get("killed_by_team_dark_green")
    @JvmField
    public val KILLED_BY_TEAM_DARK_AQUA: Criterion = get("killed_by_team_dark_aqua")
    @JvmField
    public val KILLED_BY_TEAM_DARK_RED: Criterion = get("killed_by_team_dark_red")
    @JvmField
    public val KILLED_BY_TEAM_DARK_PURPLE: Criterion = get("killed_by_team_dark_purple")
    @JvmField
    public val KILLED_BY_TEAM_GOLD: Criterion = get("killed_by_team_gold")
    @JvmField
    public val KILLED_BY_TEAM_GRAY: Criterion = get("killed_by_team_gray")
    @JvmField
    public val KILLED_BY_TEAM_DARK_GRAY: Criterion = get("killed_by_team_dark_gray")
    @JvmField
    public val KILLED_BY_TEAM_BLUE: Criterion = get("killed_by_team_blue")
    @JvmField
    public val KILLED_BY_TEAM_GREEN: Criterion = get("killed_by_team_green")
    @JvmField
    public val KILLED_BY_TEAM_AQUA: Criterion = get("killed_by_team_aqua")
    @JvmField
    public val KILLED_BY_TEAM_RED: Criterion = get("killed_by_team_red")
    @JvmField
    public val KILLED_BY_TEAM_LIGHT_PURPLE: Criterion = get("killed_by_team_light_purple")
    @JvmField
    public val KILLED_BY_TEAM_YELLOW: Criterion = get("killed_by_team_yellow")
    @JvmField
    public val KILLED_BY_TEAM_WHITE: Criterion = get("killed_by_team_white")

    // @formatter:on
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <T : Criterion> get(name: String): T = Registries.CRITERIA.get(Key.key("krypton", name))!! as T
}
