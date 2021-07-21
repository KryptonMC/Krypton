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
package org.kryptonmc.krypton.resource

import com.google.gson.JsonElement
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.DataResult
import com.mojang.serialization.Decoder
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Encoder
import com.mojang.serialization.JsonOps
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap
import me.bardy.gsonkt.parseToJson
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.RegistryHolder
import org.kryptonmc.krypton.registry.ops.RegistryReadOps
import org.kryptonmc.krypton.registry.ops.RegistryWriteOps
import org.kryptonmc.krypton.util.identityStrategy
import org.kryptonmc.krypton.util.logger
import java.io.IOException
import java.util.IdentityHashMap
import java.util.Optional
import java.util.OptionalInt

interface Resources {

    fun list(key: ResourceKey<out Registry<*>>): Collection<Key>

    fun <E : Any> parseJson(
        ops: DynamicOps<JsonElement>,
        registryKey: ResourceKey<out Registry<E>>,
        key: ResourceKey<E>,
        decoder: Decoder<E>
    ): Optional<DataResult<Pair<E, OptionalInt>>>

    companion object {

        private const val JSON = ".json"

        fun forManager(manager: ResourceManager) = object : Resources {

            override fun list(key: ResourceKey<out Registry<*>>) = manager.list(key.location.value()) { it.endsWith(JSON) }

            override fun <E : Any> parseJson(ops: DynamicOps<JsonElement>, registryKey: ResourceKey<out Registry<E>>, key: ResourceKey<E>, decoder: Decoder<E>): Optional<DataResult<Pair<E, OptionalInt>>> {
                val location = key.location
                val resourceKey = Key.key(location.namespace(), "${registryKey.location.value()}/${location.value()}$JSON")
                if (resourceKey !in manager) return Optional.empty()
                try {
                    manager(resourceKey).use { resource ->
                        resource.inputStream.reader().use { reader ->
                            val json = reader.parseToJson()
                            return Optional.of(decoder.parse(ops, json).map { Pair.of(it, OptionalInt.empty()) })
                        }
                    }
                } catch (exception: Exception) {
                    if (exception !is JsonIOException && exception !is JsonSyntaxException && exception !is IOException) throw exception
                    return Optional.of(DataResult.error("Failed to parse $resourceKey file: ${exception.message}"))
                }
            }

            override fun toString() = "Resources($manager)"
        }
    }
}

class MemoryResources : Resources {

    private val data = IdentityHashMap<ResourceKey<*>, JsonElement>()
    private val ids = Object2IntOpenCustomHashMap<ResourceKey<*>>(identityStrategy())

    fun <E : Any> add(holder: RegistryHolder, key: ResourceKey<E>, encoder: Encoder<E>, id: Int, value: E) {
        val result = encoder.encodeStart(RegistryWriteOps(JsonOps.INSTANCE, holder), value)
        if (result.error().isPresent) {
            LOGGER.error("Error adding element: ${result.error().get().message()}")
            return
        }
        data[key] = result.result().get()
        ids[key] = id
    }

    override fun list(key: ResourceKey<out Registry<*>>) = data.keys.asSequence()
        .filter { it.registry == key.location }
        .map { Key.key(it.location.namespace(), "${key.location.value()}/${it.location.value()}.json") }
        .toList()

    override fun <E : Any> parseJson(
        ops: DynamicOps<JsonElement>,
        registryKey: ResourceKey<out Registry<E>>,
        key: ResourceKey<E>,
        decoder: Decoder<E>
    ) = Optional.of(data[key]?.let { decoder.parse(ops, it) }?.map { Pair.of(it, OptionalInt.of(ids.getInt(key))) } ?: DataResult.error("Unknown element $key!"))

    companion object {

        private val LOGGER = logger<RegistryReadOps<*>>()
    }
}
