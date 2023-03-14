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
