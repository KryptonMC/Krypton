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
