/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.chunk

import org.kryptonmc.api.block.BlockContainer
import org.kryptonmc.api.fluid.FluidContainer
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.biome.BiomeContainer

/**
 * Represents a chunk, or a 16 x 16 x world height area of blocks.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Chunk : BlockContainer, FluidContainer, BiomeContainer {

    /**
     * The world this chunk is in.
     */
    @get:JvmName("world")
    public val world: World

    /**
     * The X position of this chunk.
     */
    @get:JvmName("x")
    public val x: Int

    /**
     * The Z position of this chunk.
     */
    @get:JvmName("z")
    public val z: Int

    /**
     * The cumulative number of ticks players have been in this chunk.
     *
     * Note that this value increases faster when more players are in the
     * chunk.
     *
     * This is used for regional difficulty. It increases the chances of mobs
     * spawning with equipment, the chances of that equipment having
     * enchantments, the chances of spiders having potion effects, the chances
     * of mobs having the ability to pick up dropped items, and the chances of
     * zombies having the ability to spawn other zombies when attacked.
     *
     * Also note that regional difficulty is capped when this value reaches
     * 3600000, meaning that none of the above will increase past that point.
     *
     * See [here](https://minecraft.gamepedia.com/Chunk_format#NBT_structure)
     * for more details.
     */
    @get:JvmName("inhabitedTime")
    public val inhabitedTime: Long

    /**
     * The time that this chunk was last updated. This is set when the chunk is
     * saved to disk.
     */
    @get:JvmName("lastUpdate")
    public val lastUpdate: Long
}
