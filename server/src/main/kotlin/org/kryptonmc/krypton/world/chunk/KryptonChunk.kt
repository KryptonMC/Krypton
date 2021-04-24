package org.kryptonmc.krypton.world.chunk

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import org.kryptonmc.krypton.api.world.Biome
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.chunk.Chunk

data class KryptonChunk(
    override val world: World,
    val position: ChunkPosition,
    val sections: List<ChunkSection>,
    override val biomes: List<Biome>,
    override var lastUpdate: Long,
    override var inhabitedTime: Long,
    val heightmaps: Heightmaps,
    val carvingMasks: Pair<ByteArray, ByteArray>,
    val structures: CompoundBinaryTag
) : Chunk {

    override val x: Int get() = position.x
    override val z: Int get() = position.z

    fun tick(playerCount: Int) {
        inhabitedTime += playerCount
    }
}

// TODO: Migrate the remaining commented parameters to KryptonChunk
//data class ChunkData(
//    val biomes: List<Biome>,
//    val carvingMasks: Pair<ByteArray, ByteArray>,
//    val entities: List<Entity>,
//    val heightmaps: Heightmaps,
//    val lastUpdate: Long,
//    val lights: List<ShortArray>,
//    val liquidsToBeTicked: List<List<Short>>,
//    val liquidTicks: List<CompoundBinaryTag>,
//    val inhabitedTime: Long,
//    val postProcessing: List<ShortArray>,
//    val sections: List<ChunkSection>,
//    val status: WorldGenerationStatus,
//    val tileEntities: List<BlockEntity>,
//    val tileTicks: List<TileTick>,
//    val toBeTicked: List<ShortArray>,
//    val structures: StructureData
//)

data class Heightmaps(
    val motionBlocking: LongArrayBinaryTag,
    val oceanFloor: LongArrayBinaryTag,
    val worldSurface: LongArrayBinaryTag,
)

// TODO: Do things with these
//data class TileTick(
//    val block: Block,
//    val priority: Int,
//    val delay: Int
//)
//
//data class StructureData(
//    val references: Map<String, Vector>,
//    val starts: List<Structure>
//)
