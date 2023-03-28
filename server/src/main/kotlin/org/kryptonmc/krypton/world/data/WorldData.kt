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
package org.kryptonmc.krypton.world.data

import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.rule.WorldGameRules
import org.kryptonmc.nbt.CompoundTag
import java.util.UUID

interface WorldData {

    // Settings
    val name: String
    var gameMode: GameMode
    var difficulty: Difficulty
    var isHardcore: Boolean
    var gameRules: WorldGameRules
    var isInitialized: Boolean
    val generationSettings: WorldGenerationSettings

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

    fun spawnPos(): Vec3i = Vec3i(spawnX, spawnY, spawnZ)

    fun save(): CompoundTag
}
