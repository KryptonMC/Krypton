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
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeKey
import org.spongepowered.math.vector.Vector3d
import java.util.concurrent.ThreadLocalRandom

@JvmRecord
data class PacketOutCustomSoundEffect(
    val name: Key,
    val source: Sound.Source,
    val x: Double,
    val y: Double,
    val z: Double,
    val volume: Float,
    val pitch: Float,
    val seed: Long
) : Packet {

    constructor(
        name: Key,
        source: Sound.Source,
        location: Vector3d,
        volume: Float,
        pitch: Float,
        seed: Long
    ) : this(name, source, location.x(), location.y(), location.z(), volume, pitch, seed)

    // TODO: Review if we should use a better source of randomness - as in, for example, one tied to a world.
    constructor(
        sound: Sound,
        x: Double,
        y: Double,
        z: Double
    ) : this(sound.name(), sound.source(), x, y, z, sound.volume(), sound.pitch(), sound.seed().orElse(ThreadLocalRandom.current().nextLong()))

    constructor(sound: Sound, location: Vector3d) : this(sound, location.x(), location.y(), location.z())

    constructor(buf: ByteBuf) : this(
        buf.readKey(),
        buf.readEnum(),
        buf.readCoordinate(),
        buf.readCoordinate(),
        buf.readCoordinate(),
        buf.readFloat(),
        buf.readFloat(),
        buf.readLong()
    )

    override fun write(buf: ByteBuf) {
        buf.writeKey(name)
        buf.writeEnum(source)
        buf.writeInt((x * 8.0).toInt())
        buf.writeInt((y * 8.0).toInt())
        buf.writeInt((z * 8.0).toInt())
        buf.writeFloat(volume)
        buf.writeFloat(pitch)
    }
}

private fun ByteBuf.readCoordinate(): Double = readInt().toDouble() / 8.0
