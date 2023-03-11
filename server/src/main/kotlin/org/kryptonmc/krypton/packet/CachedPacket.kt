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
