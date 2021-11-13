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
package org.kryptonmc.krypton.effect.particle

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.registry.InternalRegistries
import org.spongepowered.math.vector.Vector3d
import java.util.concurrent.ThreadLocalRandom

@JvmRecord
data class KryptonParticleEffect @JvmOverloads constructor(
    override val type: ParticleType,
    override val quantity: Int,
    override val offset: Vector3d,
    override val longDistance: Boolean,
    override val data: ParticleData? = null
) : ParticleEffect {

    init {
        require(quantity >= 1) { "Quantity must be >= 1!" }
    }

    fun write(buf: ByteBuf, x: Double, y: Double, z: Double) {
        buf.writeInt(InternalRegistries.PARTICLE_TYPE.idOf(type))
        buf.writeBoolean(longDistance)

        /*
         * Write location. If the particle is directional, colorable, or a note, then we need
         * to manually apply the offsets first.
         */
        when (data) {
            is DirectionalParticleData -> {
                writeOffsetPosition(buf, x, y, z)
                val random = ThreadLocalRandom.current()
                val directionX = data.direction?.x() ?: random.nextGaussian()
                val directionY = data.direction?.y() ?: random.nextGaussian()
                val directionZ = data.direction?.z() ?: random.nextGaussian()
                writeOffset(buf, directionX.toFloat(), directionY.toFloat(), directionZ.toFloat())
                buf.writeFloat(data.velocity)
                buf.writeInt(0)
            }
            is ColorParticleData -> {
                writeOffsetPosition(buf, x, y, z)
                writeOffset(buf, data.red.toFloat() / 255F, data.green.toFloat() / 255F, data.blue.toFloat() / 255F)
                buf.writeFloat(1F)
                buf.writeInt(0)
            }
            is NoteParticleData -> {
                writeOffsetPosition(buf, x, y, z)
                writeOffset(buf, data.note.toFloat() / 24F, 0F, 0F)
                buf.writeFloat(1F)
                buf.writeInt(0)
            }
            else -> {
                buf.writeDouble(x)
                buf.writeDouble(y)
                buf.writeDouble(z)
                writeOffset(buf, offset.x().toFloat(), offset.y().toFloat(), offset.z().toFloat())
                buf.writeFloat(1F)
                buf.writeInt(quantity)
            }
        }
        if (data is Writable) data.write(buf)
    }

    private fun writeOffsetPosition(buf: ByteBuf, x: Double, y: Double, z: Double) {
        val random = ThreadLocalRandom.current()
        buf.writeDouble(x + offset.x() * random.nextGaussian())
        buf.writeDouble(y + offset.y() * random.nextGaussian())
        buf.writeDouble(z + offset.z() * random.nextGaussian())
    }

    private fun writeOffset(buf: ByteBuf, x: Float, y: Float, z: Float) {
        buf.writeFloat(x)
        buf.writeFloat(y)
        buf.writeFloat(z)
    }

    object Factory : ParticleEffect.Factory {

        override fun of(
            type: ParticleType,
            quantity: Int,
            offset: Vector3d,
            longDistance: Boolean,
            data: ParticleData?
        ): ParticleEffect = KryptonParticleEffect(type, quantity, offset, longDistance, data)
    }
}
