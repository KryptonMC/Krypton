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
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.util.readById
import org.kryptonmc.krypton.util.readMap
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeId
import org.kryptonmc.krypton.util.writeMap
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutAwardStatistics(val statistics: Object2IntMap<Statistic<*>>) : Packet {

    constructor(buf: ByteBuf) : this(buf.readMap(::Object2IntOpenHashMap, ::readStatistic, ByteBuf::readVarInt))

    override fun write(buf: ByteBuf) {
        // The second argument can't be a method reference because of type inference.
        buf.writeMap(statistics, { buffer, key -> writeStatistic(buffer, key) }, ByteBuf::writeVarInt)
    }

    companion object {

        @JvmStatic
        private fun readStatistic(buf: ByteBuf): Statistic<*> = readStatistic(buf, buf.readById(KryptonRegistries.STATISTIC_TYPE)!!)

        @JvmStatic
        private fun <T> readStatistic(buf: ByteBuf, type: StatisticType<T>): Statistic<T> =
            type.getStatistic(buf.readById(type.registry as KryptonRegistry<T>)!!)

        @JvmStatic
        private fun <T> writeStatistic(buf: ByteBuf, statistic: Statistic<T>) {
            buf.writeId(KryptonRegistries.STATISTIC_TYPE, statistic.type)
            buf.writeId(statistic.type.registry as KryptonRegistry<T>, statistic.value)
        }
    }
}
