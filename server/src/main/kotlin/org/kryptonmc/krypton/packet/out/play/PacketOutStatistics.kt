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
import it.unimi.dsi.fastutil.objects.Object2IntMap
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.writeMap
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutStatistics(private val statistics: Object2IntMap<Statistic<*>>) : Packet {

    @Suppress("UNCHECKED_CAST")
    override fun write(buf: ByteBuf) {
        buf.writeMap(statistics, { _, key ->
            buf.writeVarInt(InternalRegistries.STATISTIC_TYPE.idOf(key.type))
            buf.writeVarInt((key.type as StatisticType<Any>).registry.idOf(key.value))
        }, ByteBuf::writeVarInt)
    }
}
