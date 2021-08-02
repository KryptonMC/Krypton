/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.generation.structure

import org.kryptonmc.krypton.util.random.WorldGenRandom
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.generation.feature.StructureFeature
import org.kryptonmc.krypton.world.generation.feature.config.FeatureConfig

class StructureStart<C : FeatureConfig>(
    private val feature: StructureFeature<C>,
    private val position: ChunkPosition,
    private val references: Int,
    seed: Long
) : StructurePieceAccessor {

    private val pieces = mutableListOf<StructurePiece>()
    private val random = WorldGenRandom().apply { setLargeFeatureSeed(seed, position.x, position.z) }
    private var cachedBoundingBox: BoundingBox? = null

    override fun addPiece(piece: StructurePiece) {
        pieces.add(piece)
        cachedBoundingBox = null
    }

    override fun findCollisionPiece(boundingBox: BoundingBox) = pieces.firstOrNull { it.boundingBox.intersects(boundingBox) }
}
