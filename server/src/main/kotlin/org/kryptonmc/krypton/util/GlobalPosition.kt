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
package org.kryptonmc.krypton.util

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.CompoundCodec
import org.kryptonmc.krypton.util.serialization.decode
import org.kryptonmc.krypton.util.serialization.encode
import org.kryptonmc.nbt.compound
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class GlobalPosition(val dimension: ResourceKey<World>, val position: Vector3i) : Writable {

    constructor(buf: ByteBuf) : this(ResourceKey.of(ResourceKeys.DIMENSION, buf.readKey()), buf.readVector())

    override fun write(buf: ByteBuf) {
        buf.writeKey(dimension.location)
        buf.writeVector(position)
    }

    companion object {

        @JvmField
        val CODEC: CompoundCodec<GlobalPosition> = CompoundCodec.of(
            {
                compound {
                    encode(Codecs.DIMENSION, "dimension", it.dimension)
                    encode(Codecs.VECTOR3I_ARRAY, "pos", it.position)
                }
            },
            { GlobalPosition(it.decode(Codecs.DIMENSION, "dimension")!!, it.decode(Codecs.VECTOR3I_ARRAY, "pos")!!) }
        )
    }
}
