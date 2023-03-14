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
package org.kryptonmc.krypton.config.serializer

import org.spongepowered.configurate.serialize.ScalarSerializer
import org.spongepowered.configurate.serialize.SerializationException
import java.lang.reflect.Type
import java.util.function.Function
import java.util.function.IntFunction
import java.util.function.Predicate

class EnumTypeSerializer<E : Enum<E>>(
    type: Class<E>,
    private val typeName: String,
    private val fromId: IntFunction<E?>,
    private val fromName: Function<String, E?>
) : ScalarSerializer<E>(type) {

    override fun serialize(item: E, typeSupported: Predicate<Class<*>>?): Any = item.name.lowercase()

    override fun deserialize(type: Type, source: Any): E = when (source) {
        is Int -> fromId.apply(source) ?: throw SerializationException("$source is not a valid $typeName ID!")
        is String -> fromName.apply(source.lowercase()) ?: throw SerializationException("$source is not a valid $typeName name!")
        else -> throw SerializationException("Expected either an integer or a string for this $typeName, got ${source.javaClass.simpleName}!")
    }

    companion object {

        @JvmStatic
        inline fun <reified E : Enum<E>> of(
            typeName: String,
            crossinline fromId: (Int) -> E?,
            crossinline fromName: (String) -> E?
        ): EnumTypeSerializer<E> {
            return EnumTypeSerializer(E::class.java, typeName, { fromId(it) }, { fromName(it) })
        }
    }
}
