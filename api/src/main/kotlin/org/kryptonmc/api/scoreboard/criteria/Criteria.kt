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
@Catalogue(KeyedCriterion::class)
public object Criteria {

    // @formatter:off
    @JvmField
    public val DUMMY: KeyedCriterion = get("dummy")
    @JvmField
    public val TRIGGER: KeyedCriterion = get("trigger")
    @JvmField
    public val DEATH_COUNT: KeyedCriterion = get("death_count")
    @JvmField
    public val PLAYER_KILL_COUNT: KeyedCriterion = get("player_kill_count")
    @JvmField
    public val TOTAL_KILL_COUNT: KeyedCriterion = get("total_kill_count")
    @JvmField
    public val HEALTH: KeyedCriterion = get("health")
    @JvmField
    public val FOOD: KeyedCriterion = get("food")
    @JvmField
    public val AIR: KeyedCriterion = get("air")
    @JvmField
    public val ARMOR: KeyedCriterion = get("armor")
    @JvmField
    public val EXPERIENCE: KeyedCriterion = get("experience")
    @JvmField
    public val LEVEL: KeyedCriterion = get("level")
    @JvmField
    public val TEAM_KILL_BLACK: KeyedCriterion = get("team_kill_black")
    @JvmField
    public val TEAM_KILL_DARK_BLUE: KeyedCriterion = get("team_kill_dark_blue")
    @JvmField
    public val TEAM_KILL_DARK_GREEN: KeyedCriterion = get("team_kill_dark_green")
    @JvmField
    public val TEAM_KILL_DARK_AQUA: KeyedCriterion = get("team_kill_dark_aqua")
    @JvmField
    public val TEAM_KILL_DARK_RED: KeyedCriterion = get("team_kill_dark_red")
    @JvmField
    public val TEAM_KILL_DARK_PURPLE: KeyedCriterion = get("team_kill_dark_purple")
    @JvmField
    public val TEAM_KILL_GOLD: KeyedCriterion = get("team_kill_gold")
    @JvmField
    public val TEAM_KILL_GRAY: KeyedCriterion = get("team_kill_gray")
    @JvmField
    public val TEAM_KILL_DARK_GRAY: KeyedCriterion = get("team_kill_dark_gray")
    @JvmField
    public val TEAM_KILL_BLUE: KeyedCriterion = get("team_kill_blue")
    @JvmField
    public val TEAM_KILL_GREEN: KeyedCriterion = get("team_kill_green")
    @JvmField
    public val TEAM_KILL_AQUA: KeyedCriterion = get("team_kill_aqua")
    @JvmField
    public val TEAM_KILL_RED: KeyedCriterion = get("team_kill_red")
    @JvmField
    public val TEAM_KILL_LIGHT_PURPLE: KeyedCriterion = get("team_kill_light_purple")
    @JvmField
    public val TEAM_KILL_YELLOW: KeyedCriterion = get("team_kill_yellow")
    @JvmField
    public val TEAM_KILL_WHITE: KeyedCriterion = get("team_kill_white")
    @JvmField
    public val KILLED_BY_TEAM_BLACK: KeyedCriterion = get("killed_by_team_black")
    @JvmField
    public val KILLED_BY_TEAM_DARK_BLUE: KeyedCriterion = get("killed_by_team_dark_blue")
    @JvmField
    public val KILLED_BY_TEAM_DARK_GREEN: KeyedCriterion = get("killed_by_team_dark_green")
    @JvmField
    public val KILLED_BY_TEAM_DARK_AQUA: KeyedCriterion = get("killed_by_team_dark_aqua")
    @JvmField
    public val KILLED_BY_TEAM_DARK_RED: KeyedCriterion = get("killed_by_team_dark_red")
    @JvmField
    public val KILLED_BY_TEAM_DARK_PURPLE: KeyedCriterion = get("killed_by_team_dark_purple")
    @JvmField
    public val KILLED_BY_TEAM_GOLD: KeyedCriterion = get("killed_by_team_gold")
    @JvmField
    public val KILLED_BY_TEAM_GRAY: KeyedCriterion = get("killed_by_team_gray")
    @JvmField
    public val KILLED_BY_TEAM_DARK_GRAY: KeyedCriterion = get("killed_by_team_dark_gray")
    @JvmField
    public val KILLED_BY_TEAM_BLUE: KeyedCriterion = get("killed_by_team_blue")
    @JvmField
    public val KILLED_BY_TEAM_GREEN: KeyedCriterion = get("killed_by_team_green")
    @JvmField
    public val KILLED_BY_TEAM_AQUA: KeyedCriterion = get("killed_by_team_aqua")
    @JvmField
    public val KILLED_BY_TEAM_RED: KeyedCriterion = get("killed_by_team_red")
    @JvmField
    public val KILLED_BY_TEAM_LIGHT_PURPLE: KeyedCriterion = get("killed_by_team_light_purple")
    @JvmField
    public val KILLED_BY_TEAM_YELLOW: KeyedCriterion = get("killed_by_team_yellow")
    @JvmField
    public val KILLED_BY_TEAM_WHITE: KeyedCriterion = get("killed_by_team_white")

    // @formatter:on
    @JvmStatic
    private fun get(name: String): KeyedCriterion = Registries.CRITERIA.get(Key.key("krypton", name))!!
}
