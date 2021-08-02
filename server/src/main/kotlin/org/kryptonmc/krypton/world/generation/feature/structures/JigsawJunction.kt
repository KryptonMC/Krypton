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
package org.kryptonmc.krypton.world.generation.feature.structures

import com.mojang.serialization.Dynamic
import com.mojang.serialization.DynamicOps

data class JigsawJunction(
    val sourceX: Int,
    val sourceGroundY: Int,
    val sourceZ: Int,
    val deltaY: Int,
    val destProjection: StructureTemplatePool.Projection
) {

    fun <T> serialize(ops: DynamicOps<T>): Dynamic<T> = Dynamic(ops, ops.createMap(mapOf(
        ops.createString("source_x") to ops.createInt(sourceX),
        ops.createString("source_ground_y") to ops.createInt(sourceGroundY),
        ops.createString("source_z") to ops.createInt(sourceZ),
        ops.createString("delta_y") to ops.createInt(deltaY),
        ops.createString("dest_proj") to ops.createString(destProjection.serialized)
    )))
}
