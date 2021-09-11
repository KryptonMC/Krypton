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
import net.kyori.adventure.key.Key
import org.kryptonmc.api.Server
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.Fluids
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.space.Position
import org.kryptonmc.api.space.Vector
import org.kryptonmc.api.world.chunk.Chunk
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRuleHolder
import org.spongepowered.math.vector.Vector2i
import org.spongepowered.math.vector.Vector3i
import java.nio.file.Path

/**
 * Represents a loaded world.
 */
public interface World : ForwardingAudience {

    /**
     * The server this world was loaded on.
     */
    public val server: Server

    /**
     * The name of this world.
     */
    public val name: String

    /**
     * The folder of this world on disk.
     */
    public val folder: Path

    /**
     * The dimension resource key for this world.
     */
    public val dimension: ResourceKey<World>

    /**
     * The dimension that this world is.
     */
    public val dimensionType: DimensionType

    /**
     * The spawn location of this world.
     */
    public val spawnLocation: Vector3i

    /**
     * The set of chunks currently loaded in this world.
     */
    public val chunks: Collection<Chunk>

    /**
     * This world's border.
     */
    public val border: WorldBorder

    /**
     * The difficulty of this world.
     */
    public val difficulty: Difficulty

    /**
     * The default gamemode of this world.
     */
    public val gamemode: Gamemode

    /**
     * If the world is a hardcore world.
     */
    public val isHardcore: Boolean

    /**
     * The seed of this world.
     */
    public val seed: Long

    /**
     * The current time in this world.
     */
    public val time: Long

    /**
     * If this world is currently thundering (has an ongoing thunderstorm).
     */
    public val isThundering: Boolean

    /**
     * The level of the current thunderstorm (0 if there is no thunderstorm
     * going on).
     */
    public var thunderLevel: Float

    /**
     * If this world is currently raining.
     */
    public val isRaining: Boolean

    /**
     * The level of the current rain.
     */
    public var rainLevel: Float

    /**
     * The game rules for this world.
     */
    public val gameRules: GameRuleHolder

    /**
     * Gets the block at the given coordinates.
     *
     * This function will return the following in specific cases:
     * - If the given [y] coordinate is greater than the maximum height of this
     * world, this will return [Blocks.VOID_AIR].
     * - If there is no chunk loaded at the given coordinates ([getChunkAt] was
     * null), this will return [Blocks.AIR].
     * - Else it will return the block at the given coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return see above
     */
    public fun getBlock(x: Int, y: Int, z: Int): Block

    /**
     * Gets the block at the given [position].
     *
     * This function will return the following in specific cases:
     * - If the given [position]'s [Vector3i.y] coordinate is greater than the
     * maximum height of this world, this will return [Blocks.VOID_AIR].
     * - If there is no chunk loaded at the given [position] ([getChunkAt] was
     * null), this will return [Blocks.AIR].
     * - Else it will return the block at the given [position].
     *
     * @param position the position
     * @return see above
     */
    public fun getBlock(position: Vector3i): Block

    /**
     * Gets the block at the given [position].
     *
     * This function will return the following in specific cases:
     * - If the given [position]'s [Position.blockY] coordinate is greater than
     * the maximum height of this world, this will return [Blocks.VOID_AIR].
     * - If there is no chunk loaded at the given [position] ([getChunkAt] was
     * null), this will return [Blocks.AIR]
     * - Else it will return the block at the given [position]
     *
     * @param position the position
     * @return see above
     */
    public fun getBlock(position: Position): Block = getBlock(position.blockX, position.blockY, position.blockZ)

    /**
     * Gets the fluid at the given coordinates.
     *
     * This function will return the following in specific cases:
     * - If the given [y] coordinate is greater than the maximum height of this
     * world, or there is no chunk loaded at the given coordinates
     * ([getChunkAt] was null), this will return [Fluids.EMPTY].
     * - Else it will return the fluid at the given coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return see above
     */
    public fun getFluid(x: Int, y: Int, z: Int): Fluid

    /**
     * Gets the fluid at the given [position].
     *
     * This function will return the following in specific cases:
     * - If the given [position]'s [Vector3i.y] coordinate is greater than the
     * maximum height of this world,  or if there is no chunk loaded at the
     * given [position] ([getChunkAt] returns null), this will return
     * [Fluids.EMPTY].
     * - Else it will return the fluid at the given [position].
     *
     * @param position the position
     * @return see above
     */
    public fun getFluid(position: Vector3i): Fluid

    /**
     * Gets the fluid at the given [position].
     *
     * This function will return the following in specific cases:
     * - If the given [position]'s [Position.blockY] coordinate is greater than
     * the maximum height of this world, or if there is no chunk loaded at the
     * given [position] ([getChunkAt] was null), this will return
     * [Fluids.EMPTY].
     * - Else it will return the fluid at the given [position]
     *
     * @param position the position
     * @return see above
     */
    public fun getFluid(position: Position): Fluid = getFluid(position.blockX, position.blockY, position.blockZ)

    /**
     * Sets the block at the given coordinates to the given [block].
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @param block the new block
     */
    public fun setBlock(x: Int, y: Int, z: Int, block: Block)

    /**
     * Sets the block at the given [position] to the given [block].
     *
     * @param position the position
     * @param block the new block
     */
    public fun setBlock(position: Vector3i, block: Block)

    /**
     * Sets the block at the given [position] to the given [block].
     *
     * @param position the position
     * @param block the block
     */
    public fun setBlock(position: Position, block: Block): Unit =
        setBlock(position.blockX, position.blockY, position.blockZ, block)

    /**
     * Gets a chunk from its **chunk** coordinates, or returns null if there is
     * no chunk **loaded** at the given coordinates.
     *
     * That is, to calculate the chunk coordinate from a given block
     * coordinate, the block coordinate is shifted right by 4 (divided by 16
     * and floored).
     *
     * @param x the chunk X coordinate
     * @param z the chunk Z coordinate
     * @return the chunk at the given coordinates, or null if there isn't one
     * loaded
     */
    public fun getChunkAt(x: Int, z: Int): Chunk?

    /**
     * Gets a chunk from its **chunk** coordinates, or returns null if there is
     * no chunk **loaded** at the given coordinates.
     *
     * That is, to calculate the chunk coordinate from a given block
     * coordinate, the block coordinate is shifted right by 4 (divided by 16
     * and floored).
     *
     * @param position the chunk position
     * @return the chunk at the given position, or null if there isn't one
     * loaded
     */
    public fun getChunkAt(position: Vector2i): Chunk? = getChunkAt(position.x(), position.y())

    /**
     * Gets a chunk from its **block** coordinates, or returns null if there is
     * no chunk **loaded** at the given coordinates.
     *
     * @param x the block X coordinate
     * @param y the block Y coordinate
     * @param z the block Z coordinate
     * @return the chunk at the given coordinates, or null if there isn't one
     * loaded
     */
    public fun getChunk(x: Int, y: Int, z: Int): Chunk?

    /**
     * Gets a chunk from its **block** coordinates, or returns null if there is
     * no chunk **loaded** at the given coordinates.
     *
     * @param position the block position
     * @return the chunk at the given coordinates, or null if there isn't one
     * loaded
     */
    public fun getChunk(position: Vector3i): Chunk?

    /**
     * Gets a chunk from its **block** coordinates, or returns null if there is
     * no chunk **loaded** at the given coordinates.
     *
     * @param position the position
     * @return the chunk at the given coordinates, or null if there isn't one
     * loaded
     */
    public fun getChunk(position: Position): Chunk?

    /**
     * Gets or loads the chunk at the given **chunk** coordinates.
     *
     * That is, to calculate the chunk coordinate from a given block
     * coordinate, the block coordinate is shifted right by 4 (divided by 16
     * and floored).
     *
     * Beware that chunks loaded using this function will not be automatically
     * unloaded!
     *
     * @param x the X coordinate
     * @param z the Z coordinate
     */
    public fun loadChunk(x: Int, z: Int): Chunk

    /**
     * Unloads the chunk at the specified [x] and [z] coordinates if there is
     * a chunk loaded. If there is no chunk loaded at the coordinates, this
     * function simply returns.
     *
     * If [force] is set to false and this chunk was not loaded using
     * [loadChunk], this will also simply return. If it is set to true,
     * however, the chunk will always be unloaded.
     *
     * Like [loadChunk], these coordinates are **chunk** coordinates.
     *
     * @param x the X coordinate
     * @param z the Z coordinate
     * @param force whether to force unload the chunk or not
     */
    public fun unloadChunk(x: Int, z: Int, force: Boolean)

    /**
     * Saves this world to disk. Exposed as a function of [World] to allow for
     * custom world implementations to define this.
     */
    public fun save()

    /**
     * Spawns an entity with the given [type] in this world at the given
     * [location].
     *
     * @param type the type of the entity
     * @param location the location to spawn the entity at
     */
    public fun <T : Entity> spawnEntity(type: EntityType<T>, location: Vector): T?

    public companion object {

        /**
         * The resource key for the overworld dimension.
         */
        @JvmField
        public val OVERWORLD: ResourceKey<World> = ResourceKey.of(ResourceKeys.DIMENSION, Key.key("overworld"))

        /**
         * The resource key for the nether dimension.
         */
        @JvmField
        public val NETHER: ResourceKey<World> = ResourceKey.of(ResourceKeys.DIMENSION, Key.key("the_nether"))

        /**
         * The resource key for the end dimension.
         */
        @JvmField
        public val END: ResourceKey<World> = ResourceKey.of(ResourceKeys.DIMENSION, Key.key("the_end"))
    }
}
