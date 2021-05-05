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
