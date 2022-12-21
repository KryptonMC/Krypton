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

import org.apache.logging.log4j.LogManager
import java.lang.reflect.Field

object Reflection {

    private val LOGGER = LogManager.getLogger()

    @JvmStatic
    inline fun <reified T, R> accessField(name: String, instance: Any? = null): R = accessField(T::class.java, name, instance)

    @JvmStatic
    fun <R> accessField(clazz: Class<*>, name: String, instance: Any? = null): R {
        return try {
            val value = getField(clazz, name).get(instance)
            @Suppress("UNCHECKED_CAST") // If this fails, it's on the client.
            value as R
        } catch (exception: Exception) {
            LOGGER.error("Error whilst trying to get the value of $name in ${clazz.canonicalName}!", exception)
            throw exception
        }
    }

    @JvmStatic
    fun modifyField(clazz: Class<*>, name: String, instance: Any?, value: Any) {
        try {
            getField(clazz, name).set(instance, value)
        } catch (exception: Exception) {
            LOGGER.error("Error whilst trying to set the value of $name in ${clazz.canonicalName}!", exception)
            throw exception
        }
    }

    @JvmStatic
    fun modifyStaticField(clazz: Class<*>, name: String, value: Any) {
        modifyField(clazz, name, null, value)
    }

    /**
     * Gets the field in the type with the name provided.
     *
     * Will propagate any exceptions from getDeclaredField or setAccessible.
     */
    @JvmStatic
    private fun getField(clazz: Class<*>, name: String): Field {
        try {
            return clazz.getDeclaredField(name).apply { isAccessible = true }
        } catch (exception: Exception) {
            LOGGER.error("Error whilst trying to get field $name in ${clazz.canonicalName}!", exception)
            throw exception
        }
    }
}
