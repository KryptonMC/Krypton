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
package org.kryptonmc.krypton.effect.particle

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.readById

@JvmRecord
data class ParticleOptions(val type: ParticleType, val data: ParticleData?) : Writable {

    constructor(buf: ByteBuf) : this(buf, buf.readById(Registries.PARTICLE_TYPE)!!)

    private constructor(buf: ByteBuf, type: ParticleType) : this(type, type.createData(buf))

    override fun write(buf: ByteBuf) {
        if (data != null && data is Writable) data.write(buf)
    }
}
