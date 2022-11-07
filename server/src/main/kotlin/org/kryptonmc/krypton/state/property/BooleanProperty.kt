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
package org.kryptonmc.krypton.state.property

import com.google.common.collect.ImmutableSet

class BooleanProperty(name: String) : KryptonProperty<Boolean>(name, Boolean::class.java) {

    override val values: Collection<Boolean>
        get() = VALUES

    override fun idFor(value: Boolean): Int = if (value) 1 else 0

    override fun fromString(value: String): Boolean? = value.toBooleanStrictOrNull()

    override fun toString(value: Boolean): String = value.toString()

    @Suppress("MagicNumber") // This is a hash code function
    override fun generateHashCode(): Int = 31 * super.generateHashCode() + VALUES.hashCode()

    companion object {

        private val VALUES = ImmutableSet.of(true, false)
    }
}
