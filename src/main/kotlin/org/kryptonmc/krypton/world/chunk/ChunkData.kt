package org.kryptonmc.krypton.world.chunk

import net.kyori.adventure.nbt.LongArrayBinaryTag
import org.kryptonmc.krypton.world.Biome
import org.kryptonmc.krypton.world.block.Block
import org.kryptonmc.krypton.world.structure.Structure

// TODO: Finish this
data class ChunkData(
    val biomes: List<Biome>,
    //val carvingMasks: Pair<List<Byte>, List<Byte>>,
    //val entities: List<Entity>,
    val heightmaps: Heightmaps,
    val lastUpdate: Long,
    //val lights: List<List<Short>>,
    //val liquidsToBeTicked: List<List<Short>>,
    //val liquidTicks: List<CompoundBinaryTag>,
    val inhabitedTime: Long,
    //val postProcessing: List<List<Short>>,
    val sections: List<ChunkSection>,
    //val status: WorldGenerationStatus,
    //val tileEntities: List<BlockEntity>,
    //val tileTicks: List<TileTick>,
    //val toBeTicked: List<List<Short>>,
    //val structures: StructureData
)

data class Heightmaps(
    val motionBlocking: LongArrayBinaryTag,
    val oceanFloor: LongArrayBinaryTag,
    val worldSurface: LongArrayBinaryTag,
)

// TODO: Do things with these
data class TileTick(
    val block: Block,
    val priority: Int,
    val delay: Int
)

data class StructureData(
    val references: Map<String, ChunkPosition>,
    val starts: List<Structure>
)