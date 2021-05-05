package org.kryptonmc.krypton.api.world

import org.kryptonmc.krypton.api.Server
import org.kryptonmc.krypton.api.world.chunk.Chunk

/**
 * Represents a loaded world
 */
interface World {

    /**
     * The server this world was loaded on
     */
    val server: Server

    /**
     * The name of this world
     */
    val name: String

    /**
     * The spawn location of this world.
     *
     * As spawn locations do not allow for coordinates with fractional
     * precision, the x, y and z values of this [Location] will always
     * be whole numbers (integers). They will never possess a decimal
     * component.
     *
     * Spawn locations also do not possess any rotational components, so
     * the pitch and yaw of this [Location] will always be set to 0.
     */
    val spawnLocation: Location

    /**
     * The set of chunks currently loaded in this world
     */
    val chunks: Set<Chunk>

    /**
     * This world's border
     */
    val border: WorldBorder

    /**
     * The difficulty of this world.
     */
    val difficulty: Difficulty

    /**
     * The default gamemode of this world
     */
    val gamemode: Gamemode

    /**
     * If the world is a hardcore world
     */
    val isHardcore: Boolean

    /**
     * The seed of this world
     */
    val seed: Long

    /**
     * The current time in this world
     */
    val time: Long

    /**
     * The version information of this world
     */
    val version: WorldVersion

    /**
     * The maximum build height of this world. Also known as the build limit.
     */
    val maxHeight: Int

    /**
     * Saves this world to disk. Exposed as a function of [World] to allow for custom world implementations
     * to define this.
     */
    fun save()
}

/**
 * Holder for information about the version of Minecraft this world was
 * generated in.
 */
data class WorldVersion(
    val id: Int,
    val name: String,
    val isSnapshot: Boolean
) {

    companion object {

        /**
         * The current world version
         */
        @JvmField
        val CURRENT = WorldVersion(2568, "1.16.5", false)
    }
}
