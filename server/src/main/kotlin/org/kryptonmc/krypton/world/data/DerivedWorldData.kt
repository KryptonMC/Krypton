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

import org.kryptonmc.krypton.world.generation.WorldGenerationSettings
import java.util.UUID

class DerivedWorldData(delegate: WorldData) : WorldData {

    override val name = delegate.name
    override val folder = delegate.folder
    override var gameMode = delegate.gameMode
        set(_) = Unit
    override var difficulty = delegate.difficulty
        set(_) = Unit
    override var isHardcore = delegate.isHardcore
        set(_) = Unit
    override var gameRules = delegate.gameRules
        set(_) = Unit
    override var dataPackConfig = delegate.dataPackConfig
        set(_) = Unit
    override var spawnX = delegate.spawnX
        set(_) = Unit
    override var spawnY = delegate.spawnY
        set(_) = Unit
    override var spawnZ = delegate.spawnZ
        set(_) = Unit
    override var spawnAngle = delegate.spawnAngle
        set(_) = Unit
    override var time = delegate.time
        set(_) = Unit
    override var dayTime = delegate.dayTime
        set(_) = Unit
    override var clearWeatherTime = delegate.clearWeatherTime
        set(_) = Unit
    override var isRaining = delegate.isRaining
        set(_) = Unit
    override var rainTime = delegate.rainTime
        set(_) = Unit
    override var isThundering = delegate.isThundering
        set(_) = Unit
    override var thunderTime = delegate.thunderTime
        set(_) = Unit
    override var isInitialized = delegate.isInitialized
        set(_) = Unit
    override val worldGenerationSettings: WorldGenerationSettings = delegate.worldGenerationSettings
    override var wanderingTraderSpawnDelay = 0
        set(_) = Unit
    override var wanderingTraderSpawnChance = 0
        set(_) = Unit
    override var wanderingTraderId: UUID? = null
        set(_) = Unit
}
