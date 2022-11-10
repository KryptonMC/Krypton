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
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.packet.CachedPacket
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.tags.TagSerializer
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.readMap
import org.kryptonmc.krypton.util.writeMap
import org.kryptonmc.krypton.util.writeResourceKey

@JvmRecord
data class PacketOutUpdateTags(val tags: Map<ResourceKey<out Registry<*>>, TagSerializer.NetworkPayload>) : Packet {

    constructor(buf: ByteBuf) : this(buf.readMap({ ResourceKey.of(KryptonResourceKeys.PARENT, it.readKey()) }, TagSerializer::NetworkPayload))

    override fun write(buf: ByteBuf) {
        buf.writeMap(tags, ByteBuf::writeResourceKey) { _, payload -> payload.write(buf) }
    }

    companion object {

        @JvmField
        val CACHED: CachedPacket = CachedPacket { PacketOutUpdateTags(TagSerializer.serialize()) }
    }
}
