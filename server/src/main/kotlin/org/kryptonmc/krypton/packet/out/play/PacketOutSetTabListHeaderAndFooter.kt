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
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.writeComponent

/**
 * This only exists in the protocol for use in modded servers. It is never used by the official vanilla
 * server. How nice of Mojang to do that for us :)
 *
 * Informs the client of the component to display above ([header] of the list) or below ([footer] of the list)
 * the player list.
 */
@JvmRecord
data class PacketOutSetTabListHeaderAndFooter(val header: Component, val footer: Component) : Packet {

    constructor(buf: ByteBuf) : this(buf.readComponent(), buf.readComponent())

    override fun write(buf: ByteBuf) {
        buf.writeComponent(header)
        buf.writeComponent(footer)
    }
}
