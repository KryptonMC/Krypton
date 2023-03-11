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
