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
package org.kryptonmc.krypton.util

import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.player.Abilities

object GameModes {

    @JvmField
    val VALUES: Array<GameMode> = GameMode.values()
    private val BY_NAME = VALUES.associateBy { it.name.lowercase() }
    private val BY_ABBREVIATION = VALUES.associateBy { it.abbreviation }

    @JvmStatic
    fun fromId(id: Int): GameMode? = VALUES.getOrNull(id)

    @JvmStatic
    fun fromName(name: String): GameMode? = BY_NAME.get(name)

    @JvmStatic
    fun fromAbbreviation(abbreviation: String): GameMode? = BY_ABBREVIATION.get(abbreviation)

    @JvmStatic
    fun updatePlayerAbilities(mode: GameMode, abilities: Abilities) {
        when (mode) {
            GameMode.CREATIVE -> {
                abilities.canFly = true
                abilities.canInstantlyBuild = true
                abilities.invulnerable = true
            }
            GameMode.SPECTATOR -> {
                abilities.canFly = true
                abilities.canInstantlyBuild = false
                abilities.invulnerable = true
                abilities.flying = true
            }
            else -> {
                abilities.canFly = false
                abilities.canInstantlyBuild = false
                abilities.invulnerable = false
                abilities.flying = false
            }
        }
        abilities.canBuild = !isBlockPlacingRestricted(mode)
    }

    @JvmStatic
    fun isBlockPlacingRestricted(mode: GameMode): Boolean = mode == GameMode.ADVENTURE || mode == GameMode.SPECTATOR
}
