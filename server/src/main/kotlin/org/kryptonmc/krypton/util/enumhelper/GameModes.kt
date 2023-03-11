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
package org.kryptonmc.krypton.util.enumhelper

import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.player.Abilities

object GameModes {

    @JvmField
    val VALUES: Array<GameMode> = GameMode.values()
    private val BY_NAME = VALUES.associateBy { it.name.lowercase() }

    @JvmStatic
    fun fromId(id: Int): GameMode? = VALUES.getOrNull(id)

    @JvmStatic
    fun fromIdOrDefault(id: Int): GameMode = fromId(id) ?: GameMode.SURVIVAL

    @JvmStatic
    fun fromName(name: String): GameMode? = BY_NAME.get(name)

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
