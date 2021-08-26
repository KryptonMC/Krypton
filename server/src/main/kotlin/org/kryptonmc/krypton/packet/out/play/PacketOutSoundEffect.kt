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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.space.Position
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutSoundEffect(
    private val sound: Sound,
    private val event: SoundEvent,
    x: Double,
    y: Double,
    z: Double
) : Packet {

    private val x = (x * 8.0).toInt()
    private val y = (y * 8.0).toInt()
    private val z = (z * 8.0).toInt()

    constructor(sound: Sound, event: SoundEvent, position: Position) : this(sound, event, position.x, position.y, position.z)

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(InternalRegistries.SOUND_EVENT.idOf(event))
        buf.writeEnum(sound.source())
        buf.writeInt(x)
        buf.writeInt(y)
        buf.writeInt(z)
        buf.writeFloat(sound.volume())
        buf.writeFloat(sound.pitch())
    }
}
