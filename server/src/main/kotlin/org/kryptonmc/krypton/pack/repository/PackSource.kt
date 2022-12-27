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
package org.kryptonmc.krypton.pack.repository

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.function.UnaryOperator

interface PackSource {

    fun decorate(text: Component): Component

    fun shouldAddAutomatically(): Boolean

    companion object {

        @JvmField
        val DEFAULT: PackSource = create(UnaryOperator.identity(), true)
        @JvmField
        val BUILT_IN: PackSource = create(decorateWithSource("pack.source.builtin"), true)
        @JvmField
        val FEATURE: PackSource = create(decorateWithSource("pack.source.feature"), true)
        @JvmField
        val WORLD: PackSource = create(decorateWithSource("pack.source.world"), true)
        @JvmField
        val SERVER: PackSource = create(decorateWithSource("pack.source.server"), true)


        @JvmStatic
        fun create(function: UnaryOperator<Component>, addAutomatically: Boolean): PackSource = object : PackSource {
            override fun decorate(text: Component): Component = function.apply(text)

            override fun shouldAddAutomatically(): Boolean = addAutomatically
        }

        private fun decorateWithSource(key: String): UnaryOperator<Component> {
            val translation = Component.translatable(key)
            return UnaryOperator { Component.translatable("pack.nameAndSource", NamedTextColor.GRAY, it, translation) }
        }
    }
}
