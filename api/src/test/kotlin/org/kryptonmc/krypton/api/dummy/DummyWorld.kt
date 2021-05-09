/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api.dummy

import io.mockk.mockk
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.WorldBorder
import org.kryptonmc.krypton.api.world.WorldVersion
import org.kryptonmc.krypton.api.world.chunk.Chunk
import java.util.UUID

@Suppress("EqualsOrHashCode")
data class DummyWorld(override val name: String) : World {

    private val uuid = UUID.randomUUID()

    override val server = org.kryptonmc.krypton.api.server
    override val border = mockk<WorldBorder>()
    override val chunks = emptySet<Chunk>()
    override val difficulty = Difficulty.PEACEFUL
    override val gamemode = Gamemode.SURVIVAL
    override val isHardcore = false
    override val maxHeight = 256
    override val seed = 0L
    override val spawnLocation = Location(this, 0.0, 0.0, 0.0)
    override val time = 0L
    override val version = WorldVersion.CURRENT
    override var isRaining = false
    override var isThundering = false
    override var rainLevel = 0F
    override var thunderLevel = 0F
    override fun save() = Unit

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DummyWorld
        return uuid == other.uuid
    }
}
