/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.world.components

import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.world.chunk.data.Heightmap
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.flag.FeatureFlagSet

interface ReadOnlyWorld : ChunkGetter, BlockGetter, BiomeGetter, BrightnessGetter {

    val dimensionType: KryptonDimensionType

    fun seaLevel(): Int

    fun enabledFeatures(): FeatureFlagSet

    override fun height(): Int = dimensionType.height

    override fun minimumBuildHeight(): Int = dimensionType.minimumY

    fun getHeight(type: Heightmap.Type, x: Int, z: Int): Int

    fun getHeightmapPos(type: Heightmap.Type, pos: Vec3i): Vec3i = Vec3i(pos.x, getHeight(type, pos.x, pos.z), pos.z)

    fun canSeeSkyFromBelowWater(pos: Vec3i): Boolean {
        if (pos.y >= seaLevel()) return canSeeSky(pos)
        if (!canSeeSky(pos.x, seaLevel(), pos.z)) return false
        var currentY = seaLevel()
        while (currentY > pos.y) {
            val currentPos = Vec3i(pos.x, currentY, pos.z)
            val state = getBlock(currentPos)
            if (state.getLightBlock(this, currentPos) > 0 && !state.material.liquid) return false
            currentY--
        }
        return true
    }
}
