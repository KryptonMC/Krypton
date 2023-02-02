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

import org.kryptonmc.api.state.Property

object KryptonPropertyFactory : Property.Factory {

    private var cachedProperties: Map<String, KryptonProperty<*>>? = null

    @JvmStatic
    fun findByName(name: String): KryptonProperty<*> = getAllProperties().get(name)!!

    override fun forBoolean(name: String): Property<Boolean> = findByName(name) as BooleanProperty

    override fun forInt(name: String): Property<Int> = findByName(name) as IntProperty

    @Suppress("UNCHECKED_CAST")
    override fun <E : Enum<E>> forEnum(name: String): Property<E> = findByName(name) as EnumProperty<E>

    @JvmStatic
    private fun collectFieldProperties(): Map<String, KryptonProperty<*>> {
        val map = HashMap<String, KryptonProperty<*>>()
        KryptonProperties::class.java.declaredFields.forEach { field ->
            try {
                val property = field.get(null)
                if (property is KryptonProperty<*>) map.put(field.name, property)
            } catch (exception: IllegalAccessException) {
                exception.printStackTrace()
            }
        }
        return map
    }

    @JvmStatic
    private fun getAllProperties(): Map<String, KryptonProperty<*>> {
        if (cachedProperties != null) return cachedProperties!!
        val builtins = collectFieldProperties()
        cachedProperties = builtins
        return builtins
    }
}
