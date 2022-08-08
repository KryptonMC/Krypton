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
package org.kryptonmc.krypton.state.property

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.state.Property
import org.kryptonmc.krypton.world.block.downcast
import java.util.IdentityHashMap
import java.util.function.ToIntFunction

object KryptonPropertyFactory : Property.Factory {

    private val PROPERTIES: Map<String, KryptonProperty<*>> by lazy {
        val builtins = collectFieldProperties()
        val propertyUsages = HashMap<String, KryptonProperty<*>>()
        val propertyCount = Object2IntOpenHashMap<String>()
        Registries.BLOCK.values.forEach { block ->
            block.downcast().defaultState.availableProperties.forEach { property ->
                val name = builtins.computeIfAbsent(property) {
                    val count = propertyCount.computeIfAbsent(property.name, ToIntFunction { 0 }) + 1
                    "${property.name}_$count"
                }
                propertyUsages[name] = property
            }
        }
        propertyUsages
    }

    override fun forBoolean(name: String): Property<Boolean> = PROPERTIES[name] as BooleanProperty

    override fun forInt(name: String): Property<Int> = PROPERTIES[name] as IntProperty

    @Suppress("UNCHECKED_CAST")
    override fun <E : Enum<E>> forEnum(name: String): Property<E> = PROPERTIES[name] as EnumProperty<E>

    @JvmStatic
    private fun collectFieldProperties(): MutableMap<KryptonProperty<*>, String> {
        val map = IdentityHashMap<KryptonProperty<*>, String>()
        KryptonProperties::class.java.declaredFields.forEach { field ->
            try {
                val property = field.get(null)
                if (property is KryptonProperty<*>) map[property] = field.name
            } catch (exception: IllegalAccessException) {
                exception.printStackTrace()
            }
        }
        return map
    }
}
