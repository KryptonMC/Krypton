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
package org.kryptonmc.api.scoreboard.criteria

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All of the built-in criteria for scoreboards.
 */
@Catalogue(KeyedCriterion::class)
public object Criteria {

    // @formatter:off
    @JvmField
    public val DUMMY: RegistryReference<KeyedCriterion> = of("dummy")
    @JvmField
    public val TRIGGER: RegistryReference<KeyedCriterion> = of("trigger")
    @JvmField
    public val DEATH_COUNT: RegistryReference<KeyedCriterion> = of("death_count")
    @JvmField
    public val PLAYER_KILL_COUNT: RegistryReference<KeyedCriterion> = of("player_kill_count")
    @JvmField
    public val TOTAL_KILL_COUNT: RegistryReference<KeyedCriterion> = of("total_kill_count")
    @JvmField
    public val HEALTH: RegistryReference<KeyedCriterion> = of("health")
    @JvmField
    public val FOOD: RegistryReference<KeyedCriterion> = of("food")
    @JvmField
    public val AIR: RegistryReference<KeyedCriterion> = of("air")
    @JvmField
    public val ARMOR: RegistryReference<KeyedCriterion> = of("armor")
    @JvmField
    public val EXPERIENCE: RegistryReference<KeyedCriterion> = of("experience")
    @JvmField
    public val LEVEL: RegistryReference<KeyedCriterion> = of("level")
    @JvmField
    public val TEAM_KILL_BLACK: RegistryReference<KeyedCriterion> = of("team_kill_black")
    @JvmField
    public val TEAM_KILL_DARK_BLUE: RegistryReference<KeyedCriterion> = of("team_kill_dark_blue")
    @JvmField
    public val TEAM_KILL_DARK_GREEN: RegistryReference<KeyedCriterion> = of("team_kill_dark_green")
    @JvmField
    public val TEAM_KILL_DARK_AQUA: RegistryReference<KeyedCriterion> = of("team_kill_dark_aqua")
    @JvmField
    public val TEAM_KILL_DARK_RED: RegistryReference<KeyedCriterion> = of("team_kill_dark_red")
    @JvmField
    public val TEAM_KILL_DARK_PURPLE: RegistryReference<KeyedCriterion> = of("team_kill_dark_purple")
    @JvmField
    public val TEAM_KILL_GOLD: RegistryReference<KeyedCriterion> = of("team_kill_gold")
    @JvmField
    public val TEAM_KILL_GRAY: RegistryReference<KeyedCriterion> = of("team_kill_gray")
    @JvmField
    public val TEAM_KILL_DARK_GRAY: RegistryReference<KeyedCriterion> = of("team_kill_dark_gray")
    @JvmField
    public val TEAM_KILL_BLUE: RegistryReference<KeyedCriterion> = of("team_kill_blue")
    @JvmField
    public val TEAM_KILL_GREEN: RegistryReference<KeyedCriterion> = of("team_kill_green")
    @JvmField
    public val TEAM_KILL_AQUA: RegistryReference<KeyedCriterion> = of("team_kill_aqua")
    @JvmField
    public val TEAM_KILL_RED: RegistryReference<KeyedCriterion> = of("team_kill_red")
    @JvmField
    public val TEAM_KILL_LIGHT_PURPLE: RegistryReference<KeyedCriterion> = of("team_kill_light_purple")
    @JvmField
    public val TEAM_KILL_YELLOW: RegistryReference<KeyedCriterion> = of("team_kill_yellow")
    @JvmField
    public val TEAM_KILL_WHITE: RegistryReference<KeyedCriterion> = of("team_kill_white")
    @JvmField
    public val KILLED_BY_TEAM_BLACK: RegistryReference<KeyedCriterion> = of("killed_by_team_black")
    @JvmField
    public val KILLED_BY_TEAM_DARK_BLUE: RegistryReference<KeyedCriterion> = of("killed_by_team_dark_blue")
    @JvmField
    public val KILLED_BY_TEAM_DARK_GREEN: RegistryReference<KeyedCriterion> = of("killed_by_team_dark_green")
    @JvmField
    public val KILLED_BY_TEAM_DARK_AQUA: RegistryReference<KeyedCriterion> = of("killed_by_team_dark_aqua")
    @JvmField
    public val KILLED_BY_TEAM_DARK_RED: RegistryReference<KeyedCriterion> = of("killed_by_team_dark_red")
    @JvmField
    public val KILLED_BY_TEAM_DARK_PURPLE: RegistryReference<KeyedCriterion> = of("killed_by_team_dark_purple")
    @JvmField
    public val KILLED_BY_TEAM_GOLD: RegistryReference<KeyedCriterion> = of("killed_by_team_gold")
    @JvmField
    public val KILLED_BY_TEAM_GRAY: RegistryReference<KeyedCriterion> = of("killed_by_team_gray")
    @JvmField
    public val KILLED_BY_TEAM_DARK_GRAY: RegistryReference<KeyedCriterion> = of("killed_by_team_dark_gray")
    @JvmField
    public val KILLED_BY_TEAM_BLUE: RegistryReference<KeyedCriterion> = of("killed_by_team_blue")
    @JvmField
    public val KILLED_BY_TEAM_GREEN: RegistryReference<KeyedCriterion> = of("killed_by_team_green")
    @JvmField
    public val KILLED_BY_TEAM_AQUA: RegistryReference<KeyedCriterion> = of("killed_by_team_aqua")
    @JvmField
    public val KILLED_BY_TEAM_RED: RegistryReference<KeyedCriterion> = of("killed_by_team_red")
    @JvmField
    public val KILLED_BY_TEAM_LIGHT_PURPLE: RegistryReference<KeyedCriterion> = of("killed_by_team_light_purple")
    @JvmField
    public val KILLED_BY_TEAM_YELLOW: RegistryReference<KeyedCriterion> = of("killed_by_team_yellow")
    @JvmField
    public val KILLED_BY_TEAM_WHITE: RegistryReference<KeyedCriterion> = of("killed_by_team_white")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): RegistryReference<KeyedCriterion> = RegistryReference.of(Registries.CRITERIA, Key.key("krypton", name))
}
