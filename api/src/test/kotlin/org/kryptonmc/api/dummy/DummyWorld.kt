/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.dummy

import io.mockk.mockk
import net.kyori.adventure.audience.Audience
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.space.Vector
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.Location
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.WorldBorder
import org.kryptonmc.api.world.WorldVersion
import org.kryptonmc.api.world.chunk.Chunk
import java.nio.file.Path
import java.util.UUID

@Suppress("EqualsOrHashCode")
data class DummyWorld(override val name: String) : World {

    override val uuid: UUID = UUID.randomUUID()
    override val folder: Path = Path.of("")
    override val server = org.kryptonmc.api.server
    override val border = mockk<WorldBorder>()
    override val chunks = emptySet<Chunk>()
    override val difficulty = Difficulty.PEACEFUL
    override val gamemode = Gamemode.SURVIVAL
    override val isHardcore = false
    override val maxHeight = 256
    override val seed = 0L
    override val spawnLocation = Location(0.0, 0.0, 0.0)
    override val time = 0L
    override val version = WorldVersion.CURRENT
    override var isRaining = false
    override var isThundering = false
    override var rainLevel = 0F
    override var thunderLevel = 0F
    override fun save() = Unit
    override fun <T : Entity> spawnEntity(type: EntityType<T>, location: Vector) = Unit
    override fun spawnExperienceOrb(location: Vector) = Unit
    override fun spawnPainting(location: Vector) = Unit
    override fun audiences() = emptySet<Audience>()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DummyWorld
        return uuid == other.uuid
    }
}
