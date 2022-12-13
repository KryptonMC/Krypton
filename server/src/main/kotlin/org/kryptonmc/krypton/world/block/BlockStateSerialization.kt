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
        val value = property.fromString(name)
        if (value != null) return state.set(property, value)
        LOGGER.warn("Unable to read property $name with value ${propertiesTag.getString(name)} for block state $blockStateTag!")
        return state
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <T : Comparable<T>> name(property: KryptonProperty<T>, value: Comparable<*>): String = property.toString(value as T)
}
