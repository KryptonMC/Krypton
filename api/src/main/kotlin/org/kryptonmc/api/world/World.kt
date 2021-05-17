/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import net.kyori.adventure.audience.ForwardingAudience
import org.kryptonmc.api.Server
import org.kryptonmc.api.world.chunk.Chunk
import java.nio.file.Path
import java.util.UUID

/**
 * Represents a loaded world
 */
interface World : ForwardingAudience {

    /**
     * The server this world was loaded on
     */
    val server: Server

    /**
     * The name of this world
     */
    val name: String

    /**
     * The unique ID of this world
     */
    val uuid: UUID

    /**
     * The folder of this world on disk
     */
    val folder: Path

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
     * If this world is currently thundering (has an ongoing thunderstorm)
     */
    var isThundering: Boolean

    /**
     * The level of the current thunderstorm (0 if there is no thunderstorm going on)
     */
    var thunderLevel: Float

    /**
     * If this world is currently raining
     */
    var isRaining: Boolean

    /**
     * The level of the current rain
     */
    var rainLevel: Float

    /**
     * Saves this world to disk. Exposed as a function of [World] to allow for custom world implementations
     * to define this.
     */
    fun save()
}

/**
 * Holder for information about the version of Minecraft this world was
 * generated in.
 *
 * @param id the ID of the world version
 * @param name the name of the Minecraft version
 * @param isSnapshot whether this world version is a snapshot version or not
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
