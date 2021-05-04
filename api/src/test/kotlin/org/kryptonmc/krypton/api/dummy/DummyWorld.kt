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
    override fun save() = Unit

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DummyWorld
        return uuid == other.uuid
    }
}
