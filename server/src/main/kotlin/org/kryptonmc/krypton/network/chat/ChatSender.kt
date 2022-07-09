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
package org.kryptonmc.krypton.network.chat

import io.netty.buffer.ByteBuf
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeUUID
import java.util.UUID

@JvmRecord
data class ChatSender(val uuid: UUID, val name: Component, val teamName: Component? = null) : Writable {

    constructor(buf: ByteBuf) : this(buf.readUUID(), buf.readComponent(), buf.readNullable { buf.readComponent() })

    override fun write(buf: ByteBuf) {
        buf.writeUUID(uuid)
        buf.writeComponent(name)
        buf.writeNullable(teamName, ByteBuf::writeComponent)
    }

    companion object {

        @JvmStatic
        fun fromIdentity(identity: Identity): ChatSender = ChatSender(identity.uuid(), Component.empty(), null)
    }
}
