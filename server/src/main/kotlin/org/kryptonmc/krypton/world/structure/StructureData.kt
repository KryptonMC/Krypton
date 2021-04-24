package org.kryptonmc.krypton.world.structure

import org.kryptonmc.krypton.api.block.BoundingBox

abstract class Structure(open val id: String)

abstract class StructurePiece(open val id: String) {

    abstract val ordinal: Int

    abstract val boundingBox: BoundingBox

    abstract val orientation: Orientation
}

enum class Orientation {

    NORTH,
    SOUTH,
    EAST,
    WEST
}
