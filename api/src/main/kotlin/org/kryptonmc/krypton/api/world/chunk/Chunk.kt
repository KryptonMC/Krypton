/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api.world.chunk

import org.kryptonmc.krypton.api.block.Block
import org.kryptonmc.krypton.api.space.Position
import org.kryptonmc.krypton.api.world.Biome
import org.kryptonmc.krypton.api.world.World

/**
 * Represents a chunk, or a 16 x 16 x 256 area of blocks.
 */
interface Chunk {

    /**
     * The world this chunk is in
     */
    val world: World

    /**
     * The X position of this chunk
     */
    val x: Int

    /**
     * The Z position of this chunk
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
     * See [here](https://minecraft.gamepedia.com/Chunk_format#NBT_structure) for more details
     */
    val inhabitedTime: Long

    /**
     * The time that this chunk was last updated. This is set when the chunk is saved to disk.
     */
    val lastUpdate: Long

    /**
     * Get a block from this world at the specified [position]
     *
     * @param position the position of the block to retrieve
     * @return the block at that position
     */
    fun getBlock(position: Position): Block

    /**
     * Set the block at this [block]'s position to the specified [block]
     *
     * @param block the block to set this block's position to
     * @return if the set was successful. This is implementation specific
     */
    fun setBlock(block: Block): Boolean
}
