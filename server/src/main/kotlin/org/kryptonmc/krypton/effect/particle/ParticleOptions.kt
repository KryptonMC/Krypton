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
package org.kryptonmc.krypton.effect.particle

import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.registry.KryptonRegistries

@JvmRecord
data class ParticleOptions(val type: ParticleType, val data: ParticleData?) : Writable {

    constructor(reader: BinaryReader) : this(reader, reader.readById(KryptonRegistries.PARTICLE_TYPE)!!)

    private constructor(reader: BinaryReader, type: ParticleType) : this(type, type.downcast().createData(reader))

    override fun write(writer: BinaryWriter) {
        if (data != null && data is Writable) data.write(writer)
    }
}
