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
@Suppress("UndocumentedPublicProperty")
public object Criteria {

    // @formatter:off
    @JvmField public val DUMMY: Criterion = get("dummy")
    @JvmField public val TRIGGER: Criterion = get("trigger")
    @JvmField public val DEATH_COUNT: Criterion = get("death_count")
    @JvmField public val PLAYER_KILL_COUNT: Criterion = get("player_kill_count")
    @JvmField public val TOTAL_KILL_COUNT: Criterion = get("kill_count")
    @JvmField public val HEALTH: Criterion = get("health")
    @JvmField public val FOOD: Criterion = get("food")
    @JvmField public val AIR: Criterion = get("air")
    @JvmField public val ARMOR: Criterion = get("armor")
    @JvmField public val EXPERIENCE: Criterion = get("experience")
    @JvmField public val LEVEL: Criterion = get("level")
    @JvmField public val TEAM_KILL: CompoundCriterion = get("team_kill") as CompoundCriterion
    @JvmField public val KILLED_BY_TEAM: CompoundCriterion = get("killed_by_team") as CompoundCriterion

    // @formatter:on
    private fun get(name: String) = Registries.CRITERIA[Key.key("krypton", name)]!!
}
