/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.structure

import org.kryptonmc.krypton.api.block.BoundingBox
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Biome

// TODO: Do thingys with these
data class Stronghold(
    val name: String,
    val boundingBox: BoundingBox,
    val biome: Biome,
    val chunkPosition: Vector,
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
