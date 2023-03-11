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
package org.kryptonmc.krypton.adventure

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.function.Function

object Components {

    private val DEFAULT_SEPARATOR = Component.text(", ", NamedTextColor.GRAY)

    @JvmStatic
    fun wrapInSquareBrackets(component: Component): Component = Component.translatable("chat.square_brackets", component)

    @JvmStatic
    fun <T> formatToList(values: Collection<T>, formattedGetter: Function<T, Component>): Component =
        formatToList(values, DEFAULT_SEPARATOR, formattedGetter)

    @JvmStatic
    fun <T> formatToList(values: Collection<T>, separator: Component, formattedGetter: Function<T, Component>): Component {
        if (values.isEmpty()) return Component.empty()
        if (values.size == 1) return formattedGetter.apply(values.first())

        // More than one element and we append to an empty component so styles aren't overridden by each other.
        val result = Component.text()
        var first = true
        values.forEach { value ->
            if (!first) result.append(separator)
            result.append(formattedGetter.apply(value))
            first = false
        }
        return result.build()
    }
}
