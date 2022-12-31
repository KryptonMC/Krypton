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
package org.kryptonmc.krypton.effect.particle.data

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.effect.particle.data.VibrationParticleData
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.coordinate.KryptonVec3d

@JvmRecord
data class KryptonVibrationParticleData(override val destination: Vec3d, override val ticks: Int) : VibrationParticleData, Writable {

    constructor(buf: ByteBuf) : this(KryptonVec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readInt())

    override fun write(buf: ByteBuf) {
        // TODO: Sort this out when we have a new position source mechanism
        buf.writeDouble(destination.x)
        buf.writeDouble(destination.y)
        buf.writeDouble(destination.z)
        buf.writeInt(ticks)
    }
}
