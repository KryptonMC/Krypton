package org.kryptonmc.krypton.api.world.chunk

import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Biome
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import java.time.LocalDateTime

/**
 * Represents a chunk, or a 16 x 16 x 256 area of blocks.
 *
 * @author Callum Seabrook
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
     * The list of biomes in this chunk.
     */
    val biomes: List<Biome>

    /**
     * The cumulative number of ticks players have been in this chunk.
     *
     * Note that this value increases faster when more players are in
     * the chunk.
     *
     * This is used for regional difficulty. It increases the chances of
     * mobs spawning with equipment, the chances of that equipment having
     * enchantments, the chances of spiders having potion effects, the
     * chances of mobs having the ability to pick up dropped items, and the
     * chances of zombies having the ability to spawn other zombies when
     * attacked.
     *
     * Also note that regional difficulty is capped when this value reaches
     * 3600000, meaning that none of the above will increase past that point.
     *
     * See [here](https://minecraft.gamepedia.com/Chunk_format#NBT_structure) for more details
     *
     * @return The inhabited time, in ticks
     */
    val inhabitedTime: Long

    /**
     * The time that this chunk was last updated (will always be the value
     * read from the file)
     */
    val lastUpdate: Long
}