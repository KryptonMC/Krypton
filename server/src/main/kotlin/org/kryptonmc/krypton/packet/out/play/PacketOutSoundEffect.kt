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
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.readById
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeId

@JvmRecord
data class PacketOutSoundEffect(
    val event: SoundEvent,
    val source: Sound.Source,
    val x: Int,
    val y: Int,
    val z: Int,
    val volume: Float,
    val pitch: Float,
    val seed: Long
) : Packet {

    constructor(event: SoundEvent, source: Sound.Source, x: Double, y: Double, z: Double, volume: Float, pitch: Float,
                seed: Long) : this(event, source, (x * 8.0).toInt(), (y * 8.0).toInt(), (z * 8.0).toInt(), volume, pitch, seed)

    constructor(sound: Sound, event: SoundEvent, x: Double, y: Double, z: Double,
                seed: Long) : this(event, sound.source(), x, y, z, sound.volume(), sound.pitch(), seed)

    constructor(buf: ByteBuf) : this(buf.readEvent(), buf.readEnum(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat(),
        buf.readLong())

    override fun write(buf: ByteBuf) {
        buf.writeId(KryptonRegistries.SOUND_EVENT, event)
        buf.writeEnum(source)
        buf.writeInt(x)
        buf.writeInt(y)
        buf.writeInt(z)
        buf.writeFloat(volume)
        buf.writeFloat(pitch)
        buf.writeLong(seed)
    }
}

private fun ByteBuf.readEvent(): SoundEvent = readById(KryptonRegistries.SOUND_EVENT)!!
