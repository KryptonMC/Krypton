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
package org.kryptonmc.krypton.packet.out.play.sound

import io.netty.buffer.ByteBuf
import net.kyori.adventure.sound.SoundStop
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutStopSound(private val soundStop: SoundStop) : PlayPacket(0x52) {

    override fun write(buf: ByteBuf) {
        val source = soundStop.source()
        val sound = soundStop.sound()
        if (source != null) {
            if (sound != null) {
                buf.writeByte(3)
                buf.writeVarInt(source.ordinal)
                buf.writeKey(sound)
                return
            }
            buf.writeByte(1)
            buf.writeVarInt(source.ordinal)
            return
        }
        if (sound != null) {
            buf.writeByte(2)
            buf.writeKey(sound)
            return
        }
        buf.writeByte(0)
    }
}
