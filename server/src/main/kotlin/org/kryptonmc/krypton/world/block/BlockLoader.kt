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
package org.kryptonmc.krypton.world.block

import com.google.gson.JsonObject
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.collections.immutable.toPersistentHashMap
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.PushReaction
import org.kryptonmc.api.block.RenderShape
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.util.IntHashBiMap
import org.kryptonmc.krypton.util.KryptonDataLoader
import org.kryptonmc.krypton.world.block.property.KryptonPropertyFactory
import java.util.concurrent.ConcurrentHashMap

object BlockLoader : KryptonDataLoader<Block>("blocks", Registries.BLOCK) {

    private val KEY_MAP = mutableMapOf<String, KryptonBlock>()
    private val PROPERTY_MAP = mutableMapOf<String, PropertyEntry>()

    @JvmField
    val STATES: IntHashBiMap<Block> = IntHashBiMap()

    @JvmStatic
    fun fromKey(key: String): KryptonBlock? {
        val id = if (key.indexOf(':') == -1) "minecraft:$key" else key
        return KEY_MAP[id]
    }

    @JvmStatic
    fun fromKey(key: Key): KryptonBlock? = fromKey(key.asString())

    @JvmStatic
    fun properties(key: String, properties: Map<String, String>): KryptonBlock? = PROPERTY_MAP[key]?.properties?.get(properties)

    override fun preload() {
        KryptonPropertyFactory.bootstrap()
    }

    override fun create(key: Key, value: JsonObject): Block {
        // Map properties
        val propertyEntry = PropertyEntry()
        val availableProperties = value["properties"].asJsonArray.mapTo(mutableSetOf()) { element ->
            var string = element.asString
            if (string == "LEVEL") string = "LEVEL_FLOWING"
            KryptonPropertyFactory.PROPERTIES[string]!!
        }.toImmutableSet()

        // Iterate states
        value.remove("states").asJsonArray.forEach {
            val (properties, block) = it.asJsonObject.retrieveState(key.asString(), availableProperties, value)
            propertyEntry.properties[properties] = block
        }

        // Get default state and add to map
        val defaultState = value["defaultStateId"].asInt
        val defaultBlock = STATES[defaultState]!! as KryptonBlock
        KEY_MAP[key.asString()] = defaultBlock
        PROPERTY_MAP[key.asString()] = propertyEntry
        return defaultBlock
    }

    override fun register(key: Key, value: Block) {
        registry.register((value as KryptonBlock).id, key, value)
    }

    @JvmStatic
    private fun JsonObject.retrieveState(
        key: String,
        availableProperties: ImmutableSet<Property<*>>,
        blockObject: JsonObject
    ): Pair<Map<String, String>, KryptonBlock> {
        val stateId = get("stateId").asInt
        val properties = get("properties").asJsonObject.entrySet().associate { it.key to it.value.asString.lowercase() }.toPersistentHashMap()
        val block = createBlock(Key.key(key), blockObject, this, availableProperties, properties)
        STATES[block] = stateId
        return properties to block
    }

    @JvmStatic
    private fun createBlock(
        key: Key,
        block: JsonObject,
        state: JsonObject,
        availableProperties: ImmutableSet<Property<*>>,
        propertyMap: PersistentMap<String, String>
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
        convertRenderShape(state["renderShape"]?.asString),
        PushReaction.valueOf(state["pushReaction"].asString),
        block["correspondingItem"]?.asString?.let { Key.key(it) },
        Key.key(state["fluidState"].asString),
        availableProperties,
        propertyMap
    )

    @JvmStatic
    private fun convertRenderShape(shape: String?): RenderShape {
        if (shape == null) return RenderShape.MODEL
        if (shape == "ENTITYBLOCK_ANIMATED") return RenderShape.ANIMATED_ENTITY_BLOCK
        return RenderShape.valueOf(shape)
    }

    private class PropertyEntry {

        val properties: MutableMap<Map<String, String>, KryptonBlock> = ConcurrentHashMap()
    }
}
