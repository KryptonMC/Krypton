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
package org.kryptonmc.krypton.world.data

import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.util.enumhelper.Difficulties
import org.kryptonmc.krypton.util.enumhelper.GameModes
import org.kryptonmc.krypton.world.rule.WorldGameRules
import org.kryptonmc.serialization.Dynamic

class WorldSettings(
    val worldName: String,
    val gameMode: GameMode,
    val hardcore: Boolean,
    val difficulty: Difficulty,
    val gameRules: WorldGameRules,
    val dataConfiguration: WorldDataConfiguration
) {

    fun withGameMode(mode: GameMode): WorldSettings = WorldSettings(worldName, mode, hardcore, difficulty, gameRules, dataConfiguration)

    fun withDifficulty(difficulty: Difficulty): WorldSettings =
        WorldSettings(worldName, gameMode, hardcore, difficulty, gameRules, dataConfiguration)

    fun withDataConfiguration(config: WorldDataConfiguration): WorldSettings =
        WorldSettings(worldName, gameMode, hardcore, difficulty, gameRules, config)

    companion object {

        @JvmStatic
        fun parse(data: Dynamic<*>, config: WorldDataConfiguration): WorldSettings {
            val gameMode = GameModes.fromId(data.get("GameType").asInt(0)) ?: GameMode.SURVIVAL
            return WorldSettings(
                data.get("LevelName").asString(""),
                gameMode,
                data.get("hardcore").asBoolean(false),
                data.get("Difficulty").asNumber().map { Difficulties.fromId(it.toInt()) ?: Difficulty.NORMAL }.result().orElse(Difficulty.NORMAL),
                WorldGameRules(data.get("GameRules")),
                config
            )
        }
    }
}
