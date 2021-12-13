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
import io.netty.util.internal.ThreadLocalRandom
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.network.Writable

fun ParticleEffect.write(buf: ByteBuf, x: Double, y: Double, z: Double) {
    buf.writeInt(Registries.PARTICLE_TYPE.idOf(type))
    buf.writeBoolean(longDistance)

    /*
     * Write location. If the particle is directional, colorable, or a note, then we need
     * to manually apply the offsets first.
     *
     * The way this even works is really hacky. What we do is write the data in
     * the offsets. This goes all the way back to old versions of Minecraft,
     * and somehow still works in the newest versions.
     */
    val data = data
    when (data) {
        is DirectionalParticleData -> {
            writeOffsetPosition(buf, x, y, z)
            val random = ThreadLocalRandom.current()
            val directionX = data.direction?.x() ?: random.nextGaussian()
            val directionY = data.direction?.y() ?: random.nextGaussian()
            val directionZ = data.direction?.z() ?: random.nextGaussian()
            buf.writeOffset(directionX.toFloat(), directionY.toFloat(), directionZ.toFloat())
            buf.writeFloat(data.velocity)
            buf.writeInt(0)
        }
        is ColorParticleData -> {
            writeOffsetPosition(buf, x, y, z)
            buf.writeOffset(data.red.toFloat() / 255F, data.green.toFloat() / 255F, data.blue.toFloat() / 255F)
            buf.writeFloat(1F)
            buf.writeInt(0)
        }
        is NoteParticleData -> {
            writeOffsetPosition(buf, x, y, z)
            buf.writeOffset(data.note.toFloat() / 24F, 0F, 0F)
            buf.writeFloat(1F)
            buf.writeInt(0)
        }
        else -> {
            buf.writeDouble(x)
            buf.writeDouble(y)
            buf.writeDouble(z)
            buf.writeOffset(offset.x().toFloat(), offset.y().toFloat(), offset.z().toFloat())
            buf.writeFloat(1F)
            buf.writeInt(quantity)
        }
    }
    if (data is Writable) data.write(buf)
}

private fun ParticleEffect.writeOffsetPosition(buf: ByteBuf, x: Double, y: Double, z: Double) {
    val random = ThreadLocalRandom.current()
    buf.writeDouble(x + offset.x() * random.nextGaussian())
    buf.writeDouble(y + offset.y() * random.nextGaussian())
    buf.writeDouble(z + offset.z() * random.nextGaussian())
}

private fun ByteBuf.writeOffset(x: Float, y: Float, z: Float) {
    writeFloat(x)
    writeFloat(y)
    writeFloat(z)
}
