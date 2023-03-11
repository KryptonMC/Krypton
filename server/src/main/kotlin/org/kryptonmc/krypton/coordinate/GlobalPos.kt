/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.coordinate

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.readBlockPos
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.writeBlockPos
import org.kryptonmc.krypton.util.writeResourceKey
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class GlobalPos(val dimension: ResourceKey<World>, val position: Vec3i) : Writable {

    constructor(buf: ByteBuf) : this(ResourceKey.of(ResourceKeys.DIMENSION, buf.readKey()), buf.readBlockPos())

    override fun write(buf: ByteBuf) {
        buf.writeResourceKey(dimension)
        buf.writeBlockPos(position)
    }

    companion object {

        @JvmField
        val CODEC: Codec<GlobalPos> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codecs.DIMENSION.fieldOf("dimension").getting { it.dimension },
                BlockPos.CODEC.fieldOf("pos").getting { it.position }
            ).apply(instance) { dimension, pos -> GlobalPos(dimension, pos) }
        }
    }
}
