package org.kryptonmc.krypton.world.structure

import org.kryptonmc.krypton.api.block.BoundingBox
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Biome

// TODO: Do thingys with these
data class Mineshaft(
    val name: String,
    val boundingBox: BoundingBox,
    val biome: Biome,
    val chunkPosition: Vector,
    val pieces: List<MineshaftPiece>
) : Structure("Mineshaft")

sealed class MineshaftPiece(override val id: String) : StructurePiece(id)

data class MineshaftCorridor(
    override val ordinal: Int,
    override val boundingBox: BoundingBox,
    override val orientation: Orientation,
    val hasCobwebs: Boolean,
    val hasSpawner: Boolean,
    val hasRails: Boolean,
    val length: Int
) : MineshaftPiece("MSCorridor")

data class MineshaftCrossing(
    override val ordinal: Int,
    override val boundingBox: BoundingBox,
    override val orientation: Orientation,
    val incoming: Int,
    val twoFloors: Boolean
) : MineshaftPiece("MSCrossing")

data class MineshaftRoom(
    override val ordinal: Int,
    override val boundingBox: BoundingBox,
    override val orientation: Orientation,
    val entrances: List<BoundingBox>
) : MineshaftPiece("MSRoom")
