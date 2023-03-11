/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.block

import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.state.KryptonState
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.state.property.downcast
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound

object BlockStateSerialization {

    private const val NAME_TAG = "Name"
    private const val PROPERTIES_TAG = "Properties"
    private val LOGGER = LogManager.getLogger()

    @JvmStatic
    fun decode(data: CompoundTag): KryptonBlockState {
        if (!data.contains(NAME_TAG, StringTag.ID)) return KryptonBlocks.AIR.defaultState
        val block = KryptonRegistries.BLOCK.get(Key.key(data.getString(NAME_TAG)))
        var state = block.defaultState
        if (data.contains(PROPERTIES_TAG, CompoundTag.ID)) {
            val properties = data.getCompound(PROPERTIES_TAG)
            properties.keySet().forEach { property ->
                block.stateDefinition.getProperty(property)?.let { state = set(state, it, property, properties, data) }
            }
        }
        return state
    }

    @JvmStatic
    fun encode(state: KryptonBlockState): CompoundTag = compound {
        putString(NAME_TAG, KryptonRegistries.BLOCK.getKey(state.block).asString())
        if (state.properties.isEmpty()) return@compound
        compound(PROPERTIES_TAG) {
            state.properties.forEach {
                val property = it.key.downcast()
                putString(property.name, name(property, it.value))
            }
        }
    }

    @JvmStatic
    fun encode(data: CompoundTag.Builder, name: String, state: KryptonBlockState): CompoundTag.Builder = data.put(name, encode(state))

    @JvmStatic
    private fun <S : KryptonState<*, S>, T : Comparable<T>> set(state: S, property: KryptonProperty<T>, name: String, propertiesTag: CompoundTag,
                                                                blockStateTag: CompoundTag): S {
        val value = property.fromString(propertiesTag.getString(name))
        if (value != null) return state.setProperty(property, value)
        LOGGER.warn("Unable to read property $name with value ${propertiesTag.getString(name)} for block state $blockStateTag!")
        return state
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <T : Comparable<T>> name(property: KryptonProperty<T>, value: Comparable<*>): String = property.toString(value as T)
}
