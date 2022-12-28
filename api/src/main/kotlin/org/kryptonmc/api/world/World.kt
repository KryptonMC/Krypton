/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.key.Key
import org.kryptonmc.api.Server
import org.kryptonmc.api.block.BlockContainer
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.block.entity.BlockEntityContainer
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.fluid.FluidContainer
import org.kryptonmc.api.fluid.FluidState
import org.kryptonmc.api.fluid.Fluids
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.biome.BiomeContainer
import org.kryptonmc.api.world.chunk.BlockChangeFlags
import org.kryptonmc.api.world.chunk.Chunk
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRuleHolder
import java.nio.file.Path

/**
 * Represents a loaded world.
 */
public interface World : BlockContainer, FluidContainer, BiomeContainer, BlockEntityContainer, ForwardingAudience, GameRuleHolder {

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
    public val spawnLocation: Vec3i

    /**
     * All of the chunks currently loaded in this world.
     */
    public val chunks: Collection<Chunk>

    /**
     * All of the entities currently in this world.
     */
    public val entities: Collection<Entity>

    /**
     * All of the players currently in this world.
     */
    public val players: Collection<Player>

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
    public val gameMode: GameMode

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
     * The scoreboard for this world.
     */
    public val scoreboard: Scoreboard

    /**
     * Gets the block at the given coordinates.
     *
     * This function will return the following in specific cases:
     * - If the given [y] coordinate is greater than the maximum height of this
     * world, this will return the default state of [Blocks.VOID_AIR].
     * - If there is no chunk loaded at the given coordinates ([getChunkAt] was
     * null), this will return the default state of [Blocks.AIR].
     * - Else it will return the block at the given coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return see above
     */
    override fun getBlock(x: Int, y: Int, z: Int): BlockState

    /**
     * Gets the block at the given [position].
     *
     * This function will return the following in specific cases:
     * - If the given [position]'s [Vec3i.y] coordinate is greater than the
     * maximum height of this world, this will return [Blocks.VOID_AIR].
     * - If there is no chunk loaded at the given [position] ([getChunkAt] was
     * null), this will return [Blocks.AIR].
     * - Else it will return the block at the given [position].
     *
     * @param position the position
     * @return see above
     */
    override fun getBlock(position: Vec3i): BlockState

    /**
     * Sets the block at the given coordinates to the given [block].
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @param block the block
     * @param flags the flags to use when updating the block
     * @return true if the block was set, false otherwise
     */
    public fun setBlock(x: Int, y: Int, z: Int, block: BlockState, flags: BlockChangeFlags): Boolean

    /**
     * Sets the block at the given [position] to the given [block].
     *
     * @param position the position
     * @param block the block
     * @param flags the flags to use when updating the block
     * @return true if the block was set, false otherwise
     */
    public fun setBlock(position: Vec3i, block: BlockState, flags: BlockChangeFlags): Boolean

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
    override fun getFluid(x: Int, y: Int, z: Int): FluidState

    /**
     * Gets the fluid at the given [position].
     *
     * This function will return the following in specific cases:
     * - If the given [position]'s [Vec3i.y] coordinate is greater than the
     * maximum height of this world,  or if there is no chunk loaded at the
     * given [position] ([getChunkAt] returns null), this will return
     * [Fluids.EMPTY].
     * - Else it will return the fluid at the given [position].
     *
     * @param position the position
     * @return see above
     */
    override fun getFluid(position: Vec3i): FluidState

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
    public fun getChunk(position: Vec3i): Chunk?

    /**
     * Gets or loads the chunk at the given **chunk** coordinates, or returns
     * null if there is no chunk at the given chunk coordinates.
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
     * @return the loaded chunk, or null if not present
     */
    public fun loadChunk(x: Int, z: Int): Chunk?

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
     * Spawns an entity with the given [type] in this world at the given
     * [location].
     *
     * @param T the entity type
     * @param type the type of the entity
     * @param location the location to spawn the entity at
     */
    public fun <T : Entity> spawnEntity(type: EntityType<T>, location: Vec3d): T?

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
