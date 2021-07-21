/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

fun interface PackSource : (Component) -> Component {

    fun decorate(component: Component): Component

    override fun invoke(p1: Component) = decorate(p1)

    companion object {

        val DEFAULT = PackSource { it }
        val BUILT_IN = decorating("pack.source.builtin")
        val WORLD = decorating("pack.source.world")
        val SERVER = decorating("pack.source.server")

        private fun decorating(key: String): PackSource {
            val component = Component.translatable(key)
            return PackSource { Component.translatable("pack.nameAndSource", NamedTextColor.GRAY, it, component) }
        }
    }
}
