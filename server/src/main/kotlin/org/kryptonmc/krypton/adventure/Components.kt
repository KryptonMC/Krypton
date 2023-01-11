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
