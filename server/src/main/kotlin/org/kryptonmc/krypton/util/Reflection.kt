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

    @PublishedApi
    internal val LOGGER = logger<Reflection>()

    @JvmStatic
    inline fun <reified T, R> accessField(name: String, instance: Any? = null): R? {
        return try {
            getField<T>(name)?.get(instance) as? R
        } catch (exception: IllegalArgumentException) {
            LOGGER.warn("Attempted to get the value of field $name of class ${T::class.java.canonicalName} on invalid instance $instance")
            null
        }
    }

    @JvmStatic
    inline fun <reified T> modifyField(name: String, instance: Any?, value: Any) {
        try {
            getField<T>(name)?.set(instance, value)
        } catch (exception: IllegalArgumentException) {
            LOGGER.warn("Attempted to set the value of field $name of class ${T::class.java.canonicalName} on invalid instance $instance")
        } catch (exception: IllegalAccessException) {
            LOGGER.warn("Attempted to set the value of field $name of class ${T::class.java.canonicalName} that is static and final")
        }
    }

    @JvmStatic
    inline fun <reified T> modifyField(name: String, value: Any) {
        modifyField<T>(name, null, value)
    }

    @JvmStatic
    inline fun <reified T> getField(name: String): Field? {
        return try {
            T::class.java.getDeclaredField(name).apply { isAccessible = true }
        } catch (exception: NoSuchFieldException) {
            LOGGER.warn("Attempted to access non-existent field $name on class ${T::class.java.canonicalName}.")
            null
        }
    }
}
