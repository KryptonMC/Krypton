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
package org.kryptonmc.krypton.event

import org.kryptonmc.api.event.Event
import org.kryptonmc.api.event.Listener
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object EventMethodScanner {

    @JvmStatic
    fun <T : Event> collectMethods(targetClass: Class<*>, eventType: Class<T>): Map<String, EventListenerMethod<T>> {
        val collected = HashMap<String, EventListenerMethod<T>>()
        collectMethods(targetClass, eventType, collected)
        return collected
    }

    @JvmStatic
    private fun <T : Event> collectMethods(targetClass: Class<*>, eventType: Class<T>, collected: MutableMap<String, EventListenerMethod<T>>) {
        for (method in targetClass.declaredMethods) {
            if (!method.isAnnotationPresent(Listener::class.java)) continue

            val key = "${method.name}(${method.parameterTypes.joinToString(",") { it.name }})"
            if (collected.containsKey(key)) continue

            if (Modifier.isStatic(method.modifiers)) error(method, targetClass, "must not be static")
            if (Modifier.isAbstract(method.modifiers)) error(method, targetClass, "must not be abstract")

            val parameterCount = method.parameterCount
            if (parameterCount != 1) error(method, targetClass, "must have exactly one parameter")

            val parameterEventType = method.parameterTypes[0]
            if (!eventType.isAssignableFrom(parameterEventType)) error(method, targetClass, "must have an event parameter")

            @Suppress("UNCHECKED_CAST")
            parameterEventType as Class<T>
            collected.put(key, EventListenerMethod(method, parameterEventType))
        }

        val superclass = targetClass.superclass
        if (superclass != Any::class.java) collectMethods(superclass, eventType, collected)
    }

    @JvmStatic
    private fun error(method: Method, targetClass: Class<*>, message: String) {
        throw IllegalArgumentException("Invalid listener method ${method.name} in ${targetClass.name}: $message!")
    }

    @JvmRecord
    data class EventListenerMethod<T : Event>(val method: Method, val eventType: Class<T>)
}
