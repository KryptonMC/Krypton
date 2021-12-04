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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.registry.Registries

object KryptonVisibilities {

    // @formatter:off
    @JvmField val ALWAYS: KryptonVisibility = register(0, "always")
    @JvmField val NEVER: KryptonVisibility = register(1, "never")
    @JvmField val HIDE_FOR_OTHER_TEAMS: KryptonVisibility = register(2, "hide_for_other_teams", "hideForOtherTeams")
    @JvmField val HIDE_FOR_OWN_TEAM: KryptonVisibility = register(3, "hide_for_own_team", "hideForOwnTeam")

    // @formatter:on
    @JvmStatic
    private fun register(id: Int, key: String, name: String = key): KryptonVisibility {
        val key1 = Key.key("krypton", key)
        return Registries.VISIBILITIES.register(
            id,
            key1,
            KryptonVisibility(id, name, key1, Component.translatable("team.visibility.$name"))
        )
    }
}
