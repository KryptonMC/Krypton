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
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.collections.immutable.toPersistentHashMap
import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.util.IntHashBiMap
import org.kryptonmc.krypton.util.KryptonDataLoader
import org.kryptonmc.krypton.world.block.property.KryptonPropertyFactory
import java.util.concurrent.ConcurrentHashMap

object FluidLoader : KryptonDataLoader("fluids") {

    private val KEY_MAP = mutableMapOf<String, KryptonFluid>()
    private val PROPERTY_MAP = mutableMapOf<String, PropertyEntry>()

    @JvmField
    val STATES: IntHashBiMap<KryptonFluid> = IntHashBiMap()

    @JvmStatic
    fun properties(
        key: String,
        properties: Map<String, String>
    ): KryptonFluid? = PROPERTY_MAP[key]?.properties?.get(properties)

    override fun load(data: JsonObject) {
        data.entrySet().asSequence().map { it.key to it.value.asJsonObject }.forEach { (key, value) ->
            // Map properties
            val propertyEntry = PropertyEntry()
            val availableProperties = value["properties"].asJsonArray.mapTo(mutableSetOf()) {
                KryptonPropertyFactory.PROPERTIES[it.asString]!!
            }.toImmutableSet()

            // Iterate states
            value.remove("states").asJsonArray.forEach {
                val (properties, fluid) = it.asJsonObject.retrieveState(key, availableProperties, value)
                propertyEntry.properties[properties] = fluid
            }

            // Get default state and add to maps
            val defaultState = value["defaultStateId"].asInt // FIXME: Update ArticData when this is always non-null
            val defaultFluid = STATES[defaultState]!!
            KEY_MAP[key] = defaultFluid
            PROPERTY_MAP[key] = propertyEntry

            // Register to registry
            if (Registries.FLUID.contains(Key.key(key))) return@forEach
            Registries.FLUID.register(key, defaultFluid)
        }
    }

    @JvmStatic
    private fun JsonObject.retrieveState(
        key: String,
        availableProperties: ImmutableSet<Property<*>>,
        fluidObject: JsonObject
    ): Pair<Map<String, String>, KryptonFluid> {
        val stateId = get("stateId").asInt
        val properties = get("properties").asJsonObject.entrySet().associate {
            it.key to it.value.asString.lowercase()
        }.toPersistentHashMap()
        val fluid = createFluid(Key.key(key), fluidObject, this, availableProperties, properties)
        STATES[fluid] = stateId
        return properties to fluid
    }

    @JvmStatic
    private fun createFluid(
        key: Key,
        fluid: JsonObject,
        state: JsonObject,
        availableProperties: ImmutableSet<Property<*>>,
        propertyMap: PersistentMap<String, String>
    ): KryptonFluid = KryptonFluid(
        key,
        fluid["id"].asInt,
        state["stateId"].asInt,
        Registries.ITEM[Key.key(fluid["bucketId"].asString)],
        fluid["empty"].asBoolean,
        fluid["explosionResistance"].asDouble,
        state["source"].asBoolean,
        state["ownHeight"].asFloat,
        state["amount"].asInt,
        Key.key(state["blockState"].asString),
        availableProperties,
        propertyMap
    )

    private class PropertyEntry {

        val properties: MutableMap<Map<String, String>, KryptonFluid> = ConcurrentHashMap()
    }
}
