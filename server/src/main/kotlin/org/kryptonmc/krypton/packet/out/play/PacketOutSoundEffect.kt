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
import org.kryptonmc.api.effect.sound.SoundType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.space.Position
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutSoundEffect(
    private val sound: Sound,
    private val type: SoundType,
    private val location: Position
) : PlayPacket(0x5B) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(Registries.SOUND_EVENT.idOf(type))
        buf.writeVarInt(sound.source().ordinal)
        buf.writeInt((location.x * 8.0).toInt())
        buf.writeInt((location.y * 8.0).toInt())
        buf.writeInt((location.z * 8.0).toInt())
        buf.writeFloat(sound.volume())
        buf.writeFloat(sound.pitch())
    }
}
