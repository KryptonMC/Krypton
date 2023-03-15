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

import org.kryptonmc.api.effect.particle.data.VibrationParticleData
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter

@JvmRecord
data class KryptonVibrationParticleData(override val destination: Vec3d, override val ticks: Int) : VibrationParticleData, Writable {

    constructor(reader: BinaryReader) : this(Vec3d(reader.readDouble(), reader.readDouble(), reader.readDouble()), reader.readInt())

    override fun write(writer: BinaryWriter) {
        // TODO: Sort this out when we have a new position source mechanism
        writer.writeDouble(destination.x)
        writer.writeDouble(destination.y)
        writer.writeDouble(destination.z)
        writer.writeInt(ticks)
    }
}
