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
