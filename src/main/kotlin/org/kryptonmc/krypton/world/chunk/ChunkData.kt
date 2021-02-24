package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.world.Biome
import org.kryptonmc.krypton.world.block.Block
import org.kryptonmc.krypton.world.structure.Structure

// TODO: Finish this
data class ChunkData(
    val biomes: List<Biome>,
    //val carvingMasks: Pair<List<Byte>, List<Byte>>,
    //val entities: List<Entity>,
    val heightmaps: List<Pair<HeightmapType, List<Long>>>,
    val lastUpdate: Long,
    val lights: List<List<Short>>,
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

enum class HeightmapType {

    MOTION_BLOCKING,
    MOTION_BLOCKING_NO_LEAVES,
    OCEAN_FLOOR,
    OCEAN_FLOOR_WG,
    WORLD_SURFACE,
    WORLD_SURFACE_WG
}