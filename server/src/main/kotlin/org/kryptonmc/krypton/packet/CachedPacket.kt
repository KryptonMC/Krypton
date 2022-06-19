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
import org.kryptonmc.krypton.util.frame
import java.lang.invoke.MethodHandles
import java.lang.ref.SoftReference
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater
import java.util.function.Supplier

@Suppress("UNCHECKED_CAST", "unused")
class CachedPacket(private val supplier: Supplier<Packet>) : GenericPacket {

    private var packet: SoftReference<FramedPacket>? = null

    fun get(): GenericPacket {
        val ref = PACKET.getAcquire(this) as? SoftReference<FramedPacket> ?: return supplier.get()
        return ref.get() ?: supplier.get()
    }

    fun body(): ByteBuf {
        val cache = updatedCache()
        return cache?.body ?: supplier.get().frame()
    }

    fun invalidate() {
        PACKET.setRelease(this, null)
    }

    private fun updatedCache(): FramedPacket? {
        val ref = PACKET.getAcquire(this) as? SoftReference<FramedPacket>
        var cached: FramedPacket? = null
        if (ref?.get() == null) {
            cached = FramedPacket(supplier.get().frame())
            PACKET.setRelease(this, SoftReference(cached))
        }
        return cached
    }

    companion object {

        @JvmStatic
        private val PACKET = MethodHandles.lookup().findVarHandle(CachedPacket::class.java, "packet", SoftReference::class.java)
    }
}
