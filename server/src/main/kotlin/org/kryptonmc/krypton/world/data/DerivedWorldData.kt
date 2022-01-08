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
import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import org.kryptonmc.krypton.world.rule.KryptonGameRuleHolder
import java.nio.file.Path
import java.util.UUID

class DerivedWorldData(private val delegate: WorldData) : WorldData {

    override val name: String
        get() = delegate.name
    override val folder: Path
        get() = delegate.folder
    override var gameMode: GameMode
        get() = delegate.gameMode
        set(_) = Unit
    override var difficulty: Difficulty
        get() = delegate.difficulty
        set(_) = Unit
    override var isHardcore: Boolean
        get() = delegate.isHardcore
        set(_) = Unit
    override var gameRules: KryptonGameRuleHolder
        get() = delegate.gameRules
        set(_) = Unit
    override var spawnX: Int
        get() = delegate.spawnX
        set(_) = Unit
    override var spawnY: Int
        get() = delegate.spawnY
        set(_) = Unit
    override var spawnZ: Int
        get() = delegate.spawnZ
        set(_) = Unit
    override var spawnAngle: Float
        get() = delegate.spawnAngle
        set(_) = Unit
    override var time: Long
        get() = delegate.time
        set(_) = Unit
    override var dayTime: Long
        get() = delegate.dayTime
        set(_) = Unit
    override var clearWeatherTime: Int
        get() = delegate.clearWeatherTime
        set(_) = Unit
    override var isRaining: Boolean
        get() = delegate.isRaining
        set(_) = Unit
    override var rainTime: Int
        get() = delegate.rainTime
        set(_) = Unit
    override var isThundering: Boolean
        get() = delegate.isThundering
        set(_) = Unit
    override var thunderTime: Int
        get() = delegate.thunderTime
        set(_) = Unit
    override var isInitialized: Boolean
        get() = delegate.isInitialized
        set(_) = Unit
    override val generationSettings: WorldGenerationSettings
        get() = delegate.generationSettings
    override var wanderingTraderSpawnDelay: Int
        get() = 0
        set(_) = Unit
    override var wanderingTraderSpawnChance: Int
        get() = 0
        set(_) = Unit
    override var wanderingTraderId: UUID?
        get() = null
        set(_) = Unit
}
