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
package org.kryptonmc.krypton.packet.out.play

import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.effect.particle.downcast
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.registry.KryptonRegistries
import java.util.concurrent.ThreadLocalRandom

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

    constructor(reader: BinaryReader) : this(reader, reader.readVarInt(), reader.readBoolean(), reader.readDouble(), reader.readDouble(),
        reader.readDouble(), reader.readFloat(), reader.readFloat(), reader.readFloat(), reader.readFloat(), reader.readVarInt())

    private constructor(reader: BinaryReader, typeId: Int, longDistance: Boolean, x: Double, y: Double, z: Double, offsetX: Float, offsetY: Float,
                        offsetZ: Float, maxSpeed: Float,
                        count: Int) : this(typeId, longDistance, x, y, z, offsetX, offsetY, offsetZ, maxSpeed, count, readData(typeId, reader))

    override fun write(writer: BinaryWriter) {
        writer.writeVarInt(typeId)
        writer.writeBoolean(longDistance)
        writer.writeDouble(x)
        writer.writeDouble(y)
        writer.writeDouble(z)
        writer.writeFloat(offsetX)
        writer.writeFloat(offsetY)
        writer.writeFloat(offsetZ)
        writer.writeFloat(maxSpeed)
        writer.writeInt(count)
        if (data != null && data is Writable) data.write(writer)
    }

    companion object {

        private const val RGB_TO_FLOAT_FACTOR = 255F
        private const val NOTE_TO_FLOAT_FACTOR = 24F

        @JvmStatic
        fun fromEffect(effect: ParticleEffect, location: Vec3d): PacketOutParticle {
            val typeId = KryptonRegistries.PARTICLE_TYPE.getId(effect.type)
            var x = location.x
            var y = location.y
            var z = location.z
            var offsetX = effect.offset.x.toFloat()
            var offsetY = effect.offset.y.toFloat()
            var offsetZ = effect.offset.z.toFloat()
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
                x = location.x + effect.offset.x * random.nextGaussian()
                y = location.y + effect.offset.y * random.nextGaussian()
                z = location.z + effect.offset.z * random.nextGaussian()
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
                    offsetX = (data.direction?.x ?: random.nextGaussian()).toFloat()
                    offsetY = (data.direction?.y ?: random.nextGaussian()).toFloat()
                    offsetZ = (data.direction?.z ?: random.nextGaussian()).toFloat()
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
            return PacketOutParticle(typeId, effect.longDistance, x, y, z, offsetX, offsetY, offsetZ, maxSpeed, count, data)
        }

        @JvmStatic
        private fun readData(typeId: Int, reader: BinaryReader): ParticleData? {
            return KryptonRegistries.PARTICLE_TYPE.get(typeId)!!.downcast().createData(reader)
        }
    }
}
