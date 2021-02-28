package org.kryptonmc.krypton.world.structure

import org.kryptonmc.krypton.space.BoundingBox
import org.kryptonmc.krypton.world.Biome
import org.kryptonmc.krypton.world.chunk.ChunkPosition

// TODO: Do thingys with these
data class Stronghold(
    val name: String,
    val boundingBox: BoundingBox,
    val biome: Biome,
    val chunkPosition: ChunkPosition,
    val pieces: List<StrongholdPiece>
) : Structure("Stronghold")

sealed class StrongholdPiece(override val id: String) : StructurePiece(id) {

    abstract val entryDoor: EntryDoorType
}

enum class EntryDoorType {

    WOODEN,
    IRON
}

enum class LargeRoomType {

    PILLAR_WITH_TORCHES,
    FOUNTAIN,
    UPPER_LEVEL_WITH_CHEST,
    EMPTY
}

data class StrongholdPortalRoom(
    override val ordinal: Int,
    override val boundingBox: BoundingBox,
    override val orientation: Orientation,
    override val entryDoor: EntryDoorType
) : StrongholdPiece("SHPR")

data class StrongholdLibrary(
    override val ordinal: Int,
    override val boundingBox: BoundingBox,
    override val orientation: Orientation,
    override val entryDoor: EntryDoorType,
    val isTall: Boolean
) : StrongholdPiece("SHLi")

data class StrongholdLargeRoom(
    override val ordinal: Int,
    override val boundingBox: BoundingBox,
    override val orientation: Orientation,
    override val entryDoor: EntryDoorType,
    val type: LargeRoomType
) : StrongholdPiece("SHRC")