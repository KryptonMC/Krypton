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
package org.kryptonmc.krypton.world.dimension

import com.mojang.serialization.DataResult
import com.mojang.serialization.Dynamic
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.util.Codecs

fun Dynamic<*>.parseDimension(): DataResult<ResourceKey<World>> {
    val id = asNumber().result()
    if (id.isPresent) when (id.get().toInt()) { // Parse legacy dimension type
        -1 -> return DataResult.success(World.NETHER)
        0 -> return DataResult.success(World.OVERWORLD)
        1 -> return DataResult.success(World.END)
    }
    return Codecs.DIMENSION.parse(this)
}
