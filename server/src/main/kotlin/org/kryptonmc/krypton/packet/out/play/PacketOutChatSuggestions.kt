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
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readList
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeString

@JvmRecord
data class PacketOutChatSuggestions(val action: Action, val entries: List<String>) : Packet {

    constructor(buf: ByteBuf) : this(buf.readEnum(), buf.readList(ByteBuf::readString))

    override fun write(buf: ByteBuf) {
        buf.writeEnum(action)
        buf.writeCollection(entries, buf::writeString)
    }

    enum class Action {

        ADD,
        REMOVE,
        SET
    }
}
