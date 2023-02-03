/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2018 Velocity Contributors
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
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/PaperMC/Velocity/blob/6be344d919020544466c23112c3672710ceffb30/proxy/src/main/java/com/velocitypowered/proxy/event/EventTypeTracker.java
 */
package org.kryptonmc.krypton.event

import com.google.common.collect.HashMultimap
import com.google.common.reflect.TypeToken
import kotlinx.collections.immutable.toImmutableSet

/**
 * This is a fix that Velocity implemented that solves the issue of the handlers
 * cache for the event manager not properly invalidating subclasses of event
 * types.
 *
 * For example, the following could be the case:
 * ```kotlin
 * open class MyBaseEvent
 *
 * class MyEvent : MyBaseEvent()
 *
 * class MyListener {
 *
 *     @Listener
 *     fun onEvent(event: MyEvent) {
 *         // do something
 *     }
 * }
 *
 * val listener = MyListener()
 * eventManager.register(myPlugin, listener) // Registers as normal
 * eventManager.unregisterListener(myPlugin, listener)
 * println(eventManager.handlerCache.asMap()) // {MyEvent=MyListener}
 * ```
 */
class EventTypeTracker {

    private val friends = HashMultimap.create<Class<*>, Class<*>>()

    fun getFriendsOf(eventType: Class<*>): Collection<Class<*>> {
        if (friends.containsKey(eventType)) return friends.get(eventType)
        val types = getEventTypes(eventType)
        types.forEach {
            if (it === eventType) return@forEach
            friends.put(it, eventType)
        }
        return types
    }

    companion object {

        @JvmStatic
        private fun getEventTypes(eventType: Class<*>): Collection<Class<*>> =
            TypeToken.of(eventType).types.rawTypes().filter { it !== Any::class.java }
    }
}
