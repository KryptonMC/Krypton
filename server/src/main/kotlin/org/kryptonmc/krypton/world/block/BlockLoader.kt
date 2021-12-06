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

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.PushReaction
import org.kryptonmc.api.block.RenderShape
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.util.IntHashBiMap
import org.kryptonmc.krypton.util.KryptonDataLoader
import org.kryptonmc.krypton.world.block.property.KryptonPropertyFactory
import java.util.concurrent.ConcurrentHashMap

object BlockLoader : KryptonDataLoader("blocks") {

    private val KEY_MAP = mutableMapOf<String, KryptonBlock>()
    private val PROPERTY_MAP = mutableMapOf<String, PropertyEntry>()
    val STATES = IntHashBiMap<Block>()

    fun fromKey(key: String): KryptonBlock? {
        val id = if (key.indexOf(':') == -1) "minecraft:$key" else key
        return KEY_MAP[id]
    }

    fun fromKey(key: Key): KryptonBlock? = fromKey(key.asString())

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
            val availableProperties = ImmutableSet.copyOf(value["properties"].asJsonArray.mapTo(mutableSetOf()) { element ->
                val string = element.asString.let { if (it == "LEVEL") "LEVEL_FLOWING" else it }
                KryptonPropertyFactory.PROPERTIES[string]!!
            })

            // Iterate states
            value.remove("states").asJsonArray.forEach {
                val (properties, block) = it.asJsonObject.retrieveState(key, availableProperties, value)
                propertyEntry.properties[properties] = block
            }

            // Get default state and add to map
            val defaultState = value["defaultStateId"].asInt
            val defaultBlock = STATES[defaultState]!! as KryptonBlock
            KEY_MAP[key] = defaultBlock
            PROPERTY_MAP[key] = propertyEntry

            // Register to registry
            val namespacedKey = Key.key(key)
            if (InternalRegistries.BLOCK.contains(namespacedKey)) return@forEach
            KryptonRegistryManager.register(InternalRegistries.BLOCK, defaultBlock.id, key, defaultBlock)
        }
    }

    private fun JsonObject.retrieveState(
        key: String,
        availableProperties: Set<Property<*>>,
        blockObject: JsonObject
    ): Pair<Map<String, String>, KryptonBlock> {
        val stateId = get("stateId").asInt
        val propertyMap = ImmutableMap.copyOf(get("properties").asJsonObject.entrySet().associate {
            it.key to it.value.asString.lowercase()
        })
        val block = createBlock(Key.key(key), blockObject, this, availableProperties, propertyMap)
        STATES[block] = stateId
        return propertyMap to block
    }

    private fun createBlock(
        key: Key,
        block: JsonObject,
        state: JsonObject,
        availableProperties: Set<Property<*>>,
        propertyMap: Map<String, String>
    ): KryptonBlock = KryptonBlock(
        key,
        block["id"].asInt,
        state["stateId"].asInt,
        state["hardness"].asDouble,
        block["explosionResistance"].asDouble,
        block["friction"].asDouble,
        block["speedFactor"].asDouble,
        block["jumpFactor"].asDouble,
        state["air"].asBoolean,
        state["solid"].asBoolean,
        state["liquid"].asBoolean,
        state["solidBlocking"].asBoolean,
        block["blockEntity"].asBoolean,
        state["lightEmission"].asInt,
        state["occludes"].asBoolean,
        state["blocksMotion"].asBoolean,
        state["flammable"].asBoolean,
        block["gravity"].asBoolean,
        Component.translatable(block["translationKey"].asString),
        state["replaceable"].asBoolean,
        block["dynamicShape"].asBoolean,
        state["useShapeForLightOcclusion"].asBoolean,
        state["propagatesSkylightDown"].asBoolean,
        state["lightBlock"].asInt,
        state["conditionallyFullyOpaque"].asBoolean,
        state["solidRender"].asBoolean,
        state["opacity"].asInt,
        state["largeCollisionShape"].asBoolean,
        state["collisionShapeFullBlock"].asBoolean,
        block["canRespawnIn"].asBoolean,
        state["toolRequired"].asBoolean,
        state["renderShape"]?.asString?.let {
            if (it == "ENTITYBLOCK_ANIMATED") RenderShape.ANIMATED_ENTITY_BLOCK else RenderShape.valueOf(it)
        } ?: RenderShape.MODEL,
        PushReaction.valueOf(state["pushReaction"].asString),
        block["correspondingItem"]?.asString?.let { Key.key(it) },
        Key.key(state["fluidState"].asString),
        availableProperties,
        propertyMap
    )

    private class PropertyEntry {

        val properties = ConcurrentHashMap<Map<String, String>, KryptonBlock>()
    }
}
