package org.kryptonmc.krypton.world.structure

import org.kryptonmc.krypton.space.BoundingBox
import org.kryptonmc.krypton.world.Biome
import org.kryptonmc.krypton.world.block.Facing
import org.kryptonmc.krypton.world.chunk.ChunkPosition

data class Mineshaft(
    val name: String,
    val boundingBox: BoundingBox,
    val biome: Biome,
    val chunkPosition: ChunkPosition,
    val pieces: List<MineshaftPiece>
) : Structure("Mineshaft")

sealed class MineshaftPiece(override val id: String) : StructurePiece(id)

data class MineshaftCorridor(
    override val ordinal: Int,
    override val boundingBox: BoundingBox,
    override val orientation: Facing,
    val hasCobwebs: Boolean,
    val hasSpawner: Boolean,
    val hasRails: Boolean,
    val length: Int
) : MineshaftPiece("MSCorridor")

data class MineshaftCrossing(
    override val ordinal: Int,
    override val boundingBox: BoundingBox,
    override val orientation: Facing,
    val incoming: Int,
    val twoFloors: Boolean
) : MineshaftPiece("MSCrossing")

data class MineshaftRoom(
    override val ordinal: Int,
    override val boundingBox: BoundingBox,
    override val orientation: Facing,
    val entrances: List<BoundingBox>
) : MineshaftPiece("MSRoom")