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
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.world.data.WorldData
import org.kryptonmc.krypton.world.rule.GameRuleKeys

@JvmRecord
data class PacketOutUpdateTime(val time: Long, val dayTime: Long) : Packet {

    constructor(buf: ByteBuf) : this(buf.readLong(), buf.readLong())

    override fun write(buf: ByteBuf) {
        buf.writeLong(time)
        buf.writeLong(dayTime)
    }

    companion object {

        @JvmStatic
        fun create(data: WorldData): PacketOutUpdateTime =
            PacketOutUpdateTime(data.time, calculateDayTime(data.dayTime, data.gameRules.getBoolean(GameRuleKeys.DO_DAYLIGHT_CYCLE)))

        @JvmStatic
        private fun calculateDayTime(dayTime: Long, doDaylightCycle: Boolean): Long {
            var time = dayTime
            if (!doDaylightCycle) {
                time = -dayTime
                if (time == 0L) time = -1L
            }
            return time
        }
    }
}
