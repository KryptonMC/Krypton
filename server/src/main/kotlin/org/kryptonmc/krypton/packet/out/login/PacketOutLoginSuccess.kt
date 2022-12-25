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
package org.kryptonmc.krypton.packet.out.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readList
import org.kryptonmc.krypton.util.readProfileProperty
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeProfileProperty
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import java.util.UUID

/**
 * Sent to the client on successful login, to inform them of their own UUID.
 *
 * Not sure why we return their username to them though, as they already know
 * it because they told us it in login start.
 */
@JvmRecord
data class PacketOutLoginSuccess(val uuid: UUID, val username: String, val properties: List<ProfileProperty>) : Packet {

    constructor(buf: ByteBuf) : this(buf.readUUID(), buf.readString(), buf.readList { it.readProfileProperty() })

    override fun write(buf: ByteBuf) {
        buf.writeUUID(uuid)
        buf.writeString(username)
        buf.writeCollection(properties, buf::writeProfileProperty)
    }

    companion object {

        @JvmStatic
        fun create(profile: GameProfile): PacketOutLoginSuccess = PacketOutLoginSuccess(profile.uuid, profile.name, profile.properties)
    }
}
