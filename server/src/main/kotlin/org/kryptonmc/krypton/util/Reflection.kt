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
package org.kryptonmc.krypton.util

import java.lang.reflect.Field

object Reflection {

    private val LOGGER = logger<Reflection>()

    @JvmStatic
    inline fun <reified T, R> accessField(name: String, instance: Any? = null): R? = accessField(T::class.java, name, instance)

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <R> accessField(clazz: Class<*>, name: String, instance: Any? = null): R? {
        return try {
            getField(clazz, name)?.get(instance) as? R
        } catch (exception: IllegalArgumentException) {
            LOGGER.warn("Attempted to get the value of field $name of class ${clazz.canonicalName} on invalid instance $instance")
            null
        }
    }

    @JvmStatic
    fun modifyField(clazz: Class<*>, name: String, instance: Any?, value: Any) {
        try {
            getField(clazz, name)?.set(instance, value)
        } catch (_: IllegalArgumentException) {
            LOGGER.warn("Attempted to set the value of field $name of class ${clazz.canonicalName} on invalid instance $instance")
        } catch (_: IllegalAccessException) {
            LOGGER.warn("Attempted to set the value of field $name of class ${clazz.canonicalName} that is static and final")
        }
    }

    @JvmStatic
    fun modifyField(clazz: Class<*>, name: String, value: Any) {
        modifyField(clazz, name, null, value)
    }

    @JvmStatic
    private fun getField(clazz: Class<*>, name: String): Field? {
        return try {
            clazz.getDeclaredField(name).apply { isAccessible = true }
        } catch (_: NoSuchFieldException) {
            LOGGER.warn("Attempted to access non-existent field $name on class ${clazz.canonicalName}.")
            null
        }
    }
}
