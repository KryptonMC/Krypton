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
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.registry.network.RegistrySerialization
import org.kryptonmc.krypton.util.readIntIdList
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.readMap
import org.kryptonmc.krypton.util.writeIntIdList
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeMap

object TagSerializer {

    @JvmStatic
    fun serializeTagsToNetwork(dynamicHolder: RegistryHolder): Map<ResourceKey<out Registry<*>>, NetworkPayload> {
        return RegistrySerialization.networkSafeRegistries(dynamicHolder).registries.asSequence()
            .map { Pair(it.key, serializeToNetwork(it as KryptonRegistry<*>)) }
            .filter { !it.second.isEmpty() }
            .associate { it.first to it.second }
    }

    @JvmStatic
    private fun <T> serializeToNetwork(registry: KryptonRegistry<T>): NetworkPayload {
        val tags = HashMap<Key, IntList>()
        registry.tagEntries().forEach { (key, set) ->
            val ids = IntArrayList(set.size())
            set.forEach {
                if (it.kind() != Holder.Kind.REFERENCE) error("Cannot serialize unregistered tag value $it!")
                ids.add(registry.getId(it.value()))
            }
            tags.put(key.location, ids)
        }
        return NetworkPayload(tags)
    }

    @JvmRecord
    data class NetworkPayload(val tags: Map<Key, IntList>) : Writable {

        constructor(buf: ByteBuf) : this(buf.readMap(ByteBuf::readKey, ByteBuf::readIntIdList))

        fun isEmpty(): Boolean = tags.isEmpty()

        override fun write(buf: ByteBuf) {
            buf.writeMap(tags, ByteBuf::writeKey, ByteBuf::writeIntIdList)
        }
    }
}
