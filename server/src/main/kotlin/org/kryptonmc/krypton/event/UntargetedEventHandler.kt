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
 * https://github.com/PaperMC/Velocity/blob/1761755d4dfc16cd020aee90c48761d98552531b/proxy/src/main/java/com/velocitypowered/proxy/event/UntargetedEventHandler.java
 */
package org.kryptonmc.krypton.event

import org.kryptonmc.api.event.Continuation
import org.kryptonmc.api.event.EventHandler
import org.kryptonmc.api.event.EventTask

fun interface UntargetedEventHandler {

    fun createHandler(targetInstance: Any): EventHandler<Any>

    interface Task : UntargetedEventHandler {

        fun execute(targetInstance: Any, event: Any): EventTask?

        override fun createHandler(targetInstance: Any): EventHandler<Any> = EventHandler { execute(targetInstance, it) }
    }

    interface Void : UntargetedEventHandler {

        fun execute(targetInstance: Any, event: Any)

        override fun createHandler(targetInstance: Any): EventHandler<Any> = EventHandler {
            execute(targetInstance, it)
            null
        }
    }

    interface WithContinuation : UntargetedEventHandler {

        fun execute(targetInstance: Any, event: Any, continuation: Continuation)

        override fun createHandler(targetInstance: Any): EventHandler<Any> = EventHandler { event ->
            EventTask.withContinuation { execute(targetInstance, event, it) }
        }
    }
}
