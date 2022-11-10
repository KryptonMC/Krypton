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
package org.kryptonmc.krypton.tags

import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.IntList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.readIntIdList
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.readMap
import org.kryptonmc.krypton.util.writeIntIdList
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeMap

object TagSerializer {

    // FIXME: When we implement the registry access system, properly implement this
    @JvmStatic
    fun serialize(): Map<ResourceKey<out Registry<*>>, NetworkPayload> = emptyMap()

    /* FIXME: When we fix the above, uncomment this
    @JvmStatic
    private fun <T : Any> serializeTags(registry: KryptonRegistry<T>): NetworkPayload {
        val result = HashMap<Key, IntList>()
        registry.tags.forEach { (key, values) ->
            val ids = IntArrayList(values.size)
            values.forEach { ids.add(registry.getId(it)) }
            result.put(key.location, ids)
        }
        return NetworkPayload(result)
    }
     */

    @JvmRecord
    data class NetworkPayload(val tags: Map<Key, IntList>) : Writable {

        constructor(buf: ByteBuf) : this(buf.readMap(ByteBuf::readKey, ByteBuf::readIntIdList))

        fun isEmpty(): Boolean = tags.isEmpty()

        override fun write(buf: ByteBuf) {
            buf.writeMap(tags, ByteBuf::writeKey, ByteBuf::writeIntIdList)
        }
    }
}
