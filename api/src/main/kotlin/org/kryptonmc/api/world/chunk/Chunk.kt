/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.world.chunk

import org.kryptonmc.api.block.BlockContainer
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.fluid.FluidContainer
import org.kryptonmc.api.world.EntityContainer
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.biome.BiomeContainer

/**
 * Represents a chunk, or a 16 x 16 x world height area of blocks.
 */
public interface Chunk : BlockContainer, FluidContainer, BiomeContainer, EntityContainer {

    /**
     * The world this chunk is in.
     */
    public val world: World

    /**
     * The X position of this chunk.
     */
    public val x: Int

    /**
     * The Z position of this chunk.
     */
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
     * See [here](https://minecraft.wiki/w/Chunk_format#NBT_structure)
     * for more details.
     */
    public val inhabitedTime: Long

    /**
     * The time that this chunk was last updated. This is set when the chunk is
     * saved to disk.
     */
    public val lastUpdate: Long

    /**
     * All of the entities currently in this chunk.
     */
    override val entities: Collection<Entity>

    /**
     * All of the players currently in this chunk.
     */
    override val players: Collection<Player>
}
