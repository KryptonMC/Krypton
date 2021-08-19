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
package org.kryptonmc.krypton.world.fluid

import com.google.gson.JsonObject
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import me.bardy.gsonkt.fromJson
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.registry.data.FluidData
import org.kryptonmc.krypton.util.logger
import java.util.concurrent.ConcurrentHashMap

object FluidLoader {

    private val KEY_MAP = mutableMapOf<String, KryptonFluid>()
    private val PROPERTY_MAP = mutableMapOf<String, PropertyEntry>()
    private val STATE_MAP = Int2ObjectOpenHashMap<KryptonFluid>()

    private val LOGGER = logger<FluidLoader>()
    @JvmStatic @Volatile private var isLoaded = false

    fun init() {
        if (isLoaded) {
            LOGGER.warn("Attempted to load block loader twice!")
            return
        }
        val blocks = GSON.fromJson<JsonObject>(
            Thread.currentThread().contextClassLoader
                .getResourceAsStream("${KryptonPlatform.minecraftVersion.replace(".", "_")}_fluids.json")!!
                .reader()
        )
        blocks.entrySet().asSequence().map { it.key to it.value.asJsonObject }.forEach { (key, value) ->
            val propertyEntry = PropertyEntry()
            value.remove("states").asJsonArray.forEach {
                val (properties, block) = it.asJsonObject.retrieveState(key, value)
                propertyEntry.properties[properties] = block
            }
            val defaultState = value["defaultStateId"].asInt
            val defaultBlock = fromState(defaultState)!!
            KEY_MAP[key] = defaultBlock
            PROPERTY_MAP[key] = propertyEntry
            if (InternalRegistries.FLUID.contains(Key.key(key))) return@forEach
            KryptonRegistryManager.register(InternalRegistries.FLUID, key, defaultBlock)
        }
        isLoaded = true
    }

    private fun fromKey(key: String): KryptonFluid? {
        val id = if (key.indexOf(':') == -1) "minecraft:$key" else key
        return KEY_MAP[id]
    }

    fun fromKey(key: Key) = fromKey(key.asString())

    private fun fromState(stateId: Int): KryptonFluid? = STATE_MAP[stateId]

    fun properties(key: String, properties: Map<String, String>): KryptonFluid? = PROPERTY_MAP[key]?.properties?.get(properties)

    private fun JsonObject.retrieveState(key: String, blockObject: JsonObject): Pair<Map<String, String>, KryptonFluid> {
        val stateId = get("stateId").asInt
        val propertyMap = get("properties").asJsonObject.entrySet().associate { it.key to it.value.asString.lowercase() }
        val block = KryptonFluid(FluidData(Key.key(key), blockObject, this), propertyMap)
        STATE_MAP[stateId] = block
        return propertyMap to block
    }
}

private class PropertyEntry {

    val properties = ConcurrentHashMap<Map<String, String>, KryptonFluid>()
}
