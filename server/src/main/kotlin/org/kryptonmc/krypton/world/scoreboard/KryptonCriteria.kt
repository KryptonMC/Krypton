/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.scoreboard

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.scoreboard.RenderType

object KryptonCriteria {

    val DUMMY = register("dummy")
    val TRIGGER = register("trigger")
    val DEATH_COUNT = register("death_count", "deathCount")
    val PLAYER_KILL_COUNT = register("player_kill_count", "playerKillCount")
    val TOTAL_KILL_COUNT = register("total_kill_count", "totalKillCount")
    val HEALTH = register("health", isMutable = true, renderType = RenderType.HEARTS)
    val FOOD = register("food", isMutable = true)
    val AIR = register("air", isMutable = true)
    val ARMOR = register("armor", isMutable = true)
    val EXPERIENCE = register("experience", "xp", isMutable = true)
    val LEVEL = register("level", isMutable = true)
    val TEAM_KILL = register("team_kill", "teamkill", NamedTextColor.NAMES.keys().map {
        register("team_kill.$it", "teamkill.$it")
    })
    val KILLED_BY_TEAM = register("killed_by_team", "killedByTeam", NamedTextColor.NAMES.keys().map {
        register("team_kill.$it", "teamkill.$it")
    })

    private fun register(
        key: String,
        name: String = key,
        isMutable: Boolean = false,
        renderType: RenderType = RenderType.INTEGER
    ): KryptonCriterion {
        val key1 = Key.key(key)
        return Registries.register(
            Registries.CRITERIA,
            key1,
            KryptonCriterion(key1, name, isMutable, renderType)
        ) as KryptonCriterion
    }

    private fun register(
        key: String,
        name: String,
        children: List<KryptonCriterion>,
        isMutable: Boolean = false,
        renderType: RenderType = RenderType.INTEGER
    ): KryptonCompoundCriterion {
        val key1 = Key.key(key)
        return Registries.register(
            Registries.CRITERIA,
            key1,
            KryptonCompoundCriterion(key1, name, children, isMutable, renderType)
        ) as KryptonCompoundCriterion
    }
}
