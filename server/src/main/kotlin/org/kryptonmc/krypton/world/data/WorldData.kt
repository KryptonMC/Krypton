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
import org.kryptonmc.krypton.world.DataPackConfig
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.rule.KryptonGameRuleHolder
import java.nio.file.Path
import java.util.UUID

interface WorldData {

    // Settings
    val name: String
    val folder: Path
    var gameMode: GameMode
    var difficulty: Difficulty
    var isHardcore: Boolean
    var gameRules: KryptonGameRuleHolder
    var dataPackConfig: DataPackConfig
    var isInitialized: Boolean
    val worldGenerationSettings: WorldGenerationSettings

    // Spawn
    var spawnX: Int
    var spawnY: Int
    var spawnZ: Int
    var spawnAngle: Float

    // Time and weather
    var time: Long
    var dayTime: Long
    var clearWeatherTime: Int
    var isRaining: Boolean
    var rainTime: Int
    var isThundering: Boolean
    var thunderTime: Int

    // Wandering trader
    var wanderingTraderSpawnDelay: Int
    var wanderingTraderSpawnChance: Int
    var wanderingTraderId: UUID?
}
