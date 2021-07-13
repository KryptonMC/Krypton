/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.chunk

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.space.Position
import org.kryptonmc.api.world.Biome
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i

/**
 * Represents a chunk, or a 16 x 16 x 256 area of blocks.
 */
interface Chunk {

    /**
     * The world this chunk is in.
     */
    val world: World

    /**
     * The X position of this chunk.
     */
    val x: Int

    /**
     * The Z position of this chunk.
     */
    val z: Int

    /**
     * The list of biomes in this chunk. May be empty.
     */
    val biomes: List<Biome>

    /**
     * The cumulative number of ticks players have been in this chunk.
     *
     * Note that this value increases faster when more players are in the chunk.
     *
     * This is used for regional difficulty. It increases the chances of mobs spawning with
     * equipment, the chances of that equipment having enchantments, the chances of spiders
     * having potion effects, the chances of mobs having the ability to pick up dropped items,
     * and the chances of zombies having the ability to spawn other zombies when attacked.
     *
     * Also note that regional difficulty is capped when this value reaches
     * 3600000, meaning that none of the above will increase past that point.
     *
     * See [here](https://minecraft.gamepedia.com/Chunk_format#NBT_structure) for more details.
     */
    val inhabitedTime: Long

    /**
     * The time that this chunk was last updated. This is set when the chunk is saved to disk.
     */
    val lastUpdate: Long

    /**
     * Get a block from this chunk at the specified [x], [y] and [z] coordinates.
     *
     * @param x the x coordinate of the block
     * @param y the y coordinate of the block
     * @param z the z coordinate of the block
     * @return the block at the specified coordinates
     */
    fun getBlock(x: Int, y: Int, z: Int): Block

    /**
     * Get a block from this chunk at the specified [position].
     *
     * @param position the position of the block to retrieve
     * @return the block at that position
     */
    fun getBlock(position: Position): Block = getBlock(position.blockX, position.blockY, position.blockZ)

    /**
     * Gets a block from this chunk at the specified [position].
     *
     * @param position the position of the block to retrieve
     * @return the block at that position
     */
    fun getBlock(position: Vector3i): Block = getBlock(position.x(), position.y(), position.z())

    /**
     * Set the block at this [block]'s position to the specified [block].
     *
     * @param block the block to set this block's position to
     * @return if the set was successful. This is implementation specific
     */
    fun setBlock(x: Int, y: Int, z: Int, block: Block)
}
