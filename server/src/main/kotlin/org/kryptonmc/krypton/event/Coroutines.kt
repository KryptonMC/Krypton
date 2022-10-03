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

import com.google.common.reflect.TypeToken
import org.kryptonmc.api.event.EventTask
import java.lang.reflect.Method
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.reflect.jvm.kotlinFunction
import org.kryptonmc.api.event.Continuation as EventContinuation

/**
 * This allows registering an extra handler adapter that supports coroutine
 * suspending functions as event handlers.
 */
object Coroutines {

    fun registerHandlerAdapter(manager: KryptonEventManager) {
        val filter = Predicate<Method> { it.kotlinFunction?.isSuspend == true }
        val validator = BiConsumer<Method, MutableList<String>> { method, errors ->
            val function = requireNotNull(method.kotlinFunction) { "Method was not Kotlin function despite being validated as one!" }
            // parameters includes receiver, but excludes continuation
            if (function.parameters.size != 2) errors.add("function must have a single parameter which is the event type")
            if (function.returnType.classifier != Unit::class) errors.add("function return type must be Unit")
        }
        val handlerBuilder = Function<CoroutineEventFunction, BiFunction<Any, Any, EventTask>> { invokeFunction ->
            BiFunction { instance, event -> suspendingEventTask { invokeFunction.execute(instance, event) } }
        }
        manager.registerHandlerAdapter("coroutines", object : TypeToken<CoroutineEventFunction>() {}, filter, validator, handlerBuilder)
    }

    private fun suspendingEventTask(handler: suspend () -> Unit): EventTask = EventTask.withContinuation {
        handler.startCoroutine(it.toCoroutines())
    }
}

private fun EventContinuation.toCoroutines(): Continuation<Unit> = Continuation(EmptyCoroutineContext) {
    if (it.isFailure) resumeWithException(it.exceptionOrNull()!!) else resume()
}
