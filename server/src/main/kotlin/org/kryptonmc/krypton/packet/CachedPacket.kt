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
package org.kryptonmc.krypton.packet

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.PacketFraming
import java.lang.ref.SoftReference
import java.util.function.Supplier

class CachedPacket(private val supplier: Supplier<Packet>) : GenericPacket {

    private var packet: SoftReference<CachedFramedPacket>? = null

    fun packet(): Packet {
        val cache = updatedCache()
        return cache?.packet ?: supplier.get()
    }

    fun body(): ByteBuf? {
        val cache = updatedCache()
        return cache?.body
    }

    fun invalidate() {
        packet = null
    }

    private fun updatedCache(): CachedFramedPacket? {
        val ref = packet
        var cached: CachedFramedPacket? = null
        if (ref == null || ref.get().also { cached = it } == null) {
            val updatedPacket = supplier.get()
            cached = CachedFramedPacket(updatedPacket, PacketFraming.frame(updatedPacket))
            packet = SoftReference(cached)
        }
        return cached
    }

    @JvmRecord
    private data class CachedFramedPacket(val packet: Packet, val body: ByteBuf)
}
