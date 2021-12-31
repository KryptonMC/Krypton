/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import java.lang.ref.SoftReference
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater
import java.util.function.Supplier

class CachedPacket(private val supplier: Supplier<Packet>) : GenericPacket {

    // 0 means that the reference needs to be updated
    // Anything else (currently 1) means that the packet is up-to-date
    @Volatile
    private var updated = 0
    private var packet: SoftReference<FramedPacket>? = null

    val body: ByteBuf
        get() {
            val cache = updatedCache() ?: return supplier.get().frame()
            return cache.body
        }

    fun get(): GenericPacket {
        if (updated != 1) return supplier.get()
        val cached = packet?.get()
        if (cached != null) return cached
        return supplier.get()
    }

    fun invalidate() {
        updated = 0
    }

    private fun updatedCache(): FramedPacket? {
        val reference = packet
        var cached: FramedPacket? = null
        if (updated == 0 || reference == null || reference.get() == null) {
            cached = FramedPacket(supplier.get().frame())
            packet = SoftReference(cached)
            UPDATER.compareAndSet(this, 0, 1)
        }
        return cached
    }

    companion object {

        @JvmStatic
        private val UPDATER = AtomicIntegerFieldUpdater.newUpdater(CachedPacket::class.java, "updated")
    }
}
