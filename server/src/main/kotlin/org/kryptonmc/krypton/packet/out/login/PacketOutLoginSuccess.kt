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
