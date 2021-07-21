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

import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.util.logger
import java.io.IOException

data class Pack(
    val id: String,
    val resources: () -> PackResources,
    val position: Position,
    val isRequired: Boolean,
    val source: PackSource,
    val isFixedPosition: Boolean = false
) {

    fun open() = resources()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return id == (other as Pack).id
    }

    override fun hashCode() = id.hashCode()

    enum class Position {

        TOP,
        BOTTOM;

        val opposite by lazy { if (this === TOP) BOTTOM else TOP }

        fun <T> insert(list: MutableList<T>, value: T, creator: (T) -> Pack, isOpposite: Boolean): Int = when (if (isOpposite) opposite else this) {
            BOTTOM -> {
                var i = 0
                while (i < list.size) {
                    val pack = creator(list[i])
                    if (!pack.isFixedPosition || pack.position != this) break
                    ++i
                }
                list.add(i, value)
                i
            }
            TOP -> {
                var i = list.size - 1
                while (i >= 0) {
                    val pack = creator(list[i])
                    if (!pack.isFixedPosition || pack.position != this) break
                    --i
                }
                list.add(i + 1, value)
                i
            }
        }
    }

    companion object {

        private val LOGGER = logger<Pack>()

        fun of(id: String, resources: () -> PackResources, position: Position, required: Boolean, source: PackSource, constructor: (String, () -> PackResources, Position, Boolean, PackSource) -> Pack): Pack? = try {
            resources().use {
                val meta = it.metadata
                if (meta != null) return constructor(id, resources, position, required, source)
                LOGGER.warn("Failed to find metadata for pack $id!")
            }
            null
        } catch (exception: IOException) {
            LOGGER.warn("Failed to retrieve pack information for $id!", exception)
            null
        }
    }
}
