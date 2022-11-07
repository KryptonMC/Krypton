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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import io.netty.util.internal.ThreadLocalRandom
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.krypton.effect.particle.downcast
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeVarInt
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3f

/**
 * Tells the client to spawn some particles around it.
 */
@JvmRecord
data class PacketOutParticle(
    val typeId: Int,
    val longDistance: Boolean,
    val x: Double,
    val y: Double,
    val z: Double,
    val offsetX: Float,
    val offsetY: Float,
    val offsetZ: Float,
    val maxSpeed: Float,
    val count: Int,
    val data: ParticleData?
) : Packet {

    constructor(
        typeId: Int,
        longDistance: Boolean,
        location: Vector3d,
        offset: Vector3f,
        maxSpeed: Float,
        count: Int,
        data: ParticleData?
    ) : this(typeId, longDistance, location.x(), location.y(), location.z(), offset.x(), offset.y(), offset.z(), maxSpeed, count, data)

    constructor(buf: ByteBuf) : this(
        buf,
        buf.readVarInt(),
        buf.readBoolean(),
        buf.readDouble(),
        buf.readDouble(),
        buf.readDouble(),
        buf.readFloat(),
        buf.readFloat(),
        buf.readFloat(),
        buf.readFloat(),
        buf.readVarInt()
    )

    private constructor(
        buf: ByteBuf,
        typeId: Int,
        longDistance: Boolean,
        x: Double,
        y: Double,
        z: Double,
        offsetX: Float,
        offsetY: Float,
        offsetZ: Float,
        maxSpeed: Float,
        count: Int
    ) : this(typeId, longDistance, x, y, z, offsetX, offsetY, offsetZ, maxSpeed, count, readData(typeId, buf))

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(typeId)
        buf.writeBoolean(longDistance)
        buf.writeDouble(x)
        buf.writeDouble(y)
        buf.writeDouble(z)
        buf.writeFloat(offsetX)
        buf.writeFloat(offsetY)
        buf.writeFloat(offsetZ)
        buf.writeFloat(maxSpeed)
        buf.writeInt(count)
        if (data != null && data is Writable) data.write(buf)
    }

    companion object {

        private const val RGB_TO_FLOAT_FACTOR = 255F
        private const val NOTE_TO_FLOAT_FACTOR = 24F

        @JvmStatic
        fun from(effect: ParticleEffect, location: Vector3d): PacketOutParticle = from(effect, location.x(), location.y(), location.z())

        @JvmStatic
        fun from(effect: ParticleEffect, x: Double, y: Double, z: Double): PacketOutParticle {
            val typeId = KryptonRegistries.PARTICLE_TYPE.idOf(effect.type)
            var tempX = x
            var tempY = y
            var tempZ = z
            var offsetX = effect.offset.x().toFloat()
            var offsetY = effect.offset.y().toFloat()
            var offsetZ = effect.offset.z().toFloat()
            var maxSpeed = 1F
            var count = effect.quantity

            val data = effect.data
            val random = ThreadLocalRandom.current()

            /**
             * This may seem strange, but the randomness here is actually present with all particles.
             * The client applies this randomness to all particles with a count that isn't 0, which is
             * why we have to manually apply it server-side for certain particle types, as those types
             * have a count of 0, but we still want this randomness applied.
             */
            fun offsetPosition() {
                tempX = x + effect.offset.x() * random.nextGaussian()
                tempY = y + effect.offset.y() * random.nextGaussian()
                tempZ = z + effect.offset.z() * random.nextGaussian()
            }

            /*
             * Write location. If the particle is directional, colorable, or a note, then we need
             * to manually apply the offsets first.
             *
             * The way this even works is really hacky. What we do is write the data in
             * the offsets. This goes all the way back to old versions of Minecraft,
             * and somehow still works in the newest versions.
             *
             * Understanding some of this requires knowledge of how the notchian client actually
             * processes particle packets.
             */
            when (data) {
                is DirectionalParticleData -> {
                    offsetPosition()
                    offsetX = (data.direction?.x() ?: random.nextGaussian()).toFloat()
                    offsetY = (data.direction?.y() ?: random.nextGaussian()).toFloat()
                    offsetZ = (data.direction?.z() ?: random.nextGaussian()).toFloat()
                    maxSpeed = data.velocity
                    count = 0
                }
                is ColorParticleData -> {
                    offsetPosition()
                    offsetX = data.color.red.toFloat() / RGB_TO_FLOAT_FACTOR
                    offsetY = data.color.green.toFloat() / RGB_TO_FLOAT_FACTOR
                    offsetZ = data.color.blue.toFloat() / RGB_TO_FLOAT_FACTOR
                    count = 0
                }
                is NoteParticleData -> {
                    offsetPosition()
                    offsetX = data.note.toFloat() / NOTE_TO_FLOAT_FACTOR
                    offsetY = 0F
                    offsetZ = 0F
                    count = 0
                }
            }
            return PacketOutParticle(typeId, effect.longDistance, tempX, tempY, tempZ, offsetX, offsetY, offsetZ, maxSpeed, count, data)
        }

        @JvmStatic
        private fun readData(typeId: Int, buf: ByteBuf): ParticleData? = KryptonRegistries.PARTICLE_TYPE.get(typeId)!!.downcast().createData(buf)
    }
}
