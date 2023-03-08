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
import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.biome.BiomeContainer
import org.kryptonmc.api.world.chunk.BlockChangeFlags
import org.kryptonmc.api.world.chunk.Chunk
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRuleHolder
import java.nio.file.Path
import java.util.function.Consumer

/**
 * Represents a loaded world.
 */
public interface World : BlockContainer, FluidContainer, BiomeContainer, BlockEntityContainer, EntityContainer, ForwardingAudience, GameRuleHolder {

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
    override val entities: Collection<Entity>

    /**
     * All of the players currently in this world.
     */
    override val players: Collection<Player>

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
     * The seed of this world.
     */
    public val seed: Long

    /**
     * The current time in this world.
     */
    public val time: Long

    /**
     * The level of the current rain.
     */
    public var rainLevel: Float

    /**
     * The level of the current thunderstorm (0 if there is no thunderstorm
     * going on).
     */
    public var thunderLevel: Float

    /**
     * The scoreboard for this world.
     */
    public val scoreboard: Scoreboard

    /**
     * The scheduler for this world.
     *
     * Useful for scheduling tasks that should only exist for the lifetime of
     * the world, as all tasks that are scheduler with this scheduler will
     * stop running after the world is removed.
     */
    public val scheduler: Scheduler

    /**
     * The registry holder for this world.
     *
     * This contains registries that are specific to this world, and not
     * shared across the whole server.
     */
    public val registryHolder: RegistryHolder

    /**
     * If the world is a hardcore world.
     *
     * @return true if this world is hardcore
     */
    public fun isHardcore(): Boolean

    /**
     * If this world is currently raining.
     *
     * @return true if it is raining in this world
     */
    public fun isRaining(): Boolean

    /**
     * If this world is currently thundering (has an ongoing thunderstorm).
     *
     * @return true if it is thundering in this world
     */
    public fun isThundering(): Boolean

    /**
     * Gets the block at the given coordinates.
     *
     * This method will return the following in specific cases:
     * - If the given [y] coordinate is greater than the maximum height of this
     * world, this will return the default state of [Blocks.VOID_AIR].
     * - If there is no chunk loaded at the given coordinates ([getChunk] was
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
     * This method has semantics identical to that of [getBlock] with
     * individual components (X, Y, and Z values).
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
     * This method will return the following in specific cases:
     * - If the given [y] coordinate is greater than the maximum height of this
     * world, or there is no chunk loaded at the given coordinates
     * ([getChunk] was null), this will return [Fluids.EMPTY].
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
     * This method has semantics identical to that of [getFluid] with
     * individual components (X, Y, and Z values).
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
    public fun getChunk(x: Int, z: Int): Chunk?

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
     * Like [loadChunk], these coordinates are **chunk** coordinates.
     *
     * @param x the X coordinate
     * @param z the Z coordinate
     */
    public fun unloadChunk(x: Int, z: Int)

    /**
     * Spawns an entity with the given [type] in this world at the
     * given [position].
     *
     * @param T the entity type
     * @param type the type of the entity
     * @param position the position to spawn the entity at
     */
    public fun <T : Entity> spawnEntity(type: EntityType<T>, position: Position): T?

    /**
     * Gets all entities that are within the given [range] of the
     * given [position], calling the given [callback] for each entity found.
     *
     * @param position the centre position to look around
     * @param range the range to look for entities in
     * @param callback the callback called for each entity found
     */
    public fun getNearbyEntities(position: Position, range: Double, callback: Consumer<Entity>)

    /**
     * Gets all entities that are within the given [range] of the
     * given [position].
     *
     * @param position the centre position to look around
     * @param range the range to look for entities in
     * @return all found entities
     */
    public fun getNearbyEntities(position: Position, range: Double): Collection<Entity>

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
