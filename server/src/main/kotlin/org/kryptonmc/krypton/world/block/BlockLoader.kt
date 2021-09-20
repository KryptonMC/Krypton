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
package org.kryptonmc.krypton.world.block

import com.google.gson.JsonObject
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.registry.data.BlockData
import org.kryptonmc.krypton.util.KryptonDataLoader
import org.kryptonmc.krypton.world.block.property.KryptonPropertyFactory
import java.util.concurrent.ConcurrentHashMap

object BlockLoader : KryptonDataLoader("blocks") {

    private val KEY_MAP = mutableMapOf<String, KryptonBlock>()
    private val PROPERTY_MAP = mutableMapOf<String, PropertyEntry>()
    private val STATE_MAP = Int2ObjectOpenHashMap<KryptonBlock>()

    fun fromKey(key: String): KryptonBlock? {
        val id = if (key.indexOf(':') == -1) "minecraft:$key" else key
        return KEY_MAP[id]
    }

    fun fromKey(key: Key) = fromKey(key.asString())

    fun properties(
        key: String,
        properties: Map<String, String>
    ): KryptonBlock? = PROPERTY_MAP[key]?.properties?.get(properties)

    override fun load(data: JsonObject) {
        KryptonPropertyFactory.bootstrap()
        data.entrySet().forEach { (key, value) ->
            value as JsonObject
            // Map properties
            val propertyEntry = PropertyEntry()
            val availableProperties = value["properties"].asJsonArray.mapTo(mutableSetOf()) { element ->
                val string = element.asString.let { if (it == "LEVEL") "LEVEL_FLOWING" else it }
                KryptonPropertyFactory.PROPERTIES[string]!!
            }

            // Iterate states
            value.remove("states").asJsonArray.forEach {
                val (properties, block) = it.asJsonObject.retrieveState(key, availableProperties, value)
                propertyEntry.properties[properties] = block
            }

            // Get default state and add to map
            val defaultState = value["defaultStateId"].asInt
            val defaultBlock = fromState(defaultState)!!
            KEY_MAP[key] = defaultBlock
            PROPERTY_MAP[key] = propertyEntry

            // Register to registry
            val namespacedKey = Key.key(key)
            if (InternalRegistries.BLOCK.contains(namespacedKey)) return@forEach
            KryptonRegistryManager.register(InternalRegistries.BLOCK, key, defaultBlock)
        }
        STATE_MAP.int2ObjectEntrySet().fastForEach { KryptonBlock.STATES[it.value] = it.intKey }
    }

    private fun fromState(stateId: Int): KryptonBlock? = STATE_MAP[stateId]

    private fun JsonObject.retrieveState(
        key: String,
        availableProperties: Set<Property<*>>,
        blockObject: JsonObject
    ): Pair<Map<String, String>, KryptonBlock> {
        val stateId = get("stateId").asInt
        val propertyMap = get("properties").asJsonObject.entrySet().associate {
            it.key to it.value.asString.lowercase()
        }
        val block = KryptonBlock(BlockData(Key.key(key), blockObject, this), availableProperties, propertyMap)
        STATE_MAP[stateId] = block
        return propertyMap to block
    }

    private class PropertyEntry {

        val properties = ConcurrentHashMap<Map<String, String>, KryptonBlock>()
    }
}
