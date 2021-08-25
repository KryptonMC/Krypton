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
package org.kryptonmc.krypton.space

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.util.Codecs
import org.spongepowered.math.vector.Vector3i

data class GlobalPosition(
    val dimension: ResourceKey<World>,
    val position: Vector3i
) {

    companion object {

        val CODEC: Codec<GlobalPosition> = RecordCodecBuilder.create {
            it.group(
                Codecs.DIMENSION.fieldOf("dimension").forGetter(GlobalPosition::dimension),
                Codecs.VECTOR3I_STREAM.fieldOf("pos").forGetter(GlobalPosition::position)
            ).apply(it, ::GlobalPosition)
        }
    }
}
