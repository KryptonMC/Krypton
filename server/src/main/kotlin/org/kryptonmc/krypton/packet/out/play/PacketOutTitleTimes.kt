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
data class PacketOutTitleTimes(val fadeInTicks: Int, val stayTicks: Int, val fadeOutTicks: Int) : Packet {

    constructor(times: Title.Times) : this(times.fadeIn(), times.stay(), times.fadeOut())

    constructor(fadeIn: Duration, stay: Duration, fadeOut: Duration) : this(fadeIn.toTicks(), stay.toTicks(), fadeOut.toTicks())

    override fun write(buf: ByteBuf) {
        buf.writeInt(fadeInTicks)
        buf.writeInt(stayTicks)
        buf.writeInt(fadeOutTicks)
    }
}

private fun Duration.toTicks(): Int = toSeconds().toInt() * 20
