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
package org.kryptonmc.krypton.effect.particle.data

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.effect.particle.data.VibrationParticleData
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.network.Writable

@JvmRecord
data class KryptonVibrationParticleData(override val destination: Vec3d, override val ticks: Int) : VibrationParticleData, Writable {

    constructor(buf: ByteBuf) : this(Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readInt())

    override fun write(buf: ByteBuf) {
        // TODO: Sort this out when we have a new position source mechanism
        buf.writeDouble(destination.x)
        buf.writeDouble(destination.y)
        buf.writeDouble(destination.z)
        buf.writeInt(ticks)
    }
}
