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
import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readById
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeVarInt
import org.spongepowered.math.vector.Vector3d

@JvmRecord
data class PacketOutSoundEffect(
    val event: SoundEvent,
    val source: Sound.Source,
    val x: Int,
    val y: Int,
    val z: Int,
    val volume: Float,
    val pitch: Float
) : Packet {

    constructor(
        event: SoundEvent,
        source: Sound.Source,
        x: Double,
        y: Double,
        z: Double,
        volume: Float,
        pitch: Float
    ) : this(event, source, (x * 8.0).toInt(), (y * 8.0).toInt(), (z * 8.0).toInt(), volume, pitch)

    constructor(
        event: SoundEvent,
        source: Sound.Source,
        location: Vector3d,
        volume: Float,
        pitch: Float
    ) : this(event, source, location.x(), location.y(), location.z(), volume, pitch)

    constructor(
        sound: Sound,
        event: SoundEvent,
        x: Double,
        y: Double,
        z: Double
    ) : this(event, sound.source(), x, y, z, sound.volume(), sound.pitch())

    constructor(sound: Sound, event: SoundEvent, location: Vector3d) : this(sound, event, location.x(), location.y(), location.z())

    constructor(buf: ByteBuf) : this(buf.readEvent(), buf.readEnum(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat())

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(Registries.SOUND_EVENT.idOf(event))
        buf.writeEnum(source)
        buf.writeInt(x)
        buf.writeInt(y)
        buf.writeInt(z)
        buf.writeFloat(volume)
        buf.writeFloat(pitch)
    }
}

private fun ByteBuf.readEvent(): SoundEvent = readById(Registries.SOUND_EVENT)!!
