/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
