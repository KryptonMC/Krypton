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
 * https://github.com/PaperMC/Velocity/blob/1761755d4dfc16cd020aee90c48761d98552531b/proxy/src/main/java/com/velocitypowered/proxy/event/CustomHandlerAdapter.java
 */
package org.kryptonmc.krypton.event

import org.kryptonmc.api.event.EventHandler
import org.kryptonmc.api.event.EventTask
import org.lanternpowered.lmbda.LambdaFactory
import org.lanternpowered.lmbda.LambdaType
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate

class CustomHandlerAdapter<F : Any>(
    val name: String,
    private val handlerBuilder: Function<F, BiFunction<Any, Any, EventTask>>,
    val filter: Predicate<Method>,
    val validator: BiConsumer<Method, MutableList<String>>,
    private val functionType: LambdaType<F>,
    private val lookup: MethodHandles.Lookup
) {

    fun createUntargetedHandler(method: Method): UntargetedEventHandler {
        val handle = lookup.unreflect(method)
        val defineLookup = MethodHandles.privateLookupIn(method.declaringClass, lookup)
        val lambdaType = functionType.defineClassesWith(defineLookup)
        val invokeFunction = LambdaFactory.create(lambdaType, handle)
        val handlerFunction = handlerBuilder.apply(invokeFunction)
        return UntargetedEventHandler { targetInstance -> EventHandler { handlerFunction.apply(targetInstance, it) } }
    }
}
