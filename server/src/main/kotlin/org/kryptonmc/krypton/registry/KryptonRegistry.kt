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
package org.kryptonmc.krypton.registry

import com.google.common.collect.HashBiMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.IdentityHashStrategy
import org.kryptonmc.krypton.util.IntBiMap
import org.kryptonmc.krypton.util.mapSuccess
import org.kryptonmc.krypton.util.orElseError
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound
import org.kryptonmc.nbt.list
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.Encoder
import org.kryptonmc.serialization.nbt.NbtOps
import java.util.Optional
import kotlin.math.max

open class KryptonRegistry<T>(override val key: ResourceKey<out Registry<T>>) : Registry<T>, IntBiMap<T> {

    private val byId = ObjectArrayList<T>(256)
    private val toId = Object2IntOpenCustomHashMap(IdentityHashStrategy.get<T>())
    private val storage = HashBiMap.create<Key, T>()
    private val keyStorage = HashBiMap.create<ResourceKey<T>, T>()
    private var nextId = 0

    override val keySet: Set<Key>
        get() = storage.keys
    override val entries: Set<Map.Entry<ResourceKey<T>, T>>
        get() = keyStorage.entries
    override val keys: Set<ResourceKey<T>>
        get() = keyStorage.keys
    override val values: Collection<T>
        get() = storage.values
    override val size: Int
        get() = storage.size

    override fun contains(key: Key): Boolean = storage.containsKey(key)

    fun contains(id: Int): Boolean = id >= 0 && id < byId.size

    override fun containsKey(key: ResourceKey<T>): Boolean = keyStorage.containsKey(key)

    override fun containsValue(value: T): Boolean = storage.containsValue(value)

    override fun get(key: Key): T? = getNullable(key)

    override fun get(id: Int): T? = byId.getOrNull(id)

    override fun get(value: T): Key? = storage.inverse().get(value)

    override fun get(key: ResourceKey<T>): T? = keyStorage.get(key)

    private fun getNullable(key: Key): T? = storage.get(key)

    override fun resourceKey(value: T): ResourceKey<T>? = keyStorage.inverse().get(value)

    private fun getResourceKey(value: T): Optional<ResourceKey<T>> = Optional.ofNullable(keyStorage.inverse().get(value))

    override fun idOf(value: T): Int = toId.getInt(value)

    override fun <V : T> register(key: ResourceKey<T>, value: V): V = register(nextId, key, value)

    override fun <V : T> register(key: Key, value: V): V = register(ResourceKey.of(this.key, key), value)

    fun <V : T> register(id: Int, key: Key, value: V): V = register(id, ResourceKey.of(this.key, key), value)

    protected open fun <V : T> register(id: Int, key: ResourceKey<T>, value: V): V {
        byId.size(max(byId.size, id + 1))
        byId.set(id, value)
        toId.put(value, id)
        storage.put(key.location, value)
        keyStorage.put(key, value)
        if (nextId <= id) nextId = id + 1
        return value
    }

    override fun isEmpty(): Boolean = storage.isEmpty()

    override fun iterator(): Iterator<T> = storage.values.iterator()

    fun byNameCodec(): Codec<T> = Codecs.KEY.flatXmap(
        { Optional.ofNullable(get(it)).mapSuccess().orElseError("Unknown registry key $it in $key!") },
        { getResourceKey(it).map(ResourceKey<T>::location).mapSuccess().orElseError("Unknown registry element $it in $key!") }
    )

    fun encode(elementEncoder: Encoder<T>): CompoundTag = compound {
        putString("type", key.location.asString())
        putList("value", CompoundTag.ID, values.map {
            compound {
                putString("name", get(it)!!.asString())
                putInt("id", idOf(it))
                put("element", elementEncoder.encodeStart(it, NbtOps.INSTANCE).result().get())
            }
        })
    }
}
