/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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

import kotlinx.collections.immutable.ImmutableSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.scoreboard.ObjectiveRenderType
import org.kryptonmc.krypton.util.mapPersistentSet

object KryptonCriteria {

    @JvmField val DUMMY: KryptonCriterion = register("dummy")
    @JvmField val TRIGGER: KryptonCriterion = register("trigger")
    @JvmField val DEATH_COUNT: KryptonCriterion = register("death_count", "deathCount")
    @JvmField val PLAYER_KILL_COUNT: KryptonCriterion = register("player_kill_count", "playerKillCount")
    @JvmField val TOTAL_KILL_COUNT: KryptonCriterion = register("total_kill_count", "totalKillCount")
    @JvmField val HEALTH: KryptonCriterion = register("health", isMutable = true, renderType = ObjectiveRenderType.HEARTS)
    @JvmField val FOOD: KryptonCriterion = register("food", isMutable = true)
    @JvmField val AIR: KryptonCriterion = register("air", isMutable = true)
    @JvmField val ARMOR: KryptonCriterion = register("armor", isMutable = true)
    @JvmField val EXPERIENCE: KryptonCriterion = register("experience", "xp", isMutable = true)
    @JvmField val LEVEL: KryptonCriterion = register("level", isMutable = true)
    @JvmField val TEAM_KILL: KryptonCompoundCriterion = register(
        "team_kill",
        "teamkill",
        NamedTextColor.NAMES.keys().mapPersistentSet { register("team_kill.$it", "teamkill.$it") }
    )
    @JvmField val KILLED_BY_TEAM: KryptonCompoundCriterion = register(
        "killed_by_team",
        "killedByTeam",
        NamedTextColor.NAMES.keys().mapPersistentSet { register("killed_by_team.$it", "killedByTeam.$it") }
    )

    @JvmStatic
    private fun register(
        key: String,
        name: String = key,
        isMutable: Boolean = false,
        renderType: ObjectiveRenderType = ObjectiveRenderType.INTEGER
    ): KryptonCriterion {
        val namespacedKey = Key.key(key)
        return Registries.CRITERIA.register(namespacedKey, KryptonCriterion(namespacedKey, name, isMutable, renderType))
    }

    @JvmStatic
    private fun register(
        key: String,
        name: String,
        children: ImmutableSet<KryptonCriterion>,
        isMutable: Boolean = false,
        renderType: ObjectiveRenderType = ObjectiveRenderType.INTEGER
    ): KryptonCompoundCriterion {
        val namespacedKey = Key.key(key)
        return Registries.CRITERIA.register(namespacedKey, KryptonCompoundCriterion(namespacedKey, name, children, isMutable, renderType))
    }
}
