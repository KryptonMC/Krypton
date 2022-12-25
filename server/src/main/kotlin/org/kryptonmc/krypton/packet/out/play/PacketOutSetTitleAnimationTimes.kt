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
import net.kyori.adventure.title.Title
import org.kryptonmc.krypton.packet.Packet
import java.time.Duration

@JvmRecord
data class PacketOutSetTitleAnimationTimes(val fadeInTicks: Int, val stayTicks: Int, val fadeOutTicks: Int) : Packet {

    constructor(buf: ByteBuf) : this(buf.readInt(), buf.readInt(), buf.readInt())

    override fun write(buf: ByteBuf) {
        buf.writeInt(fadeInTicks)
        buf.writeInt(stayTicks)
        buf.writeInt(fadeOutTicks)
    }

    companion object {

        private const val TICKS_PER_SECOND = 20

        @JvmStatic
        fun fromTimes(times: Title.Times): PacketOutSetTitleAnimationTimes =
            PacketOutSetTitleAnimationTimes(toTicks(times.fadeIn()), toTicks(times.stay()), toTicks(times.fadeOut()))

        @JvmStatic
        private fun toTicks(duration: Duration): Int = duration.toSeconds().toInt() * TICKS_PER_SECOND
    }
}
