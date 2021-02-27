package org.kryptonmc.krypton.world.structure

import org.kryptonmc.krypton.space.BoundingBox
import org.kryptonmc.krypton.world.block.FacingState

abstract class Structure(open val id: String)

abstract class StructurePiece(open val id: String) {

    abstract val ordinal: Int

    abstract val boundingBox: BoundingBox

    abstract val orientation: FacingState
}