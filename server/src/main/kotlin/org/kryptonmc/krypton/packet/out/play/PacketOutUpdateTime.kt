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
