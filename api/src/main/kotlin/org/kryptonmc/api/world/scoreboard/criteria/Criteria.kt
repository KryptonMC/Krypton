/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.scoreboard.criteria

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

/**
 * All of the built-in criteria for scoreboards.
 */
object Criteria {

    // @formatter:off
    @JvmField val DUMMY = get("dummy")
    @JvmField val TRIGGER = get("trigger")
    @JvmField val DEATH_COUNT = get("death_count")
    @JvmField val PLAYER_KILL_COUNT = get("player_kill_count")
    @JvmField val TOTAL_KILL_COUNT = get("kill_count")
    @JvmField val HEALTH = get("health")
    @JvmField val FOOD = get("food")
    @JvmField val AIR = get("air")
    @JvmField val ARMOR = get("armor")
    @JvmField val EXPERIENCE = get("experience")
    @JvmField val LEVEL = get("level")
    @JvmField val TEAM_KILL = get("team_kill") as CompoundCriterion
    @JvmField val KILLED_BY_TEAM = get("killed_by_team") as CompoundCriterion

    // @formatter:on
    private fun get(name: String) = Registries.CRITERIA[Key.key("krypton", name)]!!
}
